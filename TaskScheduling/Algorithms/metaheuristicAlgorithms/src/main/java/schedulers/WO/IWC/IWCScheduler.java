package schedulers.WO.IWC;

import commmon.Job;
import commmon.Scheduler;
import commmon.SchedulerInterface;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Data
public class IWCScheduler extends Scheduler implements SchedulerInterface {
    // the number of whale
    private int numberWhale;
    // the number of iteration
    private int iterNumber;
    // the number of host
    private int numberHost;

    private String url = "result/WO/IWC/";

    // resolution space
    private int D;
    // resolutions
    private double[][] woa;
    // every solution in each iteration
    private double[] res;
    // optimal solution
    private double[] woa_index;
    // solution of the aim function
    private double woa_get;
    // the optimal solution in the last iteration
    private double[] woa_indexPre2;
    // the optimal solution in the last two iteration
    private double[] woa_indexPre1;
    //the method to select optimal solution
    private double alpha;
    // each iteration the best index
    private int Pbests;

    public IWCScheduler(int numberWhale, int iterNumber, double alpha, String url) {
        this.numberWhale = numberWhale;
        this.iterNumber = iterNumber;
        this.alpha = alpha;
        this.url += url;
    }

    public IWCScheduler(int numberWhale, int iterNumber, String url) {
        this.numberWhale = numberWhale;
        this.iterNumber = iterNumber;
        this.alpha = 0.25;
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
        int taskNumber = job.getTaskNumber();
        D = numberHost * taskNumber;
        woa = new double[numberWhale][];
        Random random = new Random(numberHost ^ taskNumber);
        for (int i = 0; i < numberWhale; i++) {
            //woa[i] = Stream.generate(random::nextGaussian)
            //        .mapToDouble(Double::doubleValue)
            //        .limit(D)
            //        .toArray();
            woa[i] = Stream.generate(() -> random.nextInt(numberHost))
                    .map(x -> {
                        double[] doubles = new double[numberHost];
                        Arrays.fill(doubles, 1.0);
                        doubles[x] = 3.0;
                        return doubles;
                    })
                    .limit(taskNumber)
                    .flatMapToDouble(Arrays::stream)
                    .toArray();
        }
        // the information of tasks
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String taskInfo = homogeneous ? "is it heterogeneous\t\t" : "is it homogeneous\t\t";
        taskInfo += String.format("the number of host：%d, the number of task：%d, iteration number：%d, the number of whale： %d, ideal scheduling time：%.3f", numberHost, taskNumber, iterNumber, numberWhale, job.getIdealTime());
        writeToFile(url, dateTime + "\t\t\t" + taskInfo);
        log.debug("initialized information");
    }

    @Override
    public double train(Job job) {
        log.debug("schedulers.WO.IWC...");
        initialParameter(job);
        int ps = numberWhale, PSMax = numberWhale + 1, PSMin = 10;
        byte flag1 = 0, flag2 = 0;
        for (int t = 1; t <= iterNumber; t++) {
            System.out.println("IWC: "+"iterNumber="+t+";taskNumber="+ job.getTaskNumber());
            for (int j = 0; j < numberWhale; j++) {
                double fitness = fitness(woa[j], job);
                if (fitness < woa_get) {
                    woa_get = fitness;
                    woa_index = woa[j];
                    Pbests = j;
                }
            }
            //log.debug("estimated value：{}", Arrays.toString(estimateWoa));
            // calculate the value of a
            double a = (1 - t * 1.0 / (20 * iterNumber)) + (1 - t * 1.0 / iterNumber) / (1 - 20 * 1.0 * t / iterNumber);

            if (Double.isInfinite(a)) {
                a = 100;
            }
            // calculate c1 so as to get the value of l
            double cl = -1 - t * 1.0 / iterNumber;
            /**
             * change the number of whales
             */
            if (t > 3) {
                // delete whale
                float v = (float) (Math.pow(ps, 2) * ((PSMax - ps) / Math.pow(PSMax, 2)));
                if (flag1 == 1 || flag2 == 1) {
                    int dec = Math.round(v);
                    double[][] rst = distanceSort(false);
                    for (int i = 0; i < dec; i++) {
                        Arrays.fill(woa[(int) rst[i][1]], 0);
                    }
                    ps -= dec;
                }
                // add whale
                if (flag2 == 2) {
                    int ninc = Math.round(v);
                    clusterGroups(ninc);
                    ps += ninc;
                }
            }
            // update location
            for (int j = 0; j < numberWhale; j++) {
                // initialize parameters
                double r = Math.random();
                double A = 2 * r * a - a;
                double C = 2 * r;
                double l = cl * Math.random() + 1;
                double p = Math.random();
                int finalJ = j;
                // update location
                if (p < 0.5) {
                    if (Math.abs(A) > 1) {
                        int randNumber = (int) Math.floor(Math.random() * numberWhale);
                        double[] randIndex = woa[randNumber];
                        woa[j] = IntStream.range(0, D)
                                .mapToDouble(i -> randIndex[i] - A * Math.abs(C * randIndex[i] - woa[finalJ][i]))
                                .toArray();
                    } else {
                        woa[j] = IntStream.range(0, D)
                                .mapToDouble(i -> woa_index[i] - A * Math.abs(C * woa_index[i] - woa[finalJ][i]))
                                .toArray();
                    }
                } else {
                    woa[j] = IntStream.range(0, D)
                            .mapToDouble(i -> woa_index[i] + Math.abs(woa_index[i] - woa[finalJ][i]) * Math.exp(l) * Math.cos(2 * Math.PI * l))
                            .toArray();
                }

            }
            //res[t - 1] = woa_get / job.getIdealTime();
            res[t - 1] = woa_get;
            // judge whether we should change the number of whales
            if (t > 2) {
                if (sumCount(woa_index, woa_indexPre1) < D && sumCount(woa_indexPre1, woa_indexPre2) < D && ps > PSMin) {
                    flag1 = 1;
                } else {
                    flag1 = 0;
                }
                if (sumCount(woa_index, woa_indexPre1) < D && ps == PSMax) {
                    flag2 = 1;
                } else if (sumCount(woa_index, woa_indexPre1) == D && ps < PSMax) {
                    flag2 = 2;
                } else {
                    flag2 = 0;
                }
            }
            woa_indexPre2 = woa_indexPre1;
            woa_indexPre1 = woa_index;
        }
        writeToFile(url, res);
        return res[iterNumber - 1];
    }

