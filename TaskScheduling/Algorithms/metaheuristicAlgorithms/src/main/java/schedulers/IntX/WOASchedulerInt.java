package schedulers.IntX;

import commmon.Job;
import commmon.Scheduler;
import commmon.SchedulerInterface;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@Slf4j
public class WOASchedulerInt extends Scheduler implements SchedulerInterface {
    // the number of whale
    private int numberWhale;
    // the number of iteration
    private int iterNumber;
    // the number of host
    private int numberHost;

    private String url = "result/INT/";

    // solution space
    private int D;
    // solutions
    private int[][] woa;
    // the solutions of every iteration
    private double[] res;
    // optimal solution
    private int[] woa_index;
    // current iteration optimal solution
    private double woa_get;

    //private Random random;


    public WOASchedulerInt(int numberWhale, int iterNumber, String url) {
        this.numberWhale = numberWhale;
        this.iterNumber = iterNumber;
        this.url += "WOA";
        this.url += url;
    }


    /**
     * initialization
     * @param taskNumber
     */
    @Override
    @SuppressWarnings("all")
    public void initialParameter(Job job) {
        numberHost = dataCenter.getHostNumber();
        res = new double[iterNumber];
        woa_get = Double.MAX_VALUE;
        //D = numberHost * job.getTaskNumber();
        D = job.getTaskNumber();
        woa = new int[numberWhale][];
        Random random = new Random(numberHost ^ job.getTaskNumber() + 1);
        for (int i = 0; i < numberWhale; i++) {
            woa[i] = Stream.generate(() -> random.nextInt(numberHost))
                    .mapToInt(Integer::intValue)
                    .limit(D)
                    .toArray();
        }
        // tasks information
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String taskInfo = homogeneous ? "is it heteogeneous\t\t" : "is it homogeneous\t\t";
        taskInfo += String.format("the number of host：%d, the number of task：%d, iteration number：%d, the number of whale： %d, ideal scheduling time：%.3f", numberHost, job.getTaskNumber(), iterNumber, numberWhale, job.getIdealTime());
        writeToFile(url, dateTime + "\t\t\t" + taskInfo);
        log.debug("initialed information");
    }

    @Override
    public double train(Job job) {
        log.debug("schedulers.WO.WOAInt...");
        initialParameter(job);
        Random random = new Random();
        for (int t = 1; t <= iterNumber; t++) {
            System.out.println("WOAInt: "+"iterNumber="+t+";taskNumber="+ job.getTaskNumber());
            for (int j = 0; j < numberWhale; j++) {
                double fitness = fitness(woa[j], job);
                if (fitness < woa_get) {
                    woa_get = fitness;
                    woa_index = woa[j];
                }
            }

            //log.debug("the value of estimation：{}", Arrays.toString(estimateWoa));
            // calculate a
            double a = 2 * (1 - t * 1.0 / iterNumber);
            // calculate c1 so as to calculate l
            double cl = -1 - t * 1.0 / iterNumber;
            // update location
            for (int j = 0; j < numberWhale; j++) {
                // initialize all the parameters
                double r = Math.random();
                double A = 2 * r * a - a;
                double C = 2 * r;
                double l = cl * Math.random() + 1;
                //double l = 2 * Math.random() - 1;
                double p = Math.random();
                int finalJ = j;
                // update location
                if (p < 0.5) {
                    if (Math.abs(A) > 1) {
                        int randNumber = random.nextInt(numberWhale);
                        int[] randIndex = woa[randNumber];
                        woa[j] = IntStream.range(0, D)
                                .parallel()
                                .map(i -> {
                                    int x = (int) Math.round(randIndex[i] - A * Math.abs(C * randIndex[i] - woa[finalJ][i]));
                                    return Math.min(Math.max(x, 0), numberHost - 1);
                                })
                                .toArray();
                    } else {
                        woa[j] = IntStream.range(0, D)
                                .parallel()
                                .map(i -> {
                                    int x = (int) Math.round(woa_index[i] - A * Math.abs(C * woa_index[i] - woa[finalJ][i]));
                                    return Math.min(Math.max(x, 0), numberHost - 1);
                                })
                                .toArray();
                    }
                } else {
                    woa[j] = IntStream.range(0, D)
                            .parallel()
                            .map(i -> {
                                int x = (int) Math.round(woa_index[i] + Math.abs(woa_index[i] - woa[finalJ][i]) * Math.exp(l) * Math.cos(2 * Math.PI * l));
                                return Math.min(Math.max(x, 0), numberHost - 1);
                            })
                            .toArray();
                }

            }
            // put the result into the array
            //res[t - 1] = woa_get / job.getIdealTime();
            res[t - 1] = woa_get;
            if (t == iterNumber) {
                System.out.println("the last distribution: " + Arrays.toString(woa_index));
            }
            //log.debug("{} the solution of current iteration：{}", t, res[t - 1]);
        }
        writeToFile(url, res);
        writeToFile("result/INT/IntWoaIndex.txt", "OWAInt");
        writeToFile("result/INT/IntWoaIndex.txt", woa_index);
        return res[iterNumber - 1];
    }


}


