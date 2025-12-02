package game;

import basicStructure.*;
import logic.Direction;
import logic.Move;

import java.util.*;

public class GameState {

    private final Board board;
    private final Pyramid pyramid;
    private GameState parent;

    public GameState(Board board, Pyramid pyramid) {
        this.pyramid = pyramid;
        this.board = board;
    }

    public Board getBoard() {
        return board;
    }

    public Pyramid getPyramid() {
        return pyramid;
    }


    public GameState getParent() {
        return parent;
    }

    public void setParent(GameState parent) {
        this.parent = parent;
    }

    public boolean checkWining() {
        Square[][] squares = board.getGrid();


        // Perfect !!!
        return squares[pyramid.getLocation().getRow()][pyramid.getLocation().getColumn()].isEnd();

        /*if (!isAllCollapsed()) {
            return false;
        }
        return true;*/
    }

    public GameState clone() {
        Board cloneBoard = board.clone();
        Pyramid clonePyramid = pyramid.clone();
        return new GameState(cloneBoard, clonePyramid);
    }

    private boolean isAllCollapsed() {
        Square[][] grid = board.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
                if (pyramid.getLocation().getRow() == row && pyramid.getLocation().getColumn() == col) {
                    continue;
                }
                if (!grid[row][col].isCollapsed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== BOARD (TILES) ===\n");
        sb.append(renderTilesBoard());
        sb.append("\n");

        sb.append("=== BOARD COLORS ===\n");
        sb.append(renderColorBoard());

        return sb.toString();
    }

    private String renderTilesBoard() {
        Square[][] grid = board.getGrid();
        int rows = grid.length;
        int columns = grid[0].length;

        StringBuilder sb = new StringBuilder();

        String topBottomLine = "+---";
        String middleLineFormat = "| %c ";
        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < columns; c++) {
                sb.append(topBottomLine);
            }
            sb.append("+\n");

            for (int c = 0; c < columns; c++) {
                char content = getSquareContent(grid[r][c], r, c);
                sb.append(String.format(middleLineFormat, content));
            }
            sb.append("|\n");
        }

        for (int c = 0; c < columns; c++) {
            sb.append(topBottomLine);
        }
        sb.append("+\n");

        return sb.toString();
    }

    private String renderColorBoard() {
        Square[][] grid = board.getGrid();
        int rows = grid.length;
        int columns = grid[0].length;

        StringBuilder sb = new StringBuilder();

        String topBottomLine = "+---";
        for (int r = 0; r < rows; r++) {

            for (int c = 0; c < columns; c++) {
                sb.append(topBottomLine);
            }
            sb.append("+\n");

            for (int c = 0; c < columns; c++) {
                char colorChar = getColorChar(grid[r][c].getColor());
                sb.append("| ").append(colorChar).append(" ");
            }
            sb.append("|\n");
        }

        for (int c = 0; c < columns; c++) {
            sb.append(topBottomLine);
        }
        sb.append("+\n");

        return sb.toString();
    }



    private char getSquareContent(Square square, int row, int col) {

        Location pyramidLocation = pyramid.getLocation();
        if (pyramidLocation.getRow() == row && pyramidLocation.getColumn() == col) {
            return 'P';
        }
        if (square.getType() == SquareType.VOID) {
            return '#';
        }
        if (square.getState() == SquareState.COLLAPSED) {
            return 'X';
        }
        if (square instanceof SpecialSquare)
            return 'S';

        if (square.getType() == SquareType.END) {
            return 'E';
        }

        if (square.isLocked()) {
            return 'L';
        }
        if (square.getHas() == Has.KEY) {
            return 'K';
        }

        if (square.getStrength() == SquareStrength.STRONG)
            return '3';
        if (square.getStrength() == SquareStrength.MEDIUM)
            return '2';
        if (square.getStrength() == SquareStrength.WEAK)
            return '1';
        return ' ';
    }

    private char getColorChar(Color color) {
        return switch (color) {
            case RED -> 'R';
            case BLUE -> 'L';
            case GREEN -> 'G';
            case YELLOW -> 'Y';
            case WHITE -> 'W';
            case BLACK -> 'B';
            case PINK -> 'P';
            case PURPLE -> 'U';
        };
    }


    public NextStates getNextStates(boolean requireCollapseBeforeEnd) {
        NextStates nextState = new NextStates();
        Move move = new Move(requireCollapseBeforeEnd);
        for (Direction direction : Direction.values()) {
            GameState result = move.move(this, direction);
            if (result != null) {
                nextState.addState(this, result, direction);
            }
        }

        return nextState;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof GameState obj)) return false;
        return pyramid.equals(obj.pyramid) &&
                board.equals(obj.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pyramid, board);
    }

}
