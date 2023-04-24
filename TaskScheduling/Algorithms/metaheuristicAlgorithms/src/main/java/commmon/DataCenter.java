package commmon;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

@Data
@NoArgsConstructor
@Slf4j
public class DataCenter implements Cloneable {

    public static final int INF = 65535;

    @Data
    public class Host implements Cloneable {
        private int hostId;
        private int cpu;
        private int memory;
        private int availableMemory;
        // available CPU
        private int availableCpu;
        // sort in task-finish-time ascending order
        private PriorityQueue<Integer> preTaskID = new PriorityQueue<>(Comparator.comparingDouble(t -> completeTask.get(t)));

        private List<Job.Task> tasks = new LinkedList<>();

        public Host(int hostId, int cpu, int memory) {
            this.hostId = hostId;
            this.cpu = cpu;
            this.memory = memory;
            this.availableMemory = memory;
            this.availableCpu = this.cpu;
        }

        public void addTask(Job.Task task) {
            tasks.add(task);
            tasks.sort(Comparator.comparingInt(Job.Task::getTaskData));
            taskToHost.put(task.getTaskId(), this.hostId);
            costTime();
        }

        public void costTime() {
            if (parallel) {
                costParallelTime();
            } else {
                costSerialTime();
            }
        }

        public void costSerialTime() {
            Iterator<Job.Task> iterator = tasks.iterator();
            boolean sign = false;
            while (iterator.hasNext() && !sign) {
                Job.Task task = iterator.next();
                double waitTime = 0;
                // all preceding tasks
                List<Integer> rely = task.getRely();
                if (rely.size() == 0) {
                    waitTime = getWaitTime(task);
                    // if no preceding tasks, run the task immediately
                    availableMemory -= task.getTaskData();
                    completeTask.put(task.getTaskId(), waitTime + task.getSpanTime());
                    // add into the array of finished tasks
                    preTaskID.add(task.getTaskId());
                    // delete the task from tasks array
                    iterator.remove();
                    sign = true;
                } else if (completeTask.keySet().containsAll(rely)) {
                    waitTime = getWaitTime(task);
                    // if all preceding tasks have finished, then get the data transmission time
                    Double aDouble = rely.stream()
                            .map(t -> tasksAll.get(t).getRetData().get(task.getTaskId()) * matrix[taskToHost.get(t)][this.hostId] + completeTask.get(t))
                            .max(Double::compareTo)
                            .get();
                    waitTime = Math.max(aDouble, waitTime);
                    completeTask.put(task.getTaskId(), waitTime + task.getSpanTime());
                    availableMemory -= task.getTaskData();
                    preTaskID.add(task.getTaskId());
                    iterator.remove();
                    sign = true;
                }
            }
        }


        public void costParallelTime() {
            Iterator<Job.Task> iterator = tasks.iterator();
            while (iterator.hasNext()) {
                Job.Task task = iterator.next();
                double waitTime = 0;
                // all preceding tasks
                List<Integer> rely = task.getRely();
                if (rely.size() == 0) {
                    // when cpu is not enough
                    if (task.getTaskData() > availableCpu) {
                        waitTime = getWaitTime(task);
                    }
                    // if no preceding tasks, run the task immediately
                    availableCpu -= task.getTaskData();
                    // the finish time will put into completeTask
                    completeTask.put(task.getTaskId(), waitTime + task.getSpanTime() * 1.0);
                    // add into finished-tasks array
                    preTaskID.add(task.getTaskId());
                    // delete the task from task queue
                    iterator.remove();
                } else if (completeTask.keySet().containsAll(rely)) {
                    // if cpu is not enough
                    if (task.getTaskData() > availableCpu) {
                        waitTime = getWaitTime(task);
                    }
                    //  if all preceding tasks have finished, then get the data transmission time
                    Double aDouble = rely.stream()
                            .map(t -> tasksAll.get(t).getRetData().get(task.getTaskId()) / matrix[taskToHost.get(t)][this.hostId] + completeTask.get(t))
                            .max(Double::compareTo)
                            .get();
                    waitTime = Math.max(aDouble, waitTime);
                    completeTask.put(task.getTaskId(), waitTime + task.getSpanTime() * 1.0);
                    iterator.remove();
                    availableCpu -= task.getTaskData();
                    preTaskID.add(task.getTaskId());
                }
            }
        }

