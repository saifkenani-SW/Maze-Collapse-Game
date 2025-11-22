package logic;

import basicStructure.*;
import game.Board;
import game.GameState;
import game.Pyramid;

import java.io.*;

public class MakeState {

    public GameState importFromFile(String filePath) throws IOException {
        return seed(filePath);
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
                return new Square(Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);
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
}
