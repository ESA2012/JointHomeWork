package com.logistic.impl.generators;

/**
 * Created by SnakE on 06.11.2015.
 */

public class RouteMatrix {
    private int[][] matrix;
    private int size;



    public RouteMatrix (int nodes) {
        size = nodes;
        matrix = new int[size][size];
    }



    public int getSize() {
        return size;
    }



    public void setArray(int[][] matrix) {
        this.matrix = matrix;
    }



    public static RouteMatrix join(RouteMatrix matrix1, RouteMatrix matrix2) {
        int size = matrix2.getSize() > matrix1.getSize()? matrix1.getSize(): matrix2.getSize();
        RouteMatrix newMatrix = new RouteMatrix(size);
        for (int i = 0; i < size; i ++) {
            for (int j = 0; j < size; j++) {
                newMatrix.getArray()[i][j] = matrix1.getArray()[i][j] + matrix2.getArray()[i][j];
            }
        }
        return newMatrix;
    }



    public static RouteMatrix join (RouteMatrix...matrixes) {
        int size = matrixes[0].getSize();

        RouteMatrix res = new RouteMatrix(size);
        res.setArray(new int[size][size]);

        for (RouteMatrix matrixe : matrixes) {
            res = join(res, matrixe);
        }
        return res;
    }



    public int[][] getArray() {
        return matrix;
    }



    public String toString() {
        String res = "";
        for (int i = 0; i < size; i++) {
            res += i + " : ";
            for (int j = 0; j < size; j++) {
                res += String.format("[%d]", matrix[i][j]);
            }
            res+="\r\n";
        }
        return res;
    }
}
