import algorithim.BFS;
import algorithim.DFSLoop;
import algorithim.DFSRecursion;
import algorithim.UCS;
import basicStructure.*;
import game.Board;
import game.GameState;
import game.Levels;
import logic.Direction;
import logic.MakeState;
import logic.Move;
import logic.Play;

import java.io.IOException;
import java.util.function.Function;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        Levels level1 = new Levels();
        Play play = new Play();

     //   play.playRecursion(level1.getGameStateSolvableNonCentral());
     //   new DFSLoop(level1.getGameStateSolvableNonCentral());
       String path = "src/seed1.txt";
       String boardPath = "src/board1.txt";
       String coloresPath = "src/colors1.txt";
        // new BFS((new MakeState().importFromFile(path)));
         new UCS().search(new MakeState().importFromFiles(boardPath,coloresPath));
        long endTime = System.currentTimeMillis();

        System.out.println("Time "+(endTime - startTime));
    //    new DFSLoop(level1.getGameStateSolvableNonCentral());
         // play.playLoop(new MakeState().importFromFile(path));
       //play.playLoop(level1.getGameStateSolvableNonCentral());


        //play.playLoop(new MakeState().seed());
    }
}