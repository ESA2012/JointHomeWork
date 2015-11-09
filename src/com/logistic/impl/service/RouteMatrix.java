package com.logistic.impl.service;

/**
 * Created by SnakE on 06.11.2015.
 */

public class RouteMatrix {
    private boolean[][] matrix;
    private int size;

    public RouteMatrix (int nodes) {
        size = nodes;
        matrix = new boolean[size][size];
    }

    public int getSize() {
        return size;
    }

    public void setMatrix (boolean[][] matrix) {
        this.matrix = matrix;
    }

    public boolean isConnected(int indexA, int indexB) {
        return matrix[indexA][indexB];
    }

    public boolean[][] getMatrix() {
        return matrix;
    }

    public String toString() {
        String res = "";
        for (int i = 0; i < size; i++) {
            res += i + " : ";
            for (int j = 0; j < size; j++) {
                res += String.format("[%s]", matrix[i][j]?"+":" ");
            }
            res+="\r\n";
        }
        return res;
    }
}
