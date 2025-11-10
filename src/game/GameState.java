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

        if (!isAllCollapsed()) {
            return false;
        }
        return true;
    }

    public GameState clone() {
        Board cloneBoard = board.clone();
        Pyramid clonePyramid = pyramid.clone();
        return new GameState(cloneBoard, clonePyramid);
    }

  /*  public GameState withBoard(Board newBoard) {
        return new GameState(newBoard, this.pyramid.clone());
    }

    public GameState withPyramid(Pyramid newPyramid) {
        return new GameState(this.board.clone(), newPyramid);
    }

    public GameState with(Board newBoard, Pyramid newPyramid) {
        return new GameState(newBoard, newPyramid);
    }*/

    public boolean isAllCollapsed() {
        Square[][] grid = board.getGrid();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[row].length; col++) {
                if (pyramid.getLocation().getRow() == row && pyramid.getLocation().getColumn() == col) {
                    continue;
                }
                if (grid[row][col].isCollapsed()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Square[][] grid = board.getGrid();
        int rows = grid.length;
        int columns = grid[0].length;

        StringBuilder sb = new StringBuilder();

        // السطور الثلاثة التي تمثل أي مربع (بشكل ثابت)
        String topBottomLine = "+---";
        String middleLineFormat = "| %c "; // %c سيتم استبدالها بمحتوى المربع

        // التكرار على صفوف الرقعة (الصف الأفقي)
        for (int r = 0; r < rows; r++) {

            // 1. بناء السطر العلوي (الجزء الثابت)
            for (int c = 0; c < columns; c++) {
                sb.append(topBottomLine);
            }
            sb.append("+\n"); // إنهاء السطر العلوي بـ '+' و سطر جديد

            // 2. بناء السطر الأوسط (محتوى المربع الديناميكي)
            for (int c = 0; c < columns; c++) {
                // الحصول على الرمز الديناميكي
                char content = getSquareContent(grid[r][c], r, c);

                // إضافة رمز الحدود والجزء الأوسط
                sb.append(String.format(middleLineFormat, content));
            }
            sb.append("|\n"); // إنهاء السطر الأوسط بـ '|' و سطر جديد
        }

        // 3. بناء السطر السفلي الأخير (لإنهاء آخر صف في الرقعة)
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
        if (square.getType() == SquareType.END) {
            return 'E';
        }

        if (square.getState() == SquareState.COLLAPSED) {
            return 'X';
        }
        if (square.isLocked()) {
            return 'L';
        }
        if (square.getHas() == Has.KEY) {
            return 'K'; // K للمفتاح
        }

        if (square.getStrength() == SquareStrength.STRONG)
            return '3';
        if (square.getStrength() == SquareStrength.MEDIUM)
            return '2';
        if (square.getStrength() == SquareStrength.WEAK)
            return '1';
        return ' ';
    }

    public NextState getNextState() {
        NextState nextState = new NextState();
        Move move = new Move();
        for (Direction direction : Direction.values()) {
            GameState resultantState = move.move(this, direction);
            if (resultantState != null) {
                nextState.addState(this, resultantState,direction);
            }
        }

        return nextState;
    }

    public List<Direction> directions() {
        GameState state=this;
        List<Direction> path = new LinkedList<>();
        GameState parentState = state.getParent();

        while (parentState != null) {
            Direction direction = findDirection(parentState, state);
            path.add(direction);
            state = parentState;
            parentState = state.getParent();
        }
        Collections.reverse(path);

        return path;
    }

    private static Direction findDirection(GameState parent, GameState child) {
        NextState successors = parent.getNextState();

        for (Map.Entry<Direction, GameState> entry : successors.getSuccessors().entrySet()) {
            if (entry.getValue().equals(child)) {
                return entry.getKey();
            }
        }

        return null;
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
