package commmon;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import schedulers.WO.KWO.KWOScheduler;
import schedulers.WO.KWOA.KWOAScheduler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
public class Scheduler {

    // all the jobs
    private  List<Job> jobSubmitters = new LinkedList<>();
    // hosts
    @Getter
    protected static DataCenter dataCenter = new DataCenter();
    // the number of hosts
    protected static int numberHost;
    // the list of hosts
    protected static List<DataCenter.Host> hosts;
    // the map from task to host
    protected static Map<Integer, Integer> taskToHost;
    // configure
    protected Configure conf;
    private Random random;

    protected static boolean homogeneous = true;

    private List<SchedulerInterface> schedulerParallel = new ArrayList<>();
    private List<SchedulerInterface> schedulerSerial = new ArrayList<>();
    private Map<String, Map<Integer, List<Double>>> result = new HashMap<>();

    public Scheduler(Configure conf) {
        this.conf = conf;
        random = new Random(conf.getHostNumbers() + 1);
    }

    public void creatJobSubmitter() {
        jobSubmitters = new LinkedList<>();
        String[] file;
        int[] jobNumber = conf.getJobNumber();
        for (int i = 0; i < jobNumber.length; i++) {
            //int taskNumber = random.nextInt(conf.getTaskNumberMax() - conf.getTaskNumberMin()) + conf.getTaskNumberMin();
            Job job = new Job(i);
            file = conf.getFile();
            job.creatTask(jobNumber[i], conf.getOutDataMin(), conf.getOutDataMax(), conf.getSpanBase(), file[i]);
            jobSubmitters.add(job);
        }
    }

    public void writeJobsIdealTime(String url) {
        double[] jobsIdealTime = new double[jobSubmitters.size()];
        for (int i = 0; i < jobSubmitters.size(); i++){
            jobsIdealTime[i] = jobSubmitters.get(i).getIdealTime();
        }
        writeToFile(url, jobsIdealTime);
    }
    /**
     * create the set of hosts
     */
    public void creatDataCenter() {
        dataCenter = new DataCenter();
        numberHost = conf.getHostNumbers();
        dataCenter.creatHost(conf.getHostNumbers(), conf.getBandwidthlink(), conf.getHostCpuMin(),
                conf.getHostCpuMax(), conf.getHostMemoryMin(), conf.getHostMemoryMax(), conf.getCPUfile());
        hosts = dataCenter.getHosts();
        taskToHost = dataCenter.getTaskToHost();
        isHomogeneous();
        log.debug("create the set of hosts");
    }


    /**
     * add scheduler
     * @param scheduler
     */
    public void addParallelScheduler(SchedulerInterface scheduler) {
        schedulerParallel.add(scheduler);
    }

    /**
     * add to scheduler
     * @param scheduler
     */
    public void addParallelScheduler(SchedulerInterface[] scheduler) {
        for (SchedulerInterface schedulerInterface : scheduler) {
            schedulerParallel.add(schedulerInterface);
        }
    }

    /**
     * add scheduler
     * @param scheduler
     */
    public void addSerialScheduler(SchedulerInterface scheduler) {
        schedulerSerial.add(scheduler);
    }

    /**
     * all schedulers execute all the jobs
     * all schedulers run all the same jobs
     */
    public void executeParallel() {
        log.debug("parallel running...");
        dataCenter.setParallel(true);
        for (SchedulerInterface scheduler : schedulerParallel) {
            String name = getName(scheduler);
            //TODO statistic the time
            String url = getUrl(scheduler);
            LocalDateTime start = LocalDateTime.now();
            writeToFile(url, "start-time" + start);
            Map<Integer, List<Double>> map;
            if (result.containsKey(name)) {
                map = result.get(name);
            } else {
                map = new HashMap<>();
                result.put(name, map);
            }
            for (Job job : jobSubmitters) {
                //log.debug("job all the dependence：{}", job);
                double train = scheduler.train(job);
                if (map.containsKey(job.jobId)) {
                    List<Double> doubles = map.get(job.jobId);
                    doubles.add(train);
                } else {
                    List<Double> list = new LinkedList<>();
                    list.add(train);
                    map.put(job.jobId, list);
                }
                //TODO statistic the scheduling time of all jobs
                LocalDateTime end = LocalDateTime.now();
                writeToFile(url, String.format("job: %d:end-time: %s,and experience(ms): %s", job.getTaskNumber(), end, Duration.between(start, end).toMillis()));
            }
            //TODO statistic the scheduling time of all jobs
            LocalDateTime end = LocalDateTime.now();
            writeToFile(url, String.format("makeSpan all job:end-time: %s,and experience(ms): %s", end, Duration.between(start, end).toMillis()));
        }
    }


    /**
     * set parallel running or serial running
     * @param b
     */
    public void setParallel(boolean b) {
        dataCenter.setParallel(b);
    }

