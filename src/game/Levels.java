package game;
import basicStructure.*;
import logic.MakeState;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Levels {

    String boardPath1 = "src/levels/board1.txt";
    String coloresPath1 = "src/levels/color1.txt";


    String boardPath2 = "src/levels/board2.txt";
    String coloresPath2 = "src/levels/color2.txt";



    String boardPath3 = "src/levels/board3.txt";
    String coloresPath3 = "src/levels/color3.txt";


    String boardPath4 = "src/levels/board4.txt";
    String coloresPath4 = "src/levels/color4.txt";


    String boardPath5 = "src/levels/board5.txt";
    String coloresPath5 = "src/levels/color5.txt";


    GameState state1=null;
    GameState state2=null;
    GameState state3=null;
    GameState state4=null;
    GameState state5=null;

    public GameState getState5() {
        return state5;
    }

    public GameState getState4() {
        return state4;
    }

    public GameState getState3() {
        return state3;
    }

    public GameState getState2() {
        return state2;
    }

    public GameState getState1() {
        return state1;
    }

    public Levels() throws IOException {
         state1=new MakeState().importFromFiles(boardPath1,coloresPath1);
         state2=new MakeState().importFromFiles(boardPath2,coloresPath2);
         state3=new MakeState().importFromFiles(boardPath3,coloresPath3);
         state4=new MakeState().importFromFiles(boardPath4,coloresPath4);
         state5=new MakeState().importFromFiles(boardPath5,coloresPath5);    }

    private Board boardSolvableNonCentral = new Board(5, 5, (row, col) -> {
       Map<Location,Square> map=new HashMap<>();
       SpecialSquare specialSquare;

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

        if (row == 0 && col == 3) {
            map.put(new Location(2,2),new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.MEDIUM, Has.NOTHING));
            map.put(new Location(2,1),new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.MEDIUM, Has.NOTHING));
            Square square=  new Square(Color.RED, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
            specialSquare=new SpecialSquare(square,map);
            return specialSquare;
        }if ((row == 4 && col == 2)||(row == 4 && col == 3)) {
            return new Square(Color.BLACK, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.MEDIUM, Has.NOTHING);
        }
        if ((row==2&&col==2)||row==2&&col==1){
            return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.VOID, SquareStrength.WEAK, Has.NOTHING);

        }

        return new Square(Color.BLUE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    });
   private GameState gameStateSolvableNonCentral = new GameState(boardSolvableNonCentral, new Pyramid(new Location(1, 1), 0));

    public GameState getGameStateSolvableNonCentral() {
        return gameStateSolvableNonCentral;
    }
}
