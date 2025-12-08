import algorithim.*;
import game.GameState;
import game.Levels;
import logic.MakeState;
import logic.Play;
import ui.GameSolverFrame;
import javax.swing.*;
import java.io.IOException;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        //---------------------------------------


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

        Levels levels=new Levels();
        GameState stat1=levels.getState1();
        GameState stat2=levels.getState2();
        GameState stat3=levels.getState3();
        GameState stat4=levels.getState4();
        GameState stat5=levels.getState5();


        Play play=new Play();
        play.playLoop(stat1);

        long startTime = System.currentTimeMillis();

        System.err.println(new DFSLoop().DFSSearch(stat5));
        System.err.println(new DFSRecursion().DFSSearch(stat1));
        System.err.println(new BFS().BFSSearch(stat1));
        System.err.println(new UCS().UCSSearch(stat1));

        long endTime = System.currentTimeMillis();
        System.out.println("Time "+(endTime - startTime));




          SwingUtilities.invokeLater(() -> {
            try {
                GameSolverFrame controlFrame = new GameSolverFrame();
                controlFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error initializing application: " + e.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }


}