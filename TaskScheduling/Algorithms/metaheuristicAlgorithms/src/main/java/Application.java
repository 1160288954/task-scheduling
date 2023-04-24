import commmon.Configure;
import commmon.Scheduler;
import commmon.SchedulerInterface;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;

@Slf4j
public class Application {
    @SneakyThrows
    public static void main(String[] args) {
        Configure conf = Configure.creatConfigure();
        Scheduler scheduler = new Scheduler(conf);
        scheduler.creatJobSubmitter();
        scheduler.creatDataCenter();
        SchedulerInterface[] schedulerInterfaces = conf.creatScheduler();
        scheduler.addParallelScheduler(schedulerInterfaces);
        for (int i = 0; i < 1; i++) {
            log.debug("iterNumber: {}", i);
            scheduler.executeParallel();
            //scheduler.executeSerial();
        }
        Map<String, Map<Integer, Double>> average = scheduler.getAverage();
        writeToFile("result" + conf.getHostNumbers(), average.toString());
        scheduler.writeJobsIdealTime("result/INT/jobsIdealTime.txt"); 
        System.out.println(average);
    }

    @SneakyThrows
    public static void writeToFile(String url, String date) {
        @Cleanup
        FileOutputStream fos = new FileOutputStream(url, true);
        @Cleanup
        OutputStreamWriter osw = new OutputStreamWriter(fos);
        @Cleanup
        BufferedWriter bw = new BufferedWriter(osw);

        bw.write(date);
        bw.newLine();
    }
}