    /**
     * @param a
     * @param b
     * @return
     */
    private double norm(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(Math.abs(sum));
    }

    /**
     * @param a
     * @param b
     * @return
     */
    private int sumCount(double[] a, double[] b) {
        int count = 0;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b[i]) {
                count++;
            }
        }
        return count;
    }

    /**
     * add whale
     * @param nums the number of group
     * @return corresponding index
     */
    private void clusterGroups(int nums) {
        double c1 = 1.2, c2 = 1.2;
        double[][] res = distanceSort(true);
        Set<Integer> Sidex = new HashSet<>();
        for (int i = 0; i < numberWhale - 1; i++) {
            Sidex.add((int) res[i][1]);
        }
        int xmin = -1;
        int xmax = 1;
        int Nd = Math.max(Math.round(nums / 2), 1);
        int p = 2;
        int xx = 1;
        while ((p - 3) / 2 < Nd) {
            xx = xx + 1;
            p = primes(xx);
        }
        for (int i = 0; i < Nd; i++) {
            double v = 2 * Math.cos(2 * Math.PI * i / p);
            if (v >= xmin && v <= xmax) {
                Sidex.add(i);
            }
        }
        double Ud = c1 * xmax;
        double Ld = c2 * xmin;
        double R = Math.random();
        double dR = 0;
        if (R < 0.5) {
            dR = (Ud - Ld) * (2 * Math.pow(R, 2) + Ld / (Ud - Ld));
        } else {
            dR = (Ud - Ld) * (1 - 2 * Math.pow(1 - R, 2) + Ld / (Ud - Ld));
        }
        for (int i = 0; i < woa_index.length; i++) {
            woa_index[i] += dR;
        }
        Sidex.add(Pbests);
        res = distanceSort(false);
        double pow1 = Math.pow(alpha, 0.5);
        double pow2 = 1 - pow1;
        List<Integer> list = new ArrayList<>(Sidex);
        for (int i = 0; i < nums; i++) {
            int i1 = (int) (Math.random() * Sidex.size());
            int i2 = (int) (Math.random() * Sidex.size());
            while (i2 == i1) {
                i2 = (int) (Math.random() * Sidex.size());
            }
            int finalI = i2;
            for (int k = 0; k < D; k++) {
                woa[(int) res[i][1]][k] = pow1 * woa[list.get(i1)][k] + pow2 * woa[list.get(finalI)][k];
            }
        }
    }

    /**
     * @param flag true ascending order
     * @return
     */
    private double[][] distanceSort(boolean flag) {
        double[][] res = new double[numberWhale][2];
        for (int i = 0; i < numberWhale; i++) {
            res[i][0] = norm(woa_index, woa[i]);
            res[i][1] = i;
        }
        if (flag) {
            Arrays.sort(res, Comparator.comparingDouble(a -> -a[0])
            );
        } else {
            Arrays.sort(res, Comparator.comparingDouble(a -> a[0]));
        }
        return res;
    }

    /**
     * get the nth prime number
     * @param n
     * @return
     */
    private int primes(int n) {
        int i = 2, j = 1;
        while (true) {
            j = j + 1;
            if (j > i / j) {
                n--;
                if (n == 0)
                    break;
                j = 1;
            }
            if (i % j == 0) {
                i++;
                j = 1;
            }
        }
        return i;
    }

}
