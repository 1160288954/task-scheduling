package commmon;

import lombok.Data;

import java.util.Arrays;

@Data
public class GraphFloydAlgorithm {
    //vertex
    private int[] vertexs;
    //distance
    private double[][] distances;
    //preceding vertex
    private int[][] preHeads;

    public GraphFloydAlgorithm(double[][] distances) {
        this.vertexs = new int[distances.length];
        for (int i = 0; i < distances.length; i++) {
            vertexs[i] = i;
        }
        this.distances = distances;
        this.preHeads = new int[distances.length][distances.length];
        for (int i = 0; i < distances.length; i++) {
            Arrays.fill(preHeads[i], i);
        }
    }

    /**
     * show the figure
     * @param
     */
    public void showGraph() {
        System.out.print("\t\t\t" + this.vertexs[0]);
        for (int i = 1; i < this.vertexs.length; i++) {
            System.out.format("%10d", this.vertexs[i]);
        }
        System.out.println();
        for (int i = 0; i < this.vertexs.length; i++) {
            System.out.print(this.vertexs[i] + " ");
            for (int j = 0; j < this.vertexs.length; j++) {
                System.out.format("%2d/%8d", this.preHeads[i][j], this.distances[i][j]);
            }
            System.out.println();
        }
    }

    //floyd algorithm
    public void floyd() {
        //remain distance
        double len = 0;
        //spread middle vertex
        for (int k = 0; k < distances.length; k++) {
            //starting vertex
            for (int i = 0; i < distances.length; i++) {
                for (int j = 0; j < distances.length; j++) {
                    len = distances[i][k] + distances[k][j];
                    if (len < distances[i][j]) {
                        distances[i][j] = len;
                        preHeads[i][j] = preHeads[k][j];
                    }
                }
            }
        }


    }
}
