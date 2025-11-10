import algorithim.DFS;
import basicStructure.*;
import game.Board;
import game.GameState;
import game.Levels;
import logic.Direction;
import logic.Move;
import logic.Play;

import java.util.function.Function;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Levels level1 = new Levels();
        Play play = new Play();
        //play.playRecursion(level1.getGameStateSolvableNonCentral());
        new DFS(level1.getGameStateSolvableNonCentral());
    }
}