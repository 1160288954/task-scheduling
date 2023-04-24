package commmon;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
@NoArgsConstructor
public class Job {

    //represent disconnection of the vertexes
    public static final int INF = 65535;
    //instance storage path
    private String insPath;
    public int[] layers ;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Task {
        // id
        private int taskId;
        // task number 
        private int taskData;
        // the spanning time of one task
        private int spanTime;
        // data transmission between tasks
        @Builder.Default
        private Map<Integer, Integer> retData = new HashMap<>();
        // all preceding tasks
        @Builder.Default
        private List<Integer> rely = new ArrayList<>();
        public Task(int taskId) {
            this.taskId = taskId;
        }

        public Task(int taskId, int taskData, int spanTime) {
            this.taskId = taskId;
            this.taskData = taskData;
            this.spanTime = spanTime;
        }
    }

    // task number
    private int taskNumber;
    // job id
    protected int jobId;
    // data transmission
    protected Map<Integer, Task> tasks = new HashMap<>();
    // scheduling order
    protected Map<Integer, List<Integer>> taskSchedulerOrder = new HashMap<>();
    protected double idealTime;
    //total time
    protected int allTime;
    protected double[][] jobMatrix;
    private double referenceMax;


    public Job(int jobId) {
        this.jobId = jobId;

    }

    /**
     * create the tasks list
     * @param nums       the number of tasks
     * @param minOutData the minimum transmission time from one task to another
     * @param maxOutData the maximum transmission time from one task to another
     */
    public void creatTask(int nums, int minOutData, int maxOutData, int spanBase, String path) {
        insPath = path;
        jobMatrix = new double[nums][nums];
        Random random = new Random(10);
        // create 4 or 8 numbers to the variable layers of class Job whose sum is the task amount.
        int layerNum = 0;
        if (nums < 3000) {
            layerNum = 2;
        }else {
            layerNum = 4;
        }
        layers = new int[layerNum];
        //increment any random element from the array of layers by 1, and then the sum of layers is task number.
        for (int i = 0; i < nums; i++) {
            layers[(int)(random.nextInt(layerNum))]++;
        }
        for (int i = 0; i < layerNum; i++) {
            if (i == 0) continue;
            layers[i] = layers[i - 1] + layers[i];
        }
        // initial the matrix
        for (int i = 0; i < nums; i++) {
            Arrays.fill(jobMatrix[i], INF);
        }
        List<String[]> instance = new ArrayList<>();
        try (CSVReader InstanceReader = new CSVReader(new FileReader(insPath))){
            instance = InstanceReader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvException e) {
            e.printStackTrace();
        }
        this.taskNumber = nums;
        Integer taskData;
        for (int i = 0; i < (instance.size()-1) ; i++) {
            int time = Integer.parseInt(instance.get(i+1)[7]) - Integer.parseInt(instance.get(i+1)[6]);
            if (time == 0) time =1;
            //int time = random.nextInt(maxTime - minTime) + minTime;
            allTime += time;
            taskData = (int)Float.parseFloat(instance.get(i+1)[11]);
            Task task = Task.builder().taskId(i).taskData(taskData)
                    .spanTime(time).build();
            if (i == nums - 1) {
                tasks.put(i, task);
                jobMatrix[i][i] = 0;
                continue;
            }
            // generate the data randomly
            //int ii = i;
            //List<Boolean> layerLocationMark = IntStream.range(0, layerNum)
            //        .mapToObj(x -> ii < layers[x])
            //        .collect(Collectors.toList());
            //int layerLocation = -1;
            //for (int j = 0; j < layerNum; j++) {
            //    if (layerLocationMark.get(j)) {
            //        layerLocation = j;
            //        break;
            //    }
            //}
            //if (layerLocation != layerNum - 1) {
            //    for (int j = layers[layerLocation]; j < layers[layerLocation + 1]; j++) {
            //        task.retData.put(j, random.nextInt(maxOutData - minOutData) + minOutData);
            //        jobMatrix[i][j] = 1;
            //        jobMatrix[j][i] = jobMatrix[i][j];
            //    }
            //}

            for (int j = 0; j < random.nextInt(1); j++) {
                //int k = random.nextInt(nums - i - 1) + i + 1;
                int k = random.nextInt(nums);
                if (k > i) {
                    task.retData.put(k, random.nextInt(maxOutData - minOutData) + minOutData);
                    jobMatrix[i][k] = 1;
                    jobMatrix[k][i] = jobMatrix[i][k];
                }
            }
            tasks.put(i, task);
            jobMatrix[i][i] = 0;
        }
        // using floyd algorithm to find the minimum distance from one task to another
        GraphFloydAlgorithm grap = new GraphFloydAlgorithm(jobMatrix);
        grap.floyd();
        // the maximum relationship
        for (int i = 0; i < nums; i++) {
            for (int j = i; j < nums; j++) {
                referenceMax += jobMatrix[i][j];
            }
        }
        // create the dependence relationship between tasks
        for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
            for (Integer index : entry.getValue().getRetData().keySet()) {
                tasks.get(index).getRely().add(entry.getKey());
            }
        }
        Map<Integer, Integer> tempOrder = new HashMap<>();
        // create tasks scheduling order
        for (int i = 0; i < nums; i++) {
            // get the list of preceding tasks
            List<Integer> rely = tasks.get(i).getRely();
            // if no dependence, the the value is 1
            if (rely.size() == 0) {
                tempOrder.put(i, 1);
                continue;
            }
            // get the task has the highest scheduling level
            Integer integer = rely.stream()
                    .map(tempOrder::get)
                    .max(Integer::compareTo)
                    .get();
            tempOrder.put(i, integer + 1);
        }
        // generate the scheduling order
        taskSchedulerOrder = tempOrder.entrySet()
                .stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey,
                                Collectors.toList())));
        // calculate the minimum final finish time of the job
        HashMap<Integer, Integer> timeMap = new HashMap<>();
        for (List<Integer> value : taskSchedulerOrder.values()) {
            for (Integer integer : value) {
                List<Integer> rely = tasks.get(integer).getRely();
                if (rely.size() == 0) {
                    timeMap.put(integer, tasks.get(integer).getSpanTime());
                } else {
                    int maxDelayTime = 0;
                    for (Integer integer1 : rely) {
                        maxDelayTime = Math.max(timeMap.get(integer1), maxDelayTime);
                    }
                    timeMap.put(integer, tasks.get(integer).getSpanTime() + maxDelayTime);
                }
            }
        }
        idealTime = timeMap.entrySet()
                .stream()
                .map(Map.Entry::getValue)
                .max(Integer::compareTo).get();
        //idealTime = taskSchedulerOrder.entrySet()
        //        .stream()
        //        .map(x -> x.getValue().stream().map(t -> tasks.get(t).getSpanTime()).max(Integer::compareTo).get())
        //        .reduce(Integer::sum).get();

    }

    private List<String> getRecordFromLine(String line) {
        List<String> values = new ArrayList<String>();
        try (Scanner rowScanner = new Scanner(line)){
            rowScanner.useDelimiter(",");
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }


}
