import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import edu.princeton.cs.algs4.StdIn;

public class Percolation {
	
	private final WeightedQuickUnionUF uf;
	private final int N;
	private boolean grid[][];
	private boolean full[][];
	private int open_count;
	
	public Percolation(int n) {
		if (n <= 0) throw new IllegalArgumentException("N must be greater then 0");
		this.N = n;
		this.open_count = 0;
		this.uf = new WeightedQuickUnionUF(n*n + 2);
		grid = new boolean[n][n];
		full = new boolean[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				grid[i][j] = false;
				full[i][j] = false;
			}
		}
	}
	private int getAbsolutePos(int row,int col) {
		return (row - 1)*this.N + (col);
	}
	private void connect(int row1,int col1,int row2,int col2) {
		int cell1 = getAbsolutePos(row1,col1);
		int cell2 = getAbsolutePos(row2,col2);
		this.uf.union(cell1, cell2);
	}
	private void validate(int row,int col) {
		if ((row > this.N) || (col > this.N) || (row <= 0) || (col <= 0)) {
			throw new IllegalArgumentException("Value of Row and Column must between 0 and "+ this.N + " Value Given " + row + "," + col);
		}
	}
	public void open(int row, int col) {
		validate(row,col);
		if (this.isOpen(row,col)) return;
		this.grid[row-1][col-1] = true;
		this.open_count++;
		updateFlow();
		if (row == 1) {
			if(row+1 <= this.N) {
				if(this.isOpen(row+1,col)) connect(row,col,row+1,col);
			}
			this.uf.union(0,this.getAbsolutePos(row, col));
		}
		if (row == this.N) {
			if(row-1 >= 1) {
				if(this.isOpen(row-1,col)) connect(row,col,row-1,col);
			}
			this.uf.union(this.N*this.N + 1,this.getAbsolutePos(row, col));
		}
		else if ((row != 1) && (row != this.N)) {
			if(this.isOpen(row+1,col)) connect(row,col,row+1,col);
			if(this.isOpen(row-1,col)) connect(row,col,row-1,col);
		}
		
		if (col == 1) {
			if(col+1 <= this.N) {
				if(this.isOpen(row,col+1)) connect(row,col,row,col+1);
			}
		}
		else if (col == this.N) {
			if(col-1 >= 1) {
				if(this.isOpen(row,col-1)) connect(row,col,row,col-1);
			}
		}
		else if ((col != 1) && (col != this.N)) {
			if (this.isOpen(row,col+1)) connect(row,col,row,col+1);
			if (this.isOpen(row,col-1)) connect(row,col,row,col-1);
		}
	}
	
	public boolean isOpen(int row,int col) {
		validate(row,col);
		return this.grid[row-1][col-1];
	}
	
	public boolean isFull(int row,int col) {
		validate(row,col);
		return full[row-1][col-1];
	}
	
	private void updateFlow() {
		boolean full[][] = new boolean[this.N][this.N];
		for (int i = 1; i <= this.N; i++) {
			flow(full,1,i);
		}
		this.full = full;
	}
	private void flow(boolean[][] full,int row,int col) {
		if (row < 1 || row > this.N) return;
		if (col < 1 || col > this.N) return;
		if (!this.isOpen(row, col)) return;
		if (full[row-1][col-1]) return;
		
		full[row-1][col-1] = true;
		flow(full,row-1,col); // down
		flow(full,row,col+1); // right
		flow(full,row,col-1); // left
		flow(full,row+1,col); // up
	}
	
	public int numberOfOpenSites() {
		return this.open_count;
	}
	
	public boolean percolates() {
		return this.uf.find(0) == this.uf.find(this.N*this.N+1);
	}
	
	public static void main(String args[]) {
		int n = StdIn.readInt();
		Percolation p = new Percolation(n);
		// System.out.println("Enter row and column id to open a position, -1 to exit");
		double startTime = System.nanoTime();
		while(true) {
			int row = StdIn.readInt();
			if (row == -1) break;
			if (row == -2) { System.out.println(p.percolates()); continue; }
			if (row == -3) { System.out.println(p.numberOfOpenSites()); continue; }
			int col = StdIn.readInt();
			p.open(row,col);
		}
		double endTime = System.nanoTime();
		double timeElapsed = (endTime - startTime)/1000000000;
		System.out.println("Execution Time = " + timeElapsed);
	}
}
