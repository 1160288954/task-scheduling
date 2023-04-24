package schedulers.IntX;

import commmon.Job;
import commmon.Scheduler;
import commmon.SchedulerInterface;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Data
@Slf4j
public class RTGSSchedulerInt extends Scheduler implements SchedulerInterface {
    // whale number
    private int numberWhale;
    // iteration number
    private int iterNumber;
    // the number of host
    private int numberHost;

    private String url = "result/INT/";

    // solution space
    private int D;
    // solution
    private int[][] woa;
    // solution of every iteration
    private double[] res;
    // optimal solution
    private int[] woa_index;
    // the value of aim
    private double woa_get;
    private double[][] matrix = dataCenter.getMatrix();
    private double[][] jobMatrix;

    private Map<Integer, Job.Task> tasks;
    private int allTaskTime;
    private double referenceMax;
    private int numberTasks;
    private double pnB;
    private double pnL;
    private double pnR;
    //private Random random;
    public RTGSSchedulerInt(int numberWhale, int iterNumber, String url) {
        this.numberWhale = numberWhale;
        this.iterNumber = iterNumber;
        this.pnB = 0.33;
        this.pnL = 1.0;
        this.pnR = 1.0;
        this.url += "WOABLR";
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
        jobMatrix = job.getJobMatrix();
        tasks = job.getTasks();
        allTaskTime = job.getAllTime();
        referenceMax = job.getReferenceMax();
        numberTasks = job.getTaskNumber();
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
        // the information of tasks
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).toString();
        String taskInfo = homogeneous ? "hosts are heterogeous\t\t" : "hosts are homogeneous\t\t";
        taskInfo += String.format("the number of hosts：%d, the number of tasks：%d, iteration number：%d, the number of whale： %d, ideal scheduling time：%.3f", numberHost, numberTasks, iterNumber, numberWhale, job.getIdealTime());
        writeToFile(url, dateTime + "\t\t\t" + taskInfo);
        log.debug("initial information");
    }

    @Override
    public double train(Job job) {
        log.debug("schedulers.WO.WOABLR...");
        initialParameter(job);
        Random random = new Random();
        for (int t = 1; t <= iterNumber; t++) {
            System.out.println("WOABLR: "+"iterNumber="+t+";taskNumber="+ job.getTaskNumber());
            log.debug("迭代：{}", t);
            double fitness = 0;
            for (int j = 0; j < numberWhale; j++) {
                 fitness = fitness(woa[j], job);
                if (fitness < woa_get) {
                    woa_get = fitness;
                    woa_index = woa[j];
                }
            }
            // update location
            for (int j = 0; j < numberWhale; j++) {
                // initial parameters
                double a = 2 * (1 - t * 1.0 / iterNumber);
                double r = Math.random();
                double C = 2 * r - 1;
                double A = 2 * r * a - a;
                double l = 2 * Math.random() - 1;
                double b = 1.0;
                double p = Math.random();
                //new location of whale
                double AorE = 0;
                int[] woa_afterC;
                // update location
                //if (Math.abs(A) > 1) {
                //    AorE = A;
                //    int randNumber = random.nextInt(numberWhale);
                //    woa_afterC = createNewWOAforIndex(a, C, woa[randNumber]);
                //    //woa[j] = createNewWOA(AorE, woa[randNumber], woa_afterC, woa[j], a);
                //    //woa[j] = createNewWOA(AorE, woa_afterC, woa_afterC, woa[j], a);
                //    woa[j] = createNewWOAWithRandom(AorE, woa[randNumber], woa[randNumber], woa[j], a);
                //} else {
                //    if ( p > 0.5) {
                //        AorE = Math.exp(l * b) * Math.cos(2 * Math.PI * l);
                //        woa[j] = createNewWOAwithSpiral(AorE, woa_index, woa[j], a);
                //    }
                //    else {
                //        AorE = A;
                //        woa_afterC = createNewWOAforIndex(a, C, woa_index);
                //        //woa[j] = createNewWOA(AorE, woa_index, woa_afterC, woa[j], a);
                //        //woa[j] = createNewWOA(AorE, woa_afterC, woa_afterC, woa[j], a);
                //        woa[j] = createNewWOA(AorE, woa_index, woa_index, woa[j], a);
                //    }
                //}
                if (p > 0.5) {
                    AorE = Math.exp(l * b) * Math.cos(2 * Math.PI * l);
                    woa[j] = createNewWOAwithSpiral(AorE, woa_index, woa[j], a);
                } else {
                    if ( Math.abs(A) > 1) {
                        AorE = A;
                        int randNumber = random.nextInt(numberWhale);
                        woa_afterC = createNewWOAforIndex(a, C, woa[randNumber]);
                        //woa[j] = createNewWOA(AorE, woa[randNumber], woa_afterC, woa[j], a);
                        //woa[j] = createNewWOA(AorE, woa_afterC, woa_afterC, woa[j], a);
                        woa[j] = createNewWOAWithRandom(AorE, woa[randNumber], woa[randNumber], woa[j], a);
                    }
                    else {
                        AorE = A;
                        woa_afterC = createNewWOAforIndex(a, C, woa_index);
                        //woa[j] = createNewWOA(AorE, woa_index, woa_afterC, woa[j], a);
                        //woa[j] = createNewWOA(AorE, woa_afterC, woa_afterC, woa[j], a);
                        woa[j] = createNewWOA(AorE, woa_index, woa_index, woa[j], a);
                    }
                }
                //task waiting for scheduling
                //if (AorE == A) {
                //    woa[j] = createNewWOA(AorE, woa_index_rand, woa[j], a);
                //} else {
                //    woa[j] = createNewWOAwithSpiral(AorE, woa_index_rand, woa[j], a);
                //}
            }
            // put the result into array
            //res[t - 1] = woa_get / job.getIdealTime();
            res[t - 1] = woa_get;
            //log.debug("{} iteration result：{}", t, res[t - 1]);
        }
        writeToFile(url, res);
        writeToFile("result/INT/BLRIndex.txt", "WOABLR");
        writeToFile("result/INT/BLRIndex.txt", woa_index);
        return res[iterNumber - 1];
    }

    private int[] createNewWOA(double AorE, int[] woa_index_random, int[] woa_afterC, int[] woa_j,double a) {
        Random random = new Random();
        double pn = 0;
        List<Integer> listStay = new LinkedList<>();
        int[] woa_new = Arrays.copyOf(woa_index_random,woa_index_random.length);
        //while (listStay.size() < numberTasks * a + numberTasks * 0.1) {
        while (listStay.size() < numberTasks * a * 0.05) {
        //while (listStay.size() < numberTasks * a * 0.5 + numberTasks * 0.1) {
            int nextInt = random.nextInt(numberTasks);
            if (!listStay.contains(nextInt)) {
                listStay.add(nextInt);
                woa_new[nextInt] = -1;
            }
            if (listStay.size() == numberTasks) {
                break;
            }
        }
        if (listStay.size() != 0) {
            pn = listStay.stream()
                    .map(x -> tasks.get(x).getSpanTime())
                    .reduce(0, Integer::sum);
        }
        pn = allTaskTime - pn;
        int index;
        for (Integer integer : listStay) {
            pn = pn + tasks.get(integer).getSpanTime();
            index = taskCloseHost(AorE,woa_index_random, woa_afterC, woa_j, woa_new, integer, pn / allTaskTime);
            woa_new[integer] = index;
        }
        return woa_new;
    }
    private int[] createNewWOAWithRandom(double AorE, int[] woa_index_random, int[] woa_afterC, int[] woa_j,double a) {
        Random random = new Random();
        double pn = 0;
        List<Integer> listStay = new LinkedList<>();
        int[] woa_new = Arrays.copyOf(woa_index_random,woa_index_random.length);
        //while (listStay.size() < numberTasks * a * 0.5 + numberTasks * 0.1) {
        while (listStay.size() < numberTasks * a * 0.5) {
            int nextInt = random.nextInt(numberTasks);
            if (!listStay.contains(nextInt)) {
                listStay.add(nextInt);
                woa_new[nextInt] = -1;
            }
            if (listStay.size() == numberTasks) {
                break;
            }
        }
        if (listStay.size() != 0) {
            pn = listStay.stream()
                    .map(x -> tasks.get(x).getSpanTime())
                    .reduce(0, Integer::sum);
        }
        pn = allTaskTime - pn;
        int index;
        for (Integer integer : listStay) {
            pn = pn + tasks.get(integer).getSpanTime();
            index = taskCloseHost(AorE,woa_index_random, woa_afterC, woa_j, woa_new, integer, pn / allTaskTime);
            woa_new[integer] = index;
        }
        return woa_new;
    }
    private int[] createNewWOAwithSpiral(double AorE, int[] woa_index, int[] woa_j, double a) {
        Random random = new Random();
        double pn = 0;
        List<Integer> listStay = new LinkedList<>();
        int[] woa_new = Arrays.copyOf(woa_index,woa_index.length);
        // while (listStay.size() < numberTasks * a + numberTasks * 0.1) {
        while (listStay.size() < numberTasks * a * 0.05) {
        //while (listStay.size() < numberTasks * a * 0.5 + numberTasks * 0.1) {
            int nextInt = random.nextInt(numberTasks);
            if (!listStay.contains(nextInt)) {
                listStay.add(nextInt);
                woa_new[nextInt] = -1;
            }
            if (listStay.size() == numberTasks) {
                break;
            }
        }
        if (listStay.size() != 0) {
            pn = listStay.stream()
                    .map(x -> tasks.get(x).getSpanTime())
                    .reduce(0, Integer::sum);
        }
        pn = allTaskTime - pn;
        int index;
        for (Integer integer : listStay) {
            pn = pn + tasks.get(integer).getSpanTime();
            index = taskCloseHostforSpiral(AorE, woa_index, woa_j, woa_new, integer, pn / allTaskTime);
            woa_new[integer] = index;
        }
        return woa_new;
    }
    private int taskCloseHost(double AorE, int[] woa_index_rand, int[] woa_afterC, int[] woa_j, int[] woa_new, int taskNumber, double pn) {
        double min = Double.MAX_VALUE;
        int index = 0;
        double loadIndexRand;
        double loadAfterC;
        double loadOld;
        double loadHope;
        double[] loadNew = new double[numberHost];
        double[] loadprop = new double[numberHost];
        double[] distance;
        int host_index_rand = woa_index_rand[taskNumber];
        int host_old = woa_j[taskNumber];
        int host_afterC = woa_afterC[taskNumber];

        loadIndexRand = IntStream.range(0, D)
                .parallel()
                .filter(x -> woa_index_rand[x] == host_index_rand)
                .map(x -> tasks.get(x).getSpanTime())
                .reduce(0, Integer::sum);
        loadOld = IntStream.range(0, D)
                .parallel()
                .filter(x -> woa_j[x] == host_old)
                .map(x -> tasks.get(x).getSpanTime())
                .reduce(0, Integer::sum);
        loadAfterC = IntStream.range(0, D)
                .parallel()
                .filter(x -> woa_afterC[x] == host_afterC)
                .map(x -> tasks.get(x).getSpanTime())
                .reduce(0, Integer::sum);
        // loadHope = loadIndexRand - AorE * (loadAfterC - loadOld);
        if (Math.random() > 0.5){
            loadHope = loadIndexRand - AorE * (loadAfterC - loadOld);
        }else{
            loadHope = loadIndexRand + AorE * (loadAfterC - loadOld);
        }

        int h;
        for (int i=0; i < numberHost; i++) {
            loadNew[i] = 0;
        }
        for (int i=0; i < D; i++) {
            h = woa_new[i];
            if (h == -1) {
                continue;
            }
            loadNew[h] += tasks.get(i).getSpanTime();
        }

        //for (int i=0; i < numberHost; i++) {
        //    int ii = i;
        //    loadNew[i] = IntStream.range(0, D)
        //            .parallel()
        //            .filter(x -> woa_new[x] == ii)
        //            .map(x -> tasks.get(x).getSpanTime())
        //            .reduce(0, Integer::sum);
        //}

        for (int i=0; i < numberHost; i++) {
            loadprop[i] = (loadNew[i] + tasks.get(taskNumber).getSpanTime()) / pn;
        }

        distance = Arrays.stream(loadprop)
                .map(x -> Math.abs(loadHope - x))
                .toArray();

        for (int i=0; i < numberHost; i++) {
            if (min > distance[i]) {
                min = distance[i];
                index = i;
            }
        }

        return index;
    }
    private int taskCloseHostforSpiral(double AorE, int[] woa_index, int[] woa_j, int[] woa_new, int taskNumber, double pn) {
        double min = Double.MAX_VALUE;
        int index = 0;
        double loadIndex;
        double loadOld;
        double loadHope;
        double[] loadNew = new double[numberHost];
        double[] loadprop = new double[numberHost];
        double[] distance;
        int host_index = woa_index[taskNumber];
        int host_old = woa_j[taskNumber];

        loadIndex = IntStream.range(0, D)
                .parallel()
                .filter(x -> woa_index[x] == host_index)
                .map(x -> tasks.get(x).getSpanTime())
                .reduce(0, Integer::sum);
        loadOld = IntStream.range(0, D)
                .parallel()
                .filter(x -> woa_j[x] == host_old)
                .map(x -> tasks.get(x).getSpanTime())
                .reduce(0, Integer::sum);
        //loadHope = loadIndex + AorE * (loadIndex - loadOld);
        if (Math.random() > 0.5){
            loadHope = loadIndex - AorE * (loadIndex - loadOld);
        }else {
            loadHope = loadIndex + AorE * (loadIndex - loadOld);
        }

        int h;
        for (int i=0; i < numberHost; i++) {
            loadNew[i] = 0;
        }
        for (int i=0; i < D; i++) {
            h = woa_new[i];
            if (h == -1) {
                continue;
            }
            loadNew[h] += tasks.get(i).getSpanTime();
        }
        //for (int i=0; i < numberHost; i++) {
        //    int ii = i;
        //    loadNew[i] = IntStream.range(0, D)
        //            .parallel()
        //            .filter(x -> woa_new[x] == ii)
        //            .map(x -> tasks.get(x).getSpanTime())
        //            .reduce(0, Integer::sum);
        //}

        for (int i=0; i < numberHost; i++) {
            loadprop[i] = (loadNew[i] + tasks.get(taskNumber).getSpanTime()) / pn;
        }

        distance = Arrays.stream(loadprop)
                .map(x -> Math.abs(loadHope - x))
                .toArray();

        for (int i=0; i < numberHost; i++) {
            if (min > distance[i]) {
                min = distance[i];
                index = i;
            }
        }

        return index;
    }
    private int taskCloseHostforIndex(double C, int[] woa_index, int[] indexwoa_new, int taskNumber, double pn) {
        double min = Double.MAX_VALUE;
        int index = 0;
        double loadIndex;
        double loadHope;
        double[] loadNew = new double[numberHost];
        double[] loadprop = new double[numberHost];
        double[] distance;
        int host_index = woa_index[taskNumber];

        loadIndex = IntStream.range(0, D)
                .parallel()
                .filter(x -> woa_index[x] == host_index)
                .map(x -> tasks.get(x).getSpanTime())
                .reduce(0, Integer::sum);
        //loadHope = loadIndex - C * loadIndex;
        if (Math.random() > 0.5){
            loadHope = loadIndex - C * loadIndex;
        }else{
            loadHope = loadIndex + C * loadIndex;
        }

        int h;
        for (int i=0; i < numberHost; i++) {
            loadNew[i] = 0;
        }
        for (int i=0; i < D; i++) {
            h = indexwoa_new[i];
            if (h == -1) {
                continue;
            }
            loadNew[h] += tasks.get(i).getSpanTime();
        }
        //for (int i=0; i < numberHost; i++) {
        //    int ii = i;
        //    loadNew[i] = IntStream.range(0, D)
        //            .parallel()
        //            .filter(x -> indexwoa_new[x] == ii)
        //            .map(x -> tasks.get(x).getSpanTime())
        //            .reduce(0, Integer::sum);
        //}

        for (int i=0; i < numberHost; i++) {
            loadprop[i] = (loadNew[i] + tasks.get(taskNumber).getSpanTime()) / pn;
        }

        distance = Arrays.stream(loadprop)
                .map(x -> Math.abs(loadHope - x))
                .toArray();

        for (int i=0; i < numberHost; i++) {
            if (min > distance[i]) {
                min = distance[i];
                index = i;
            }
        }

        return index;
    }
    private int[] createNewWOAforIndex (double a, double C, int[] woa_index) {
        Random random = new Random();
        double pn = 0;
        List<Integer> listStay_index = new LinkedList<>();
        int[] woa_new = Arrays.copyOf(woa_index,woa_index.length);
        while (listStay_index.size() < numberTasks * 0.1 * a / 2) {
        //while (listStay_index.size() < 0) {
            int nextInt = random.nextInt(numberTasks);
            if (!listStay_index.contains(nextInt)) {
                listStay_index.add(nextInt);
                woa_new[nextInt] = -1;
            }
        }
        if (listStay_index.size() != 0) {
            pn = listStay_index.stream()
                    .map(x -> tasks.get(x).getSpanTime())
                    .reduce(0, Integer::sum);
        }
        pn = allTaskTime - pn;
        int index;
        for (Integer integer : listStay_index) {
            pn = pn + tasks.get(integer).getSpanTime();
            index = taskCloseHostforIndex(C, woa_index, woa_new, integer, pn / allTaskTime);
            woa_new[integer] = index;
        }
        return woa_new;
    }
}


