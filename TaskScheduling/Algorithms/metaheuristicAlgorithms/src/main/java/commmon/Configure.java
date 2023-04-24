package commmon;

import lombok.*;

import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Properties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Configure {
    // total number of job
    private int[] jobNumber;
    //// the max number of task
    //private int taskNumberMax;
    //// the min number of task
    //private int taskNumberMin;
    // max spanning time of each task 
    private int spanTimeMax;
    // min spanning time of each task
    private int spanTimeMin;
    // biggest data generated from each task
    private int outDataMax;
    // smallest data generated from each task
    private int outDataMin;
    // largest spanning time of each task
    private int spanDataMax;
    // smallest spanning time of each task
    private int spanDataMin;
    // baseline of spanning time and generated data
    private int spanBase;


    // host numbers
    private int hostNumbers;
    // maximum value of host Cpu 
    private int hostCpuMax;
    // minimum value of host Cpu 
    private int hostCpuMin;
    // maximum value of host Memory
    private int hostMemoryMax;
    // minimum value of host Memory
    private int hostMemoryMin;
    // maximum value of bandwidth
    private int bandwidthMax;
    // minimum value of bandwidth
    private int bandwidthMin;
    //file link of each host bandwidth
    private String bandwidthlink;
    //create array
    private String[] scheduler;
    //create file arrays
    private String[] file;
    //file of host CPU in Alibaba
    private String CPUfile;

    @SneakyThrows
    public SchedulerInterface[] creatScheduler() {
        SchedulerInterface[] schedulerInterfaces = new SchedulerInterface[scheduler.length];
        String url = hostNumbers + ".txt";
        for (int i = 0; i < scheduler.length; i++) {
            Class<?> aClass = Class.forName(scheduler[i]);
            Constructor<?> con = aClass.getConstructor(int.class, int.class, String.class);
            SchedulerInterface schedulerInterface = (SchedulerInterface) con.newInstance(50, 50, url);
            schedulerInterfaces[i] = schedulerInterface;
        }
        return schedulerInterfaces;
    }

    @SneakyThrows
    public static Configure creatConfigure() {
        @Cleanup
        // FileInputStream inputStream = new FileInputStream("/home/hadoop/configuration.properties");
        FileInputStream inputStream = new FileInputStream("./configuration.properties");
        Properties properties = new Properties();
        properties.load(inputStream);
        String proJobNumbers = properties.getProperty("config.jobNumber");
        String proSpanTimeMax = properties.getProperty("config.spanTimeMax");
        String proSpanTimeMin = properties.getProperty("config.spanTimeMin");
        String proOutDataMax = properties.getProperty("config.outDataMax");
        String proOutDataMin = properties.getProperty("config.outDataMin");
        String proSpanBase = properties.getProperty("config.spanBase");
        String proHostNumbers = properties.getProperty("config.hostNumbers");
        String proHostMemoryMax = properties.getProperty("config.hostMemoryMax");
        String proHostMemoryMin = properties.getProperty("config.hostMemoryMin");
        String proBandwidthMax = properties.getProperty("config.bandwidthMax");
        String proBandwidthMin = properties.getProperty("config.bandwidthMin");
        String bandwidthFile = properties.getProperty("config.bandwidthlink");
        String schedulerNum = properties.getProperty("config.scheduler.Number");
        String fileNum = properties.getProperty("config.file.Number");
        String CPUfile = properties.getProperty("config.CPUlink");
        int slength = Integer.parseInt(schedulerNum);
        int flength = Integer.parseInt(fileNum);
        String[] scheduler = new String[slength];
        String[] file = new String[flength];
        for (int i = 0; i < slength; i++) {
            String property = properties.getProperty(String.format("config.scheduler.clazz[%d]", i));
            scheduler[i] = property;
        }
        for (int i = 0; i < flength; i++) {
            String property = properties.getProperty(String.format("config.file.clazz[%d]", i));
            file[i] = property;
        }


        int[] jobNumbers = Arrays.stream(proJobNumbers.split(","))
                .mapToInt(Integer::parseInt)
                .toArray();
        Configure conf = Configure.builder()
                .hostNumbers(Integer.parseInt(proHostNumbers))
                .hostCpuMax(500)
                .hostCpuMin(200)
                .hostMemoryMax(Integer.parseInt(proHostMemoryMax))
                .hostMemoryMin(Integer.parseInt(proHostMemoryMin))
                .bandwidthMax(Integer.parseInt(proBandwidthMax))
                .bandwidthMin(Integer.parseInt(proBandwidthMin))
                .bandwidthlink(bandwidthFile)
                .jobNumber(jobNumbers)
                .outDataMax(Integer.parseInt(proOutDataMax))
                .outDataMin(Integer.parseInt(proOutDataMin))
                .spanBase(Integer.parseInt(proSpanBase))
                .spanTimeMax(Integer.parseInt(proSpanTimeMax))
                .spanTimeMin(Integer.parseInt(proSpanTimeMin))
                .scheduler(scheduler)
                .file(file)
                .CPUfile(CPUfile)
                .build();
        return conf;
    }

}

