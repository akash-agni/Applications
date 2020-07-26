import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import java.lang.Math;

public class PercolationStats {
	private final int T;
	private final int N;
	private double exp_result[];
	private final double tValue = 1.96;
	
	public PercolationStats(int n, int T) {
		if (n <= 1 || T < 1) {
			throw new IllegalArgumentException("N and  T should be > 1");
		}
		this.N = n;
		this.T = T;
		this.exp_result = new double[T];
		int freq[] = new int[this.N+1];
		freq[0] = 0;
		for (int i = 1; i < freq.length; i++) freq[i] = 1;
		
		for (int i = 1; i <= T; i++) {
			Percolation p = new Percolation(this.N);
			boolean flag = false;
			while(!flag) {
				int row = StdRandom.discrete(freq);
				int col = StdRandom.discrete(freq);
				//if ((row > this.N) || (col > this.N) || (row <= 0) || (col <= 0)) continue;
				if(p.isOpen(row, col)) continue;
				p.open(row, col);
				flag = p.percolates();
			}
			double open_blocks = p.numberOfOpenSites();
			double n1 = n;
			double threshold = open_blocks/(n1*n1);
			this.exp_result[i-1] = threshold;
		}
	}
	
	public double mean() {
		double mu = StdStats.mean(this.exp_result);
		return mu;
	}
	
	public double stddev() {
		double std = StdStats.stddev(this.exp_result);
		return std;
	}
	
	public double confidenceLo() {
		double s = this.stddev();
		double root_T = Math.sqrt(this.T);
		double mu = this.mean();
		return mu - ((this.tValue*s)/root_T);
	}
	
	public double confidenceHi() {
		double s = this.stddev();
		double root_T = Math.sqrt(this.T);
		double mu = this.mean();
		return mu + ((this.tValue*s)/root_T);
	}
	
	public static void main(String[] args) {
		double startTime = System.nanoTime();
		int n = Integer.parseInt(args[0]);
		int T = Integer.parseInt(args[1]);
		PercolationStats ps = new PercolationStats(n,T);
		double endTime = System.nanoTime();
		double timeElapsed = (endTime - startTime)/1000000000;
		System.out.println("Execution Time = " + timeElapsed);
		System.out.println("mean\t= " + ps.mean());
		System.out.println("stddev\t= " + ps.stddev());
		System.out.println("95% confidence interval\t= ["+ ps.confidenceLo() + "," + ps.confidenceHi() + "]");
	}

}
