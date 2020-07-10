import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    // state = 0 site is blocked
    // state = 1 site is open
    private final boolean[] states;
    private final int length;
    private final WeightedQuickUnionUF uf;
    private int numberOfOpenSites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // the second last site is virtual top site
        // the last site is virtual bottom site
        if (n <= 0) {
            throw new IllegalArgumentException("The length of size is illegal!");
        } else {
            uf = new WeightedQuickUnionUF(n * n + 2);
            states = new boolean[n * n];
            length = n;
            numberOfOpenSites = 0;
        }
    }

    // returns the number of element using its position
    private int convertCoordinate(int row, int col) {
        if (row <= 0 || row > length || col <= 0 || col > length) {
            throw new IllegalArgumentException("The position is illegal!");
        } else {
            return (row - 1) * length + col - 1;
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        int n = convertCoordinate(row, col);
        if (!isOpen(row, col)) {
            // connect the site in the first row to the virtual top site
            if (row == 1) {
                uf.union(n, length * length);
            }

            // connect the site in the last row to the virtual bottom site
            if (row == length) {
                uf.union(n, length * length + 1);
            }

            // connect the site with its surrounding sites
            if (row + 1 <= length && isOpen(row + 1, col)) {
                uf.union(n, convertCoordinate(row + 1, col));
            }

            if (row - 1 >= 1 && isOpen(row - 1, col)) {
                uf.union(n, convertCoordinate(row - 1, col));
            }

            if (col + 1 <= length && isOpen(row, col + 1)) {
                uf.union(n, convertCoordinate(row, col + 1));
            }

            if (col - 1 >= 1 && isOpen(row, col - 1)) {
                uf.union(n, convertCoordinate(row, col - 1));
            }

            // updates state and number of open sites
            states[n] = true;
            numberOfOpenSites++;
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        int n = convertCoordinate(row, col);
        return states[n];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int n = convertCoordinate(row, col);
        return uf.find(length * length) == uf.find(n);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return uf.find(length * length) == uf.find(length * length + 1);
    }

    // test client (optional)
    public static void main(String[] args) {
        // Percolation t = new Percolation(0); // exception
        Percolation p = new Percolation(5);
        // p.open(6, 5); // exception
        // p.open(1, 0); // exception
        p.open(1, 5);
        p.open(2, 5);
        p.open(3, 5);
        p.open(3, 4);
        System.out.println(p.percolates()); // false
        p.open(3, 3);
        p.open(4, 3);
        p.open(5, 3);
        p.open(1, 1);
        p.open(5, 1);
        System.out.println(p.percolates()); // true
        System.out.println(p.isOpen(1, 2)); // false
        System.out.println(p.isOpen(1, 1)); // true
        System.out.println(p.isFull(1, 1)); // true
        System.out.println(p.isFull(3, 1)); // false
        System.out.println(p.isFull(3, 4)); // true
        System.out.println(p.isFull(5, 1)); // backwash: false
        System.out.println(p.numberOfOpenSites()); // 9
    }
}
