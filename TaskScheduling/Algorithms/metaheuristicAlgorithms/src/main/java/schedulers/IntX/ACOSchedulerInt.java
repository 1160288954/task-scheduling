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
public class ACOSchedulerInt extends Scheduler implements SchedulerInterface {

    // the number of ant
    private int antNumber;

    // iteration number
    private int iterNumber;

    // pheromone
    private double[] pheromones;
    //// α
    //private int alpha;
    //// β
    //private int beta;

    //volatile factor
    private double p = 0.7;
    // space of solution
    private int D;
    // increase of volatile factor
    private double Q = 3;

    // solution
    private int[][] woa;
    // current solution
    private double[] res;
    // optimal solution
    //private double[] woa_index;
    private double woa_get;

    private String url = "result/INT/";
    // the aim of every ant
    private double[] woap;
    // the proportion of the volatile factor
    private double[] pij;

    //private Random random;

    public ACOSchedulerInt(int antNumber, int iterNumber, String url) {

        this.antNumber = antNumber;
        this.iterNumber = iterNumber;
        this.url += "ACO";
        this.url += url;
    }

    /**
     * initialization
     * @param taskNumber
     */
    @Override
    @SuppressWarnings("all")
    public void initialParameter(Job job) {
        int taskNumber = job.getTaskNumber();
        D = taskNumber;
        // initialize the volatile factor
        pheromones = new double[taskNumber * numberHost];
        Arrays.fill(pheromones, 1.0);
        pij = new double[taskNumber * numberHost];
        res = new double[iterNumber];
        woap = new double[antNumber];
        woa_get = Double.MAX_VALUE;
        Random random = new Random(numberHost ^ taskNumber);
        woa = new int[antNumber][D];
        for (int i = 0; i < antNumber; i++) {
            woa[i] = Stream.generate(() -> random.nextInt(numberHost))
                    .mapToInt(Integer::intValue)
                    .limit(D)
                    .toArray();
        }
        // the information of tasks
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String taskInfo = homogeneous ? "hosts are heterogeneous\t\t" : "hosts are homogeneous\t\t";
        taskInfo += String.format("the number of hosts：%d, the number of tasks：%d, iteration：%d, ant number： %d, ideal scheduling time：%.3f", numberHost, taskNumber, iterNumber, antNumber, job.getIdealTime());
        writeToFile(url, dateTime + "\t\t\t" + taskInfo);
        log.debug("initial information");
    }

    @Override
    public double train(Job job) {
        log.debug("schedulers.AC.ACO...");
        int taskNumber = job.getTaskNumber();
        initialParameter(job);
        for (int i = 1; i <= iterNumber; i++) {
            System.out.println("ACO: "+"iterNumber="+i+";taskNumber="+ job.getTaskNumber());
            for (int j = 0; j < antNumber; j++) {
                double fitness = fitness(woa[j], job);
                woap[j] = fitness;
                if (fitness < woa_get) {
                    woa_get = fitness;
                    //woa_index = woa[j];
                }
            }
            //log.debug("volatile factor：{}", Arrays.toString(pheromones));
            // pheromone volatile
            pheromones = Arrays.stream(pheromones)
                    .map(x -> x * (1 - p))
                    .toArray();
            //log.debug("after pheromone is decrease：{}", Arrays.toString(pheromones));
            // uodate pheromone
            for (int j = 0; j < antNumber; j++) {
                for (int t = 0; t < taskNumber; t++) {
                    int index = t * numberHost;
                    // get two ants path
                    int asInt = woa[j][t];
                    // update pheromone
                    pheromones[asInt + index] += woa_get / woap[j] * Q;
                }
            }
            //log.debug("after pheromone is updated：{}", Arrays.toString(pheromones));
            for (int t = 0; t < taskNumber; t++) {
                int index = t * numberHost;
                double asDouble = IntStream.range(index, index + numberHost)
                        .mapToDouble(a -> pheromones[a])
                        .reduce(Double::sum)
                        .getAsDouble();
                for (int j = index; j < index + numberHost; j++) {
                    pij[j] = pheromones[j] / asDouble;
                }
            }
            // update location
            for (int j = 0; j < antNumber; j++) {
                for (int t = 0; t < taskNumber; t++) {
                    int index = t * numberHost;
                    //random parameter
                    double randomIndex = Math.random();
                    final double[] sum = {0};
                    int asInt = index + numberHost - 1;
                    int i1 = IntStream.range(index, index + numberHost)
                            .filter(k -> {
                                sum[0] += pij[k];
                                return sum[0] >= randomIndex;
                            })
                            .findFirst()
                            .orElse(asInt);
                    // update pheeromone
                    woa[j][t] = i1 - index;
                }
            }
            // save the solution to the array
            //res[i - 1] = woa_get / job.getIdealTime();
            res[i - 1] = woa_get;
            //log.debug("{} iteration result：{}", i, res[i - 1]);
        }
        writeToFile(url, res);
        //writeToFile("result/INT/ACOIntIndex.txt", woa_index);
        //pheromone restore to the default value
        pheromones = null;
        return res[iterNumber - 1];
    }


}
