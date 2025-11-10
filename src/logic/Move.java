package logic;

import basicStructure.*;
import game.Board;
import game.GameState;
import game.Pyramid;

public class Move {


    public Move() {
    }


    public boolean canMove(GameState gameState, Direction direction) {
        Board board = gameState.getBoard();
        Pyramid pyramid = gameState.getPyramid();
        Square[][] grid = board.getGrid();
        Location locationPyramid = gameState.getPyramid().getLocation().clone();
        int row = locationPyramid.getRow();
        int column = locationPyramid.getColumn();
        int newRow = row;
        int newCol = column;


        switch (direction) {
            case UP -> newRow--;
            case DOWN -> newRow++;
            case LEFT -> newCol--;
            case RIGHT -> newCol++;
        }
        if (newRow < 0 || newRow >= grid.length || newCol < 0 || newCol >= grid[0].length) {
            return false;
        }
        Square square = grid[newRow][newCol];
        if (square.getState() == SquareState.COLLAPSED) {
            return false;
        }
        if (square.isLocked() && !pyramid.hasKey()) {
            return false;
        }
        if (square.isEnd() && !canMoveHelper(board, pyramid)) {
            return false;
        }

/*
        switch (direction) {
            case UP -> {
                row--;
                if (row < 0) {
                    System.out.println("خارج الرقعه");
                    return false;
                }
                Square square = grid[row][column];
                if (square.getState() == SquareState.COLLAPSED || square.getType() == SquareType.VOID) {
                    System.out.println("المربع منهار");
                    return false;
                }
                if (square.isLocked() && !pyramid.hasKey()) {
                    System.out.println("المربع مقفول ولا تملك مفتاح");
                    return false;
                }
                return true;
            }
            case DOWN -> {
                row++;
                if (row >= grid.length) {
                    System.out.println("خارج الرقعه");
                    return false;
                }
                Square square = grid[row][column];
                if (square.getState() == SquareState.COLLAPSED || square.getType() == SquareType.VOID) {
                    System.out.println("المربع منهار");
                    return false;
                }
                if (square.isLocked() && !pyramid.hasKey()) {
                    System.out.println("المربع مقفول ولا تملك مفتاح");
                    return false;
                }

                return true;
            }
            case LEFT -> {
                column--;
                if (column < 0) {
                    System.out.println("خارج الرقعه");
                    return false;
                }
                Square square = grid[row][column];
                if (square.getState() == SquareState.COLLAPSED || square.getType() == SquareType.VOID) {
                    System.out.println("المربع منهار");
                    return false;
                }
                if (square.isLocked() && !pyramid.hasKey()) {
                    System.out.println("المربع مقفول ولا تملك مفتاح");
                    return false;
                }

                return true;
            }
            case RIGHT -> {
                column++;
                if (column >= grid[0].length) {
                    System.out.println("خارج الرقعه");
                    return false;
                }
                Square square = grid[row][column];
                if (square.getState() == SquareState.COLLAPSED || square.getType() == SquareType.VOID) {
                    System.out.println("المربع منهار");
                    return false;
                }
                if (square.isLocked() && !pyramid.hasKey()) {
                    System.out.println("المربع مقفول ولا تملك مفتاح");
                    return false;
                }

                return true;
            }
        }
*/

        return true;
    }

    private boolean canMoveHelper(Board board, Pyramid pyramid) {
        Square[][] grid = board.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (pyramid.getLocation().getRow() == row && pyramid.getLocation().getColumn() == col) {
                    continue;
                }
                Square square = grid[row][col];
                if (square.isEnd()) continue;
                if (!square.isCollapsed()) {
                    return false;
                }
            }
        }
        return true;
    }

    public GameState move(GameState gameState, Direction direction) {

        if (gameState == null) return null;
        GameState nextState = gameState.clone();
        Pyramid pyramid = nextState.getPyramid();
        Location pyramidLocation = pyramid.getLocation();
        Board board = nextState.getBoard();
        int oldRow = pyramidLocation.getRow();
        int oldCol = pyramidLocation.getColumn();
        int newRow = oldRow;
        int newCol = oldCol;

        if (canMove(nextState, direction)) {
            switch (direction) {
                case UP -> newRow--;
                case DOWN -> newRow++;
                case LEFT -> newCol--;
                case RIGHT -> newCol++;
            }
            Square previousSquare = board.getSquare(oldRow, oldCol);
            handleSquareAfterLeaving(previousSquare);
            Square nextSquare = board.getSquare(newRow, newCol);
            handleSquareAfterArrival(nextSquare, pyramid);
            pyramid.setLocation(new Location(newRow, newCol));

            return nextState;

        }
        return null;
    }

    private void handleSquareAfterLeaving(Square square) {
        if (square.getStrength() != SquareStrength.WEAK) square.setStrength(square.getStrength().weaken());
        else square.setState(SquareState.COLLAPSED);
    }

    private void handleSquareAfterArrival(Square square, Pyramid pyramid) {
        if (square.getHas() == Has.KEY) {
            square.setHas(Has.NOTHING);
            pyramid.setNumberOfKey(pyramid.getNumberOfKey() + 1);
        }
        if (square.isLocked()) {
            square.setLocked(false);
            pyramid.setNumberOfKey(pyramid.getNumberOfKey() - 1);
        }

    }
}


