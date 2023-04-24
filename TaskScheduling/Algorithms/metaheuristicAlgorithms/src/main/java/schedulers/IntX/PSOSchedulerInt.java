package schedulers.IntX;

import commmon.Job;
import commmon.Scheduler;
import commmon.SchedulerInterface;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@Slf4j
public class PSOSchedulerInt extends Scheduler implements SchedulerInterface {

    // particle number
    private int numberParticle;
    // iteration number
    private int iterNumber;
    //weight
    private double w;
    // learn factor
    private double c1;
    // learn factor
    private double c2;

    private String url = "result/INT/";
    // the number of hosts
    private int numberHost;
    //every solution of a group
    private int[][] Xid;
    //every solution speed  of a group
    private double[][] Vid;
    //the optimal solution of a particle
    private int[][] PBest;
    private double[] PBestX;
    //group optimal solution
    private int[] GBest;

    private double XBest;
    //solution
    private double[] res;
    // space of solution
    private int D;

    public PSOSchedulerInt(int numberParticle, int iterNumber, double w, double c1, double c2, String url) {
        this.numberParticle = numberParticle;
        this.iterNumber = iterNumber;
        this.w = w;
        this.c1 = c1;
        this.c2 = c2;
        this.url += "PSO";
        this.url += url;
    }

    public PSOSchedulerInt(int numberParticle, int iterNumber, String url) {
        this.numberParticle = numberParticle;
        this.iterNumber = iterNumber;
        this.w = 0.5;
        this.c1 = 1.6;
        this.c2 = 1.4;
        this.url += "PSO";
        this.url += url;
    }

    @Override
    public void initialParameter(Job job) {
        numberHost = dataCenter.getHostNumber();
        res = new double[iterNumber];
        D = job.getTaskNumber();
        Xid = new int[numberParticle][];
        Vid = new double[numberParticle][];
        PBest = new int[numberParticle][];
        PBestX = new double[numberParticle];
        XBest = Double.MAX_VALUE;
        Random random = new Random(numberHost ^ job.getTaskNumber());
        for (int i = 0; i < numberParticle; i++) {
            Xid[i] = Stream.generate(() -> random.nextInt(numberHost))
                    .mapToInt(Integer::intValue)
                    .limit(D)
                    .toArray();
            Vid[i] = Stream.generate(random::nextGaussian)
                    .mapToDouble(Double::doubleValue)
                    .limit(D)
                    .toArray();
            PBest[i] = Xid[i];
            PBestX[i] = fitness(PBest[i], job);
        }
        // the information of tasks
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String taskInfo = homogeneous ? "hosts is heterogeneous\t\t" : "hosts are homogeneous\t\t";
        taskInfo += String.format("the number of hosts：%d, the of tasks：%d, iteration number：%d, the number of particle： %d, ideal scheduling time：%.3f", numberHost, job.getTaskNumber(), iterNumber, numberParticle, job.getIdealTime());
        writeToFile(url, dateTime + "\t\t\t" + taskInfo);
        log.debug("initial the information");
    }

    @Override
    public double train(Job job) {
        log.debug("schedulers.PSOInt...");
        initialParameter(job);
        for (int t = 1; t <= iterNumber; t++) {
            System.out.println("PSOInt: "+"iterNumber="+t+";taskNumber="+ job.getTaskNumber());
            for (int j = 0; j < numberParticle; j++) {
                double fitness = fitness(Xid[j], job);
                if (fitness < PBestX[j]) {
                    PBestX[j] = fitness;
                    PBest[j] = Xid[j];
                }
                if (fitness < XBest) {
                    XBest = fitness;
                    GBest = Xid[j];
                }
            }
            for (int i = 0; i < numberParticle; i++) {
                int finalI = i;
                double Pr = Math.random();
                double Gr = Math.random();
                Vid[i] = IntStream.range(0, D)
                        .mapToDouble(x -> w * Vid[finalI][x] + c1 * Pr * (PBest[finalI][x] - Xid[finalI][x]) + c2 * Gr * (GBest[x] - Xid[finalI][x]))
                        .toArray();

                Xid[i] = IntStream.range(0, D)
                        .map(x -> {
                            int i1 = Math.round((float) (Xid[finalI][x] + Vid[finalI][x]));
                            return Math.min(Math.max(i1, 0), numberHost - 1);
                        })
                        .toArray();
            }
            // save the solution to the array
            //res[t - 1] = XBest / job.getIdealTime();
            res[t - 1] = XBest;
        }
        writeToFile(url, res);
        writeToFile("result/INT/PSOIntIndex.txt", GBest);
        return res[iterNumber - 1];
    }
}