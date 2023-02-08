package hw2;

import java.io.*;

public class ListFiles {
    public static void main(String[] args) {
        File file = new File("/home/mkx/courses/cs61/cs61b/sp18/hw2/inputFiles");
        File[] files = file.listFiles();
        String[] fileNames = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            fileNames[i] = new String(files[i].getName());
            System.out.println(fileNames[i]);
        }
    }
}
