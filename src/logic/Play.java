package logic;

import basicStructure.*;
import game.Board;
import game.GameState;
import game.Pyramid;

import java.util.Scanner;

public class Play {

    Move move = new Move();
    Scanner scanner = new Scanner(System.in);

    Board board = new Board(10, 10, (row, col) -> {
        // نقطة البداية
        if (row == 0 && col == 0) {
            return new Square(Color.GREEN, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);
        }
        // نقطة النهاية
        if (row == 9 && col == 9) {
            return new Square(Color.YELLOW, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
        }
        // بعض المربعات المقفولة لتجربة المفاتيح
        if ((row == 2 && col == 3) || (row == 5 && col == 5)) {
            return new Square(Color.RED, true, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.MEDIUM, Has.NOTHING);
        }
        // بعض المربعات المنهارة
        if ((row == 4 && col == 4) || (row == 6 && col == 6)) {
            return new Square(Color.BLACK, false, SquareState.COLLAPSED, SquareType.VOID, SquareStrength.WEAK, Has.NOTHING);
        }
        // مسار عادي
        return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });
    GameState gameState = new GameState(board, new Pyramid(new Location(0, 0), 0));


    Board board2 = new Board(10, 10, (row, col) -> {
        if (row == 5 && col == 5)
            return new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.STRONG, Has.NOTHING);
        if (row == 5 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
        if (row == 3 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);

        else
            return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });

    Board board3 = new Board(10, 10, (row, col) -> {
        if (row == 5 && col == 5)
            return new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.STRONG, Has.NOTHING);
        if (row == 5 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
        if (row == 3 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);

        else
            return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });

    Board board4 = new Board(10, 10, (row, col) -> {
        if (row == 5 && col == 5)
            return new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.STRONG, Has.NOTHING);
        if (row == 5 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
        if (row == 3 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);

        else
            return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });

    Board board5 = new Board(10, 10, (row, col) -> {
        if (row == 5 && col == 5)
            return new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.STRONG, Has.NOTHING);
        if (row == 5 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
        if (row == 3 && col == 2)
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);

        else
            return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });

    public void playRecursion(GameState gameState) {
        GameState nextState;
        Direction direction = Direction.UP;
        char wherGo = ' ';

        System.out.println("this is Game : \n" + gameState + "\n\n");
        System.out.println("""
                ==========================
                   Maze Collapse Game
                ==========================
                Use:
                  W - Up
                  S - Down
                  D - Right
                  A - Left
                  Q - Quit
                """);

        wherGo = scanner.next().charAt(0);
        switch (wherGo) {
            case 'w' -> direction = Direction.UP;
            case 's' -> direction = Direction.DOWN;
            case 'd' -> direction = Direction.RIGHT;
            case 'a' -> direction = Direction.LEFT;
            case 'q' -> {
                return;
            }
        }

        nextState = move.move(gameState, direction);

        if (nextState != null) {
            if (nextState.checkWining()) {
                System.err.println("this is final Game : \n" + nextState + "\n\n");
                System.err.println("WOOOOOOW You Win !!!");
                return;
            }
            playRecursion(nextState);
        } else {
            System.err.println("you cannot go !\n" +
                    "try other direction");
            playRecursion(gameState);
        }
    }

    public void playLoop(GameState gameState) {
        String goingTo = "";
        while (true) {
            System.out.println("this is Game : \n" + gameState + "\n\n");
            System.out.println("""
                    ==========================
                       Maze Collapse Game
                    ==========================
                    Use:
                      W - Up
                      S - Down
                      D - Right
                      A - Left
                      Q - Quit
                    """);


            char whereGo = scanner.next().toLowerCase().charAt(0);
            if (whereGo == 'q') {
                System.err.println("Game exited.");
                return;
            }
            Direction direction = switch (whereGo) {
                case 'w' -> Direction.UP;
                case 's' -> Direction.DOWN;
                case 'd' -> Direction.RIGHT;
                case 'a' -> Direction.LEFT;
                default -> null;
            };

            if (direction == null) {
                System.err.println("Invalid input, try again!");
                continue;
            }
            GameState nextState = move.move(gameState, direction);
            if (nextState != null) {
                gameState=nextState;
                goingTo += direction.toString() + " ";
                if (gameState.checkWining()) {
                    System.err.println("this is final Game : \n" + gameState + "\n\n");
                    System.err.println("WOOOOOOW You Win !!!");
                    System.out.println("The path is : " + goingTo);

                    break;
                }
            } else {
                System.err.println("You cannot go! Try another direction.");
            }
        }
    }

}
