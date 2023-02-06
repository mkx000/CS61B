package hw2;

import java.io.File;
import java.lang.Exception;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private WeightedQuickUnionUF uf;
    private int[][] grid; //0 == blocked 1 == open
    private int size;
    private int openCnt;
    private boolean isPercolate;

    private static int[][] dir = new int[][]{{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

    public Percolation(int N) throws IllegalArgumentException {
        if (N <= 0) {
            throw new IllegalArgumentException("N <= 0");
        }
        uf = new WeightedQuickUnionUF(N * N);
        size = N;
        openCnt = N * N;
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
        for (int i = 0; i < dir.length; i++) {
            int r = row + dir[i][0];
            int c = col + dir[i][1];
            if (!isIndexOut(r, c) && isOpen(r, c)) {
                uf.union(coorConvert(r, c), coorConvert(row, col));
            }
        }

        for (int i = 0; i < size; i++) {
            if (isFull(row, col) && uf.connected(coorConvert(row, col), coorConvert(size - 1, i))) {
                isPercolate = true;
                break;
            }
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
        for (int i = 0; i < size; i++) {
            if (uf.connected(coorConvert(row, col), coorConvert(0, i))) {
                return true;
            }
        }
        return false;
    }

    public int numberOfOpenSites() {
        return openCnt;
    }

    public boolean percolates() {
        return isPercolate;
    }

    /*public static void main(String[] args) {
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
    }*/
}
