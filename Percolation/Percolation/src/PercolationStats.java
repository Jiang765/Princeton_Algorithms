import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    // the number of repeating the computation experiment
    private static final double CONFIDENCE = 1.96;
    private double mean;
    private double stddev;
    private double confidenceLo;
    private double confidenceHi;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("The input(s) is(are) illegal!");
        } else {
            double[] results = new double[trials];

            for (int i = 0; i < trials; i++) {
                Percolation grid = new Percolation(n);
                // open a site uniformly at random among all blocked sites
                while (!grid.percolates()) {
                    int row = StdRandom.uniform(1, n + 1);
                    int col = StdRandom.uniform(1, n + 1);
                    if (!grid.isOpen(row, col)) {
                        grid.open(row, col);
                    }
                }

                // record the result
                results[i] = (double) grid.numberOfOpenSites() / (n * n);
                mean = StdStats.mean(results);
                stddev = StdStats.stddev(results);
                confidenceLo = mean() - CONFIDENCE * stddev() / Math.sqrt(results.length);
                confidenceHi = mean() + CONFIDENCE * stddev() / Math.sqrt(results.length);
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return confidenceLo;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return confidenceHi;
    }

    // test client (see below)
    public static void main(String[] args) {
        PercolationStats test = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        // PercolationStats test = new PercolationStats(100, 100);
        System.out.println("mean    = " + test.mean());
        System.out.println("stddev  = " + test.stddev());
        System.out.println("95% confidence interval = [" + test.confidenceLo() + ", " + test.confidenceHi() + "]");
    }
}