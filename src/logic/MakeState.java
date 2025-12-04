package logic;

import basicStructure.*;
import game.Board;
import game.GameState;
import game.Pyramid;

import java.io.*;

public class MakeState {

    GameState gameState;

    public GameState importFromFile(String filePath) throws IOException {
         gameState= seed(filePath);
         return gameState;
    }

    public GameState importFromFiles(String boardFile, String colorsFile) throws IOException {
        GameState state = seed(boardFile);
        applyColors(state, colorsFile);
        return state;
    }



    public GameState seed(String filePath) throws IOException {
        Square[][] squares = null;
        Pyramid pyramid = null;
        try (BufferedReader readerHelp = new BufferedReader(new FileReader(filePath))) {
            String line;

            line = readerHelp.readLine();
            String[] dimensions = line.split(" ");
            int rows = 1;
            int cols = dimensions.length;

            while ((readerHelp.readLine()) != null) {
                rows++;
            }
            String[][] help = new String[rows][cols];

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            int row = 0;
            String lineHelper;
            while ((lineHelper = reader.readLine()) != null) {
                String[] help2 = lineHelper.split(" ");
                for (int i = 0; i < help2.length; i++) {
                    help[row][i] = help2[i];
                }
                row++;
            }
            squares = new Square[rows][cols];
            Location location = null;
            for (int i = 0; i < help.length; i++) {
                for (int j = 0; j < help[0].length; j++) {
                    System.out.print(" " + help[i][j] + " ");
                    if (help[i][j].charAt(0) == 'S' || help[i][j].charAt(0) == 'P') {
                        location = new Location(i, j);
                    }
                    squares[i][j] = this.getSquareByRegex(help[i][j]);
                }
                System.out.println();
            }
            pyramid = new Pyramid(location, 0);

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new GameState(new Board(squares), pyramid);

    }

    private Square getSquareByRegex(String c) {
        Square square;
        char option = c.charAt(0);
        switch (option) {
            case 'P' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.MEDIUM, Has.NOTHING);
            }
            case 'E' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
            }
            case '#' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.VOID, SquareStrength.WEAK, Has.NOTHING);
            }
            case 'K' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.KEY);
            }
            case 'L' -> {
                return new Square(Color.WHITE, true, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
            }
            case '1' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
            }
            case '2' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.MEDIUM, Has.NOTHING);
            }
            case '3' -> {
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.STRONG, Has.NOTHING);
            }
        }
        return null;
    }

    private void applyColors(GameState state, String colorsFilePath) throws IOException {
        Board board = state.getBoard();
        Square[][] squares = board.getGrid();

        try (BufferedReader reader = new BufferedReader(new FileReader(colorsFilePath))) {
            int row = 0;
            String line;

            while ((line = reader.readLine()) != null && row < squares.length) {
                String[] colorCodes = line.split(" ");

                for (int col = 0; col < colorCodes.length && col < squares[0].length; col++) {
                    Color color = parseColor(colorCodes[col]);
                    if (color != null && squares[row][col] != null) {
                        squares[row][col] = updateSquareColor(squares[row][col], color);
                    }
                }
                row++;
            }
        }
    }

    private Color parseColor(String colorCode) {
        if (colorCode == null || colorCode.isEmpty()) {
            return null;
        }

        return switch (colorCode.toUpperCase()) {
            case "R", "RED" -> Color.RED;
            case "B", "BLUE" -> Color.BLUE;
            case "W", "WHITE" -> Color.WHITE;
            case "D", "DARK" -> Color.DARK;
            case "BL", "BLACK" -> Color.BLACK;
            case "G", "GREEN" -> Color.GREEN;
            case "Y", "YELLOW" -> Color.YELLOW;
            case "P", "PINK" -> Color.PINK;
            case "#", "####" -> Color.WHITE;
            default -> null;
        };
    }

    private Square updateSquareColor(Square original, Color newColor) {
        if (original == null) return null;

        return new Square(
                newColor,
                original.isLocked(),
                original.getState(),
                original.getType(),
                original.getStrength(),
                original.getHas()
        );
    }


}
