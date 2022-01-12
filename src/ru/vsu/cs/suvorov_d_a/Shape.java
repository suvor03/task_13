package ru.vsu.cs.suvorov_d_a;

import java.awt.Color;
import java.awt.Graphics;

public class Shape {

    private final Color color;
    private int x, y;
    private long time, lastTime;
    private final int normal = 600;
    private int delay;
    private int[][] coordinates;
    private int deltaX;
    private final Board board;
    private boolean collision = false;
    private int timePassedFromCollision = -1;

    public Shape(int[][] coordinates, Board board, Color color) {
        this.coordinates = coordinates;
        this.board = board;
        this.color = color;
        deltaX = 0;
        x = 4;
        y = 0;
        delay = normal;
        time = 0;
        lastTime = System.currentTimeMillis();
        int[][] reference = new int[coordinates.length][coordinates[0].length];

        System.arraycopy(coordinates, 0, reference, 0, coordinates.length);
    }

    long deltaTime;

    public void update() {
        boolean moveX = true;
        deltaTime = System.currentTimeMillis() - lastTime;
        time += deltaTime;
        lastTime = System.currentTimeMillis();

        if (collision && timePassedFromCollision > 500) {
            for (int row = 0; row < coordinates.length; row++) {
                for (int col = 0; col < coordinates[0].length; col++) {
                    if (coordinates[row][col] != 0) {
                        board.getBoard()[y + row][x + col] = color;
                    }
                }
            }
            checkLine();
            board.addScore();
            board.setCurrentShape();
            timePassedFromCollision = -1;
        }
        if (!(x + deltaX + coordinates[0].length > 10) && !(x + deltaX < 0)) {
            for (int row = 0; row < coordinates.length; row++) {
                for (int col = 0; col < coordinates[row].length; col++) {
                    if (coordinates[row][col] != 0) {
                        if (board.getBoard()[y + row][x + deltaX + col] != null) {
                            moveX = false;
                        }
                    }
                }
            }
            if (moveX) {
                x += deltaX;
            }
        }
        if (timePassedFromCollision == -1) {
            if (!(y + 1 + coordinates.length > 20)) {
                for (int row = 0; row < coordinates.length; row++) {
                    for (int col = 0; col < coordinates[row].length; col++) {
                        if (coordinates[row][col] != 0) {
                            if (board.getBoard()[y + 1 + row][x + col] != null) {
                                collision();
                            }
                        }
                    }
                }
                if (time > delay) {
                    y++;
                    time = 0;
                }
            } else {
                collision();
            }
        } else {
            timePassedFromCollision += deltaTime;
        }
        deltaX = 0;
    }

    private void collision() {
        collision = true;
        timePassedFromCollision = 0;
    }

    public void render(Graphics g) {
        g.setColor(color);
        for (int row = 0; row < coordinates.length; row++) {
            for (int col = 0; col < coordinates[0].length; col++) {
                if (coordinates[row][col] != 0) {
                    g.fillRect(col * 30 + x * 30, row * 30 + y * 30, Board.blockSize, Board.blockSize);
                }
            }
        }
    }

    private void checkLine() {
        int size = board.getBoard().length - 1;

        for (int i = board.getBoard().length - 1; i > 0; i--) {
            int count = 0;
            for (int j = 0; j < board.getBoard()[0].length; j++) {
                if (board.getBoard()[i][j] != null) {
                    count++;
                }

                board.getBoard()[size][j] = board.getBoard()[i][j];
            }
            if (count < board.getBoard()[0].length) {
                size--;
            }
        }
    }

    public void rotateShape() {
        int[][] rotatedShape = transposeMatrix(coordinates);
        reverseRows(rotatedShape);

        if ((x + rotatedShape[0].length > 10) || (y + rotatedShape.length > 20)) {
            return;
        }

        for (int row = 0; row < rotatedShape.length; row++) {
            for (int col = 0; col < rotatedShape[row].length; col++) {
                if (rotatedShape[row][col] != 0) {
                    if (board.getBoard()[y + row][x + col] != null) {
                        return;
                    }
                }
            }
        }
        coordinates = rotatedShape;
    }

    private int[][] transposeMatrix(int[][] matrix) {
        int[][] temp = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                temp[j][i] = matrix[i][j];
            }
        }
        return temp;
    }

    private void reverseRows(int[][] matrix) {
        int middle = matrix.length / 2;

        for (int i = 0; i < middle; i++) {
            int[] temp = matrix[i];
            matrix[i] = matrix[matrix.length - i - 1];
            matrix[matrix.length - i - 1] = temp;
        }
    }

    public Color getColor() {
        return color;
    }

    public void setDeltaX(int deltaX) {
        this.deltaX = deltaX;
    }

    public void speedUp() {
        delay = 50;
    }

    public void speedDown() {
        delay = normal;
    }

    public int[][] getCoordinates() {
        return coordinates;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}