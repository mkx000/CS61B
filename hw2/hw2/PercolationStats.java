package hw2;

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

import java.util.Map;

public class PercolationStats {
    private double mean;
    private double stddev;
    private double conLow;
    private double conHigh;
    private final static double E = 1.96;

    private static int SEED = 10;

    public PercolationStats(int N, int T, PercolationFactory pf) throws java.lang.IllegalArgumentException {
        if (N <= 0 || N < 0) {
            throw new java.lang.IllegalArgumentException();
        }
        pf = new PercolationFactory();
        double[] thresholds = new double[T];
        for (int i = 0; i < T; i++) {
            StdRandom.setSeed(SEED++);
            Percolation per = pf.make(N);
            int threshold = 0;
            while (!per.percolates()) {
                int row = StdRandom.uniform(N);
                int col = StdRandom.uniform(N);
                if (per.isOpen(row, col))
                    continue;
                per.open(row, col);
                threshold++;
                if (per.percolates())
                    break;
            }
            thresholds[i] = threshold;
        }

        mean = StdStats.mean(thresholds);
        stddev = StdStats.stddev(thresholds);
        conLow = mean - PercolationStats.E * stddev / Math.sqrt(T);
        conHigh = mean + PercolationStats.E * stddev / Math.sqrt(T);
    }


    public double mean() {
        return mean;
    }

    public double stddev() {
        return stddev;
    }

    public double confidenceLow() {
        return conLow;
    }

    public double confidenceHigh() {
        return conHigh;
    }

    public static void main(String[] args) {
        PercolationFactory pf = new PercolationFactory();
        PercolationStats pt = new PercolationStats(30, 10, pf);
        System.out.println(pt.mean + " " + pt.stddev + " " + pt.conHigh + " " + pt.conLow);
    }
}
