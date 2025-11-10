package game;

import basicStructure.Square;

import java.util.Arrays;
import java.util.function.BiFunction;

public class Board {
    private final Square[][] grid;
    private int columns;
    private int rows;


    public Board(int rows, int cols, BiFunction<Integer, Integer, Square> factory) {
        grid = new Square[rows][cols];
        int specialSquaresCount = 0;
        for (int row = 0; row < rows; row++)
            for (int col = 0; col < cols; col++) {
                grid[row][col] = factory.apply(row, col);
                if (grid[row][col].isEnd()) {
                    specialSquaresCount++;
                }
            }
        if (specialSquaresCount != 1) throw new IllegalArgumentException("Board must have only one end square");

        this.rows = grid.length;
        this.columns = grid[0].length;
    }

    public Board(Square[][] grid) {
        this.grid = grid;
        rows = grid.length;
        columns = grid[0].length;
    }

    public Square[][] getGrid() {
        return grid;
    }


    public Board clone() {
        Square[][] cloneGrid = new Square[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                Square square = grid[row][col];
                cloneGrid[row][col] = new Square(square.getColor(), square.isLocked(), square.getState(), square.getType(), square.getStrength(), square.getHas());
            }
        }
        return new Board(cloneGrid);
    }

    public Square getSquare(int row, int column) {
        if (row < 0 || row >= rows || column < 0 || column >= columns)
            throw new IndexOutOfBoundsException("Invalid square position: (" + row + ", " + column + ")");
        return grid[row][column];
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Board obj)) return false;
        return Arrays.deepEquals(this.grid, obj.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }


}