    /**
     * all the schedulers execute all the jobs
     * make sure all schedulers do the same jobs
     */
    public void executeSerial() {
        dataCenter.setParallel(false);
        log.debug("serial running...");
        for (SchedulerInterface scheduler : schedulerSerial) {

            String name = getName(scheduler);
            Map<Integer, List<Double>> map;
            if (result.containsKey(name)) {
                map = result.get(name);
            } else {
                map = new HashMap<>();
                result.put(name, map);
            }
            for (Job job : jobSubmitters) {
                //log.debug("tasks dependence relation：{}", job);
                double train = scheduler.train(job);
                if (map.containsKey(job.jobId)) {
                    List<Double> doubles = map.get(job.jobId);
                    doubles.add(train);
                } else {
                    List<Double> list = new LinkedList<>();
                    list.add(train);
                    map.put(job.jobId, list);
                }
            }
        }
    }

    /**
     * the hosts is heteogeneous or homogeneous
     */
    public void isHomogeneous() {
        int cpu = hosts.get(0).getCpu();
        int memory = hosts.get(0).getMemory();
        for (int i = 1; i < hosts.size(); i++) {
            DataCenter.Host host = hosts.get(i);
            if (host.getCpu() != cpu || host.getMemory() != memory) {
                homogeneous = false;
            }
        }
    }

    /**
     * write the result into files
     * @param ans
     */
    @SneakyThrows
    public void writeToFile(String url, double[] ans) {
        String join = Arrays.stream(ans)
                .parallel()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        writeToFile(url, join);
    }
    @SneakyThrows
    public void writeToFile(String url, int[] ans) {
        String join = Arrays.stream(ans)
                .parallel()
                .mapToObj(String::valueOf)
                .collect(Collectors.joining(","));
        writeToFile(url, join);
    }

    /**
     * write down all informations
     * @param date
     */
    @SneakyThrows
    public void writeToFile(String url, String date) {
        url = changeDir(url);

        File file = Paths.get(url).toFile().getParentFile();
        if (!file.exists()) {
            file.mkdirs();
        }

        @Cleanup
        RandomAccessFile raf = new RandomAccessFile(url, "rw");

        raf.seek(raf.length());
        date += "\n";
        raf.write(date.getBytes(StandardCharsets.UTF_8));

    }

    public double fitness(double[] X, Job job) {
        Map<Integer, Job.Task> tasks = job.getTasks();
        // create relative A matrix
        dataCenter.setTasksAll(tasks);
        hosts = dataCenter.getHosts();
        taskToHost = dataCenter.getTaskToHost();
        for (int i = 0; i < job.getTaskNumber(); i++) {
            int index = i * numberHost;
            int asInt = IntStream.range(index, index + numberHost)
                    .reduce((a, b) -> X[a] < X[b] ? b : a)
                    .getAsInt();
            hosts.get(asInt - index)
                    .addTask(tasks.get(i));
        }
        // calculate the result
        double pa = dataCenter.jobCompleteTime();
        return pa;
    }

    public double fitness(int[] X, Job job) {
        Map<Integer, Job.Task> tasks = job.getTasks();
        // create relative A matrix
        dataCenter.setTasksAll(tasks);
        hosts = dataCenter.getHosts();
        taskToHost = dataCenter.getTaskToHost();
        for (int i = 0; i < job.getTaskNumber(); i++) {
            hosts.get(X[i])
                    .addTask(tasks.get(i));
        }
        // calculate the result
        double pa = dataCenter.jobCompleteTime();
        return pa;
    }


    public String changeDir(String url) {
        String substring = url.substring(0, url.lastIndexOf("/") + 1);
        if (dataCenter.isParallel()) {
            substring += "parallel/";
        } else {
            substring += "serial/";
        }
        url = substring + url.substring(url.lastIndexOf("/") + 1);
        return url;
    }

    public int[] getHostID(double[] X, Job job) {
        int taskNumber = job.getTaskNumber();
        int[] ans = new int[taskNumber];
        for (int i = 0; i < taskNumber; i++) {
            int index = i * numberHost;
            int asInt = IntStream.range(index, index + numberHost)
                    .reduce((a, b) -> X[a] < X[b] ? b : a)
                    .getAsInt();
            ans[i] = asInt - index;
        }
        return ans;
    }

    public Map<String, Map<Integer, Double>> getAverage() {

        Map<String, Map<Integer, Double>> collect = result.entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()
                        .entrySet()
                        .stream()
                        .collect(Collectors.toMap(Map.Entry::getKey, x -> x.getValue()
                                .stream()
                                .collect(Collectors.averagingDouble(Double::doubleValue))))));
        return collect;
    }

    private String getName(SchedulerInterface scheduler) {
        String[] name = scheduler.getClass().getName().split("\\.");
        String schedulerName = name[name.length - 1].replace("Scheduler", "");
        if (scheduler instanceof KWOScheduler) {
            schedulerName += ((KWOScheduler) scheduler).getK();
        }
        if (scheduler instanceof KWOAScheduler) {
            schedulerName += ((KWOAScheduler) scheduler).getK();
        }

        return schedulerName;
    }

    private String getUrl(SchedulerInterface scheduler) {
        Pattern compile = Pattern.compile(".*?, url=(.*?), .*?");
        Matcher matcher = compile.matcher(scheduler.toString());
        if (matcher.find()) {
            return matcher.group(1).split("\\.")[0] + "-time.txt";
        }
        return "no-matcher";
    }


}
