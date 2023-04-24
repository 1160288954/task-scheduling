package commmon;

import java.util.*;
import java.util.stream.IntStream;

public class Kmeans {

    public static void main(String[] args) {
        double[][] ans = {
                {0, 0},
                {1, 2},
                {3, 1},
                {8, 8},
                {9, 10},
                {10, 7}
        };
        Map<Integer, List<Integer>> fit = fit(ans, 2);
        System.out.println(fit);
    }

    public static Map<Integer, List<Integer>> fit(double[][] nums, int k) {
        Random random = new Random();
        double[][] cMass = new double[k][];
        List<Integer> list = new ArrayList<>();
        Map<Integer, List<Integer>> currentMap = new HashMap<>();
        double[][] PreMass = new double[k][];
        while (list.size() < k) {
            int nextInt = random.nextInt(nums.length);
            while (list.contains(nextInt)) {
                nextInt = random.nextInt(nums.length);
            }
            list.add(nextInt);
        }
        for (int i = 0; i < k; i++) {
            cMass[i] = nums[list.get(i)];
            currentMap.put(i, new ArrayList<>());
            PreMass[i] = cMass[i];
        }
        for (int t = 0; t < 400; t++) {
            for (int i = 0; i < k; i++) {
                currentMap.get(i).clear();
            }
            for (int i = 0; i < nums.length; i++) {
                double min = norm(cMass[0], nums[i]);
                int index = 0;
                for (int j = 1; j < k; j++) {
                    double norm = norm(cMass[j], nums[i]);
                    if (norm < min) {
                        min = norm;
                        index = j;
                    }
                }
                currentMap.get(index).add(i);
            }
            for (int i = 0; i < k; i++) {
                List<Integer> list1 = currentMap.get(i);
                PreMass[i] = cMass[i];
                cMass[i] = Arrays.stream(list1.stream()
                        .parallel()
                        .map(x -> nums[x])
                        .reduce((a, b) -> IntStream.range(0, a.length)
                                .mapToDouble(x -> a[x] + b[x])
                                .toArray())
                        .get())
                        .map(x -> x / list1.size())
                        .toArray();
            }
            for (int i = 0; i < k; i++) {
                if (norm(PreMass[i], cMass[i]) > 1) {
                    break;
                }
                if (i == k - 1) {
                    return currentMap;
                }
            }
        }
        return currentMap;
    }

    private static double norm(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }
}
