package game;
import basicStructure.Color;
import basicStructure.Square;
import basicStructure.SquareState;
import basicStructure.SquareType;
import basicStructure.SquareStrength;
import basicStructure.Has;
import basicStructure.Location;
public class Levels {
   private Board boardSolvableNonCentral = new Board(5, 5, (row, col) -> {

        if (row == 1 && col == 1) {
            return new Square(Color.GREEN, false, SquareState.NOT_COLLAPSED, SquareType.START, SquareStrength.WEAK, Has.NOTHING);
        }

        if (row == 3 && col == 0) {
            return new Square(Color.YELLOW, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.KEY);
        }

        if (row == 3 && col == 4) {
            return new Square(Color.RED, true, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
        }

        if (row == 4 && col == 4) {
            return new Square(Color.GREEN, false, SquareState.NOT_COLLAPSED, SquareType.END, SquareStrength.WEAK, Has.NOTHING);
        }

        if (row == 2 && col == 2) {
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.VOID, SquareStrength.WEAK, Has.NOTHING);
        }if (row == 4 && col == 2) {
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.MEDIUM, Has.NOTHING);
        }

        return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });
   private GameState gameStateSolvableNonCentral = new GameState(boardSolvableNonCentral, new Pyramid(new Location(1, 1), 0));

    public GameState getGameStateSolvableNonCentral() {
        return gameStateSolvableNonCentral;
    }
}