        public double getWaitTime(Job.Task task) {
            // waiting time, if available cpu is not enough, this task would wait for the time where available cpu is enough
            double waitTime = 0;
            int sum = task.getTaskData() - availableCpu;
            while (sum > 0) {
                Integer peek = preTaskID.peek();
                // get the task
                int taskData = tasksAll.get(peek).getTaskData();
                availableCpu += taskData;
                sum -= taskData;
                // delete the task
                preTaskID.poll();
                // wait time
                waitTime = Math.max(waitTime, completeTask.get(peek));
            }
            return waitTime;
        }

        public double getEarlyStartTime(Job.Task task) {
            // wait time, if cpu is not enough, then the task need to wait for the time when the available cpu is available to run the task
            double waitTime = 0;
            // all preceding tasks
            List<Integer> rely = task.getRely();
            if (preTaskID.size() > 0) {
                for (Integer peek : preTaskID) {
                    waitTime = Math.max(waitTime, completeTask.get(peek));
                }
            }
            //if there are some preceding tasks
            if (rely.size() > 0) {
                Double aDouble = rely.stream()
                        .map(t -> tasksAll.get(t).getRetData().get(task.getTaskId()) * matrix[taskToHost.get(t)][this.hostId] + completeTask.get(t))
                        .max(Double::compareTo)
                        .get();
                waitTime = Math.max(aDouble, waitTime);
            }
            return waitTime;
        }

        public void cleanHost() {
            preTaskID.clear();
            tasks.clear();
            availableMemory = memory;
            availableCpu = cpu;
        }

        @Override
        protected Host clone() throws CloneNotSupportedException {
            return (Host) super.clone();
        }
    }

    //initial distance
    private double[][] matrix;
    // set of host
    private List<Host> hosts = new ArrayList<>();
    // hash map of tasks finish time
    private Map<Integer, Double> completeTask = new HashMap<>();
    // tasks
    private Map<Integer, Job.Task> tasksAll = new HashMap<>();
    // one task map to one host
    private Map<Integer, Integer> taskToHost = new HashMap<>();
    // the number of host
    private int hostNumber;
    // is it parallel or not
    private boolean parallel = true;
    private double[] factorial;
    private double bandWidthMax = Double.MIN_VALUE;

    public boolean isParallel() {
        return parallel;
    }


    public double jobCompleteTime() {
        while (completeTask.size() < tasksAll.size()) {
            for (Host host : hosts) {
                if (host.getTasks().size() == 0) {
                    continue;
                }
                host.costTime();
            }
        }
        //log.debug("the finish time of every tasks：{}", completeTask);
        Double time = completeTask.values()
                .stream()
                .max(Double::compareTo)
                .get();
        cleanCenter();
        return time;
    }

    public void cleanCenter() {
        for (Host host : hosts) {
            host.cleanHost();
        }
        completeTask.clear();
        taskToHost.clear();
    }


