import commmon.Configure;
import commmon.Scheduler;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import lombok.SneakyThrows;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

//public class PositiveRelationProveforMultiJobs extends Application {
public class PositiveRelationProveforMultiJobs {
    @SneakyThrows
    public static void main(String[] args) {
        Configure conf = Configure.creatConfigure();
        Scheduler scheduler = new Scheduler(conf);
        scheduler.creatJobSubmitter();
        scheduler.creatDataCenter();
        Random random = new Random();
        double[] correlation = new double[conf.getJobNumber().length];
        int num = 1000;
        for (int loop = 0; loop < 1; loop++) {
            System.out.printf("loop: %d%n", loop);
            Application.writeToFile("result" + conf.getHostCpuMax() + ".txt", "loop: " + loop);
            for (int jobNum = 0; jobNum < conf.getJobNumber().length; jobNum++) {
                int jobN = jobNum;
                System.out.println("current job number: " + conf.getJobNumber()[jobN]);
                Application.writeToFile("result" + conf.getHostCpuMax() + ".txt", "current job number: " + conf.getJobNumber()[jobN]);
                int[] base = Stream.generate(() -> random.nextInt(conf.getHostNumbers()))
                        .mapToInt(Integer::intValue)
                        .limit(conf.getJobNumber()[jobN])
                        .toArray();
                int[][] instance = new int[num][];
                for (int i = 0; i < num; i++) {
                    instance[i] = base.clone();
                }
                int Rnum;
                int nowTask;
                for (int i = 0; i < num; i++) {
                    Rnum = random.nextInt(conf.getJobNumber()[jobN]);
                    for (int j = 0; j < Rnum; j++) {
                        nowTask = random.nextInt(conf.getJobNumber()[jobN]);
                        instance[i][nowTask] = random.nextInt(conf.getHostNumbers());
                    }
                }
                double timeBase = scheduler.fitness(base, scheduler.getJobSubmitters().get(jobN));

                double[] timeInstance = new double[num];
                for (int i = 0; i < num; i++) {
                    timeInstance[i] = scheduler.fitness(instance[i], scheduler.getJobSubmitters().get(jobN));
                }
                double[] differentTime = Arrays.stream(timeInstance)
                        .map(x -> Math.abs(timeBase - x))
                        .toArray();

                //double[] hostDifferent = new double[num];
                //for (int i = 0; i < num; i++) {
                //    int ii = i;
                //    hostDifferent[i] = (int) IntStream.range(0, conf.getJobNumber()[jobN])
                //            .filter(x -> instance[ii][x] != base[x])
                //            .count();
                //}

                double[] loadBase = new double[conf.getJobNumber()[jobN]];
                for (int i = 0; i < conf.getJobNumber()[jobN]; i++) {
                    int ii = i;
                    loadBase[i] = IntStream.range(0, conf.getJobNumber()[jobN])
                            .parallel()
                            .filter(x -> base[x] == base[ii])
                            .map(x -> scheduler.getJobSubmitters().get(jobN).getTasks().get(x).getSpanTime())
                            .reduce(0, Integer::sum);
                }
                double[] loadInstance = new double[conf.getJobNumber()[jobN]];
                double[] differentLoad = new double[num];
                for (int j = 0; j < num; j++) {
                    int jj = j;
                    for (int i = 0; i < conf.getJobNumber()[jobN]; i++) {
                        int ii = i;
                        loadInstance[i] = IntStream.range(0, conf.getJobNumber()[jobN])
                                .parallel()
                                .filter(x -> instance[jj][x] == instance[jj][ii])
                                .map(x -> scheduler.getJobSubmitters().get(jobN).getTasks().get(x).getSpanTime())
                                .reduce(0, Integer::sum);
                    }
                    differentLoad[j] = IntStream.range(0, conf.getJobNumber()[jobN])
                            .mapToDouble(x -> Math.abs(loadBase[x] - loadInstance[x]))
                            .average()
                            .getAsDouble();
                }

                //double[] loadHostSumDifferent = IntStream.range(0, num)
                //        .mapToDouble(x -> differentLoad[x] + hostDifferent[x])
                //        .toArray();

                System.out.println("time different: " + Arrays.toString(differentTime));
                System.out.println("load different: " + Arrays.toString(differentLoad));
                Application.writeToFile("result" + conf.getHostCpuMax() + ".txt", "time different: " + Arrays.toString(differentTime));
                Application.writeToFile("result" + conf.getHostCpuMax() + ".txt", "load different: " + Arrays.toString(differentLoad));
                //System.out.println("host different: " + Arrays.toString(hostDifferent));
                //System.out.println("loadHostSum different: " + Arrays.toString(loadHostSumDifferent));

                Correlation corr = new Correlation();
                //corr.initialParameter(differentTime, differentLoad, hostDifferent, loadHostSumDifferent);
                corr.initialParameter(differentTime, differentLoad);
                correlation[jobN] = corr.getCorrLT();
                //ScatterChart<Number, Number> sc1 = createScatterChart(differentTime, differentLoad, 1, corr.getCorrLT());
                //ScatterChart<Number, Number> sc2 = createScatterChart(differentTime, hostDifferent, 2, corr.getCorrHT());
                //ScatterChart<Number, Number> sc3 = createScatterChart(differentTime, loadHostSumDifferent, 3, corr.getCorrLHT());

                //FlowPane root = new FlowPane();
                ////root.getChildren().addAll(sc1, sc2, sc3);
                //root.getChildren().addAll(sc1);
                //Scene scene = new Scene(root, 500, 400);
                //stage.setScene(scene);
                //stage.show();
            }
            System.out.println("correlation of all jobs: " + Arrays.toString(correlation));
            Application.writeToFile("result" + conf.getHostCpuMax() + ".txt", "correlation of all jobs: " + Arrays.toString(correlation));
        }
    }

    //private ScatterChart<Number, Number> createScatterChart(double[] xAxisValues, double[] yAxisValues, int mark, double corr) {
    //    final NumberAxis xAxis = new NumberAxis();
    //    final NumberAxis yAxis = new NumberAxis();
    //    xAxis.setLabel("Running Time Gap");
    //
    //    final ScatterChart<Number, Number> scatterChart = new ScatterChart<>(xAxis, yAxis);
    //    if (mark == 1){
    //        scatterChart.setTitle("load: " + Double.toString(corr));
    //        yAxis.setLabel("TLDist");
    //    }else if (mark == 2) {
    //        scatterChart.setTitle("host: " + Double.toString(corr));
    //        yAxis.setLabel("hostDifferent");
    //    }else if (mark == 3) {
    //        scatterChart.setTitle("loadandHost: " + Double.toString(corr));
    //        yAxis.setLabel("TLDist_hostDiff");
    //    }
    //    XYChart.Series<Number, Number> series = new XYChart.Series<>();
    //    for (int i = 0; i < xAxisValues.length; i++){
    //        series.getData().add(new ScatterChart.Data<>(xAxisValues[i], yAxisValues[i]));
    //    }
    //    scatterChart.getData().add(series);
    //    return scatterChart;
    //}
}
