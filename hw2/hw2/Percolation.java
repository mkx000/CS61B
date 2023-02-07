package hw2;

import java.io.File;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;

    private WeightedQuickUnionUF topUf, bottomUf;
    private int topNode, bottomNode;
    private int[][] grid; //0 == blocked 1 == open
    private int size;
    private int openCnt;
    private boolean isPercolate;

    private static int[][] dir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};


    public Percolation(int N) throws IllegalArgumentException {
        if (N <= 0) {
            throw new IllegalArgumentException("N <= 0");
        }
        topUf = new WeightedQuickUnionUF(N * N + 2);
        bottomUf = new WeightedQuickUnionUF(N * N + 2);
        topNode = N * N;
        bottomNode = N * N + 1;
        size = N;
        openCnt = 0;
        isPercolate = false;
        grid = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = 0;
            }
        }

    }

    private int coorConvert(int row, int col) {
        return row * size + col;
    }

    private boolean isIndexOut(int row, int col) {
        if (row < 0 || col < 0) {
            return true;
        }
        if (row >= size || col >= size) {
            return true;
        }
        return false;
    }

    public void open(int row, int col) throws IndexOutOfBoundsException {
        if (isIndexOut(row, col)) {
            throw new IndexOutOfBoundsException("Index out");
        }
        if (isOpen(row, col)) {
            return;
        }
        openCnt++;
        grid[row][col] = 1;
        if (row == 0) {
            topUf.union(coorConvert(row, col), topNode);
        } else if (row == size - 1) {
            bottomUf.union(coorConvert(row, col), bottomNode);
        }
        for (int i = 0; i < dir.length; i++) {
            int r = row + dir[i][0];
            int c = col + dir[i][1];
            if (!isIndexOut(r, c) && isOpen(r, c)) {
                topUf.union(coorConvert(r, c), coorConvert(row, col));
                bottomUf.union(coorConvert(r, c), coorConvert(row, col));
            }
        }
        int index = coorConvert(row, col);
        if (topUf.connected(index, topNode) && bottomUf.connected(index, bottomNode)) {
            isPercolate = true;
        }
    }

    public boolean isOpen(int row, int col) {
        if (isIndexOut(row, col)) {
            throw new IndexOutOfBoundsException("Index out");
        }
        return grid[row][col] == 1;
    }

    public boolean isFull(int row, int col) {
        if (isIndexOut(row, col)) {
            throw new IndexOutOfBoundsException("Index out");
        }
        if (!isOpen(row, col)) {
            return false;
        }

        return topUf.connected(coorConvert(row, col), size * size);
    }

    public int numberOfOpenSites() {
        return openCnt;
    }

    public boolean percolates() {
        return isPercolate;
    }

    public static void main(String[] args) {
        String path = "inputFiles/";
        File file = new File("/home/mkx/courses/cs61/cs61b/sp18/hw2/inputFiles");
        File[] files = file.listFiles();
        String[] filenames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            filenames[i] = files[i].getName();
        }

        System.out.println(filenames.length);

        for (int i = 0; i < filenames.length; i++) {
            String filename = path + filenames[i];
            In in = new In(filename);
            int N = in.readInt();
            Percolation per = new Percolation(N);
            while (!in.isEmpty()) {
                int row = in.readInt();
                int col = in.readInt();
                per.open(row, col);
                if (per.percolates()) {
                    break;
                }
            }
            if (per.percolates()) {
                System.out.println(filename + " percolates");
            } else {
                System.out.println(filename + " does not percolates");
            }
        }

        System.out.println("ends");
    }
}