    /**
     * create host
     * @param nums        the number of host
     * @param bandwidthlink file link of the hosts bandwidth
     * @param minCpu      minimum CPU
     * @param maxCpu      maximum CPU
     * @param minMemory   minimum memory
     * @param maxMemory   maximum memory
     */
    public void creatHost(int nums, String bandwidthlink, int minCpu, int maxCpu, int minMemory, int maxMemory, String CPUlink) {
        this.hostNumber = nums;
        factorial = new double[nums];
        matrix = new double[nums][nums];
        // initial the matrix
        for (int i = 0; i < nums; i++) {
            Arrays.fill(matrix[i], INF);
            if (i == 0) {
                factorial[i] = 1;
            } else {
                factorial[i] = svmFac(i);
            }
        }
        // the seed the random number
        Random random = new Random(20);
        for (int i = 0; i < nums; i++) {
            // create host
            //Host host = new Host(i, random.nextInt(maxCpu - minCpu) + minCpu, (random.nextInt(maxMemory - minMemory) + minMemory) * 1024);
            List<String[]> CPUlist = new ArrayList<>();
            try (CSVReader hostBreader = new CSVReader(new FileReader(CPUlink))){
                CPUlist = hostBreader.readAll();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvException e) {
                e.printStackTrace();
            }
            //Host host = new Host(i, 9600 - Integer.parseInt(CPUlist.get(i+1)[2]) * 100, (random.nextInt(maxMemory - minMemory) + minMemory) * 1024);
            Host host = new Host(i, random.nextInt(maxCpu - minCpu) + minCpu, (random.nextInt(maxMemory - minMemory) + minMemory) * 1024);
            hosts.add(host);
            //if (i == nums - 1) {
            //    matrix[i][i] = 0;
            //    continue;
            //}
            // create bandwidth
            //add each bandwidth between
            List<String[]> hostsBandwidth = new ArrayList<>();
            try (CSVReader hostBreader = new CSVReader(new FileReader(bandwidthlink))){
                hostsBandwidth = hostBreader.readAll();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CsvException e) {
                e.printStackTrace();
            }
            String[] hostOut = hostsBandwidth.get(i+1);
            for (int j = 0; j < nums; j++){
                if (i == j){
                    continue;
                }
                String[] hostIn = hostsBandwidth.get(j+1);
                matrix[i][j] = Math.min(Double.parseDouble(hostOut[2]), Double.parseDouble(hostIn[1]));
            }
            //for (int j = 0; j < Math.max(random.nextInt(nums), 1); j++) {
            //    int k = random.nextInt(nums - i - 1) + i + 1;
            //    matrix[i][k] = 1.0 / (random.nextInt(maxBandWith - minBandWith) + minBandWith);
            //    matrix[k][i] = matrix[i][k];
            //}
           // matrix[i][i] = INF;
        }
        // using floyd  algorithm to generate the bandwidth from a host to another
        //GraphFloydAlgorithm grap = new GraphFloydAlgorithm(matrix);
        //grap.floyd();
        //log.debug("bandwidth between hosts：{}", JSON.toJSONString(matrix));
        //// get the maximum bandwidth
        //for (double[] doubles : matrix) {
        //    double asDouble = Arrays.stream(doubles)
        //            .max()
        //            .getAsDouble();
        //    bandWidthMax = Math.max(bandWidthMax, asDouble);
        //}
    }

    public double svmFac(int n) {
        double num = 1;
        for (int i = 1; i <= n; i++) {
            num *= 2 * 1.0 / i;
        }
        return Math.sqrt(num);
    }

    public double[] encode(int t) {
        double[] doubles = new double[hostNumber];
        double x = t * 1.0 / (hostNumber - 1);
        double A = Math.exp(-1 * Math.pow(x, 2) / 2);
        doubles[0] = A;
        for (int i = 1; i < hostNumber; i++) {
            doubles[i] = A * factorial[i] * Math.pow(x, i);
        }
        return doubles;
    }

    public int decode(double[] ans) {
        double[] doubles = new double[hostNumber - 1];
        double sum = 0;
        for (int i = 1; i < hostNumber; i++) {
            doubles[i - 1] = Math.pow(ans[i] / (ans[0] * factorial[i]), 1.0 / i);
            sum += doubles[i - 1];
        }
        return Math.round((float) sum);
    }


    @SneakyThrows
    @Override
    protected DataCenter clone() {
        DataCenter dataCenter = (DataCenter) super.clone();
        List<Host> hosts = this.getHosts();
        LinkedList<Host> hosts1 = new LinkedList<>();
        for (Host host : hosts) {
            Host clone = host.clone();
            hosts1.add(clone);
        }
        dataCenter.setHosts(hosts1);
        dataCenter.setParallel(false);
        return dataCenter;
    }
}
