package schedulers.GeneticAlgorithm;

import commmon.Job;
import commmon.Scheduler;
import commmon.SchedulerInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class GA extends Scheduler implements SchedulerInterface {
    // the number of people(even number)
    private int gaNumber;
    // iteration number
    private int iterNumber;
    private int codeNumber;
    // space of result
    private double[][] woa;
    // the value of every people
    private double[] woy;
    // the optimal solution
    private double[] woaOpt;
    // optimal solution
    private double woaGet;
    // current optimal solution
    private double[] res;
    // the location of the output file
    private String url = "result/GA/";
    // dimension
    private int D;
    // the number job
    private int jobNumber;

    public GA(int gaNumber, int iterNumber, String url) {
        this.codeNumber = Integer.toBinaryString(dataCenter.getHostNumber()).length();
        this.gaNumber = gaNumber;
        this.iterNumber = iterNumber;
        this.url += url;
    }

    /**
     * code the solution
     * @param X 
     * @return the solution after code
     */
    public String encode(double[] X) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < jobNumber; i++) {
            int index = i * numberHost;
            int asInt = IntStream.range(index, index + numberHost)
                    .reduce((a, b) -> X[a] < X[b] ? b : a)
                    .getAsInt();
            builder.append(String.format("%0" + codeNumber + "d", Integer.parseInt(Integer.toBinaryString(asInt - index))));
        }
        return builder.toString();
    }

    public double[] decode(String ans) {
        double[] res = new double[D];
        //Arrays.fill();
        for (int i = 0; i < jobNumber; i++) {
            int index = i * codeNumber;
            String substring = ans.substring(index, index + codeNumber);
            int i1 = Integer.parseInt(substring, 2);
            if (i1 >= 3) {
                i1 = 3;
            }
            res[i * numberHost + i1] = 1;
        }
        return res;
    }

    /**
     * iteration
     * @param job
     */
    @Override
    public double train(Job job) {
        log.debug("schedulers/GeneticAlgorithm/GA...");
        int taskNumber = job.getTaskNumber();
        initialParameter(job);
        for (int t = 1; t <= iterNumber; t++) {
            System.out.println("GA: "+"iterNumber="+t+";taskNumber="+ job.getTaskNumber());
            for (int i = 0; i < gaNumber; i++) {
                double fitness = fitness(woa[i], job);
                woy[i] = fitness;
                if (fitness < woaGet) {
                    woaGet = fitness;
                    woaOpt = woa[i];
                }
            }
            // calculate the sum solution
            double sum = Arrays.stream(woy)
                    .reduce(Double::sum)
                    .getAsDouble();
            String[] encodeWoa = new String[gaNumber];
            // get the proportion of each people
            double[] ratio = Arrays.stream(woy)
                    .map(x -> x / sum)
                    .toArray();
            // select the next people
            for (int i = 0; i < gaNumber; i++) {
                double random = Math.random();
                final double[] raSum = {0};
                int index = IntStream.range(0, gaNumber)
                        .filter(k -> {
                            raSum[0] += ratio[k];
                            return raSum[0] >= random;
                        })
                        .findFirst()
                        .orElse(gaNumber - 1);
                // to code the solution
                encodeWoa[i] = encode(woa[index]);
            }
            List<Integer> indexList = IntStream.range(0, gaNumber)
                    .boxed()
                    .collect(Collectors.toList());
            // change people
            Collections.shuffle(indexList);

            for (int i = 0; i < indexList.size(); i += 2) {
                // the location need to be change
                int v = (int) (Math.random() * codeNumber * taskNumber);
                swapBit(encodeWoa, indexList.get(i), indexList.get(i + 1), v);
                woa[i] = decode(encodeWoa[indexList.get(i)]);
                woa[i + 1] = decode(encodeWoa[indexList.get(i + 1)]);
            }
            // restore the solution to a array
            //res[t - 1] = woaGet / job.getIdealTime();
            res[t - 1] = woaGet;
        }
        writeToFile(url, res);
        writeToFile("result/INT/GAIndex.txt", woaOpt);
        return res[iterNumber - 1];
    }

    @Override
    @SuppressWarnings("all")
    public void initialParameter(Job job) {
        jobNumber = job.getTaskNumber();
        D = jobNumber * numberHost;
        woaGet = Double.MAX_VALUE;
        res = new double[iterNumber];
        Random random = new Random(numberHost ^ jobNumber);
        woa = new double[gaNumber][D];
        woy = new double[gaNumber];
        for (int i = 0; i < gaNumber; i++) {
            woa[i] = Stream.generate(() -> random.nextInt(numberHost))
                    .map(x -> {
                        double[] doubles = new double[numberHost];
                        Arrays.fill(doubles, 1.0);
                        doubles[x] = 3.0;
                        return doubles;
                    })
                    .limit(jobNumber)
                    .flatMapToDouble(Arrays::stream)
                    .toArray();
        }
        // task information
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String taskInfo = homogeneous ? "heterogeneous\t\t" : "homogeneous\t\t";
        taskInfo += String.format("the number of host：%d, the number of task：%d, iteration number：%d, the number of people： %d, ideal scheduling time：%.3f", numberHost, job.getTaskNumber(), iterNumber, gaNumber, job.getIdealTime());
        writeToFile(url, dateTime + "\t\t\t" + taskInfo);
        log.debug("initial information");
    }

    public void swapBit(String[] ans, int x1, int x2, int index) {

        StringBuilder builder = new StringBuilder();
        String x1Bit = ans[x1].substring(index, index + 1);
        String x2Bit = ans[x2].substring(index, index + 1);
        builder.append(ans[x1]);
        builder.replace(index, index + 1, x2Bit);
        int v = (int) (Math.random() * codeNumber * jobNumber);
        builder.replace(v, v + 1, String.valueOf(1 - Integer.parseInt(builder.substring(v, v + 1))));
        ans[x1] = builder.toString();

        builder.delete(0, builder.length());
        builder.append(ans[x2]);
        builder.replace(index, index + 1, x1Bit);
        v = (int) (Math.random() * codeNumber * jobNumber);
        builder.replace(v, v + 1, String.valueOf(1 - Integer.parseInt(builder.substring(v, v + 1))));
        ans[x2] = builder.toString();
    }
}
