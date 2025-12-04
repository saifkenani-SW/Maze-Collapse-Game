import algorithim.*;
import basicStructure.*;
import game.Board;
import game.GameState;
import game.Levels;
import logic.Direction;
import logic.MakeState;
import logic.Move;
import logic.Play;
import ui.GameSolverFrame;
import ui.Maze2DPanel;
import ui.SearchFrame;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        long startTime = System.currentTimeMillis();
        Levels level1 = new Levels();
        Play play = new Play();

     //   play.playRecursion(level1.getGameStateSolvableNonCentral());
     //   new DFSLoop(level1.getGameStateSolvableNonCentral());
       String path = "src/seed1.txt";
       String boardPath = "src/board1.txt";
       String coloresPath = "src/colors1.txt";
        // new BFS((new MakeState().importFromFile(path)));
       //  new UCS().search(new MakeState().importFromFiles(boardPath,coloresPath));
        long endTime = System.currentTimeMillis();
        System.out.println("Time "+(endTime - startTime));
        System.out.println("\n\n\n _______________________ \n\n\n");
        startTime = System.currentTimeMillis();
       //  new AStar().search(new MakeState().importFromFiles(boardPath,coloresPath));
        endTime = System.currentTimeMillis();

        System.out.println("Time "+(endTime - startTime));

        //    new DFSLoop(level1.getGameStateSolvableNonCentral());
         // play.playLoop(new MakeState().importFromFile(path));
       //play.playLoop(level1.getGameStateSolvableNonCentral());


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
     /*   SwingUtilities.invokeLater(() -> {
            try {
                new MazeGUI3D(new MakeState().importFromFiles(boardPath5,coloresPath5));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });*/



//     new UCS().UCSSearch(new MakeState().importFromFiles(boardPath1,coloresPath1));
//
//       new UCS().UCSSearch(new MakeState().importFromFiles(boardPath2,coloresPath2));
//
//        new UCS().UCSSearch(new MakeState().importFromFiles(boardPath3,coloresPath3));

//        new UCS().UCSSearch(new MakeState().importFromFiles(boardPath4,coloresPath4));
//
        GameState gameState1=new MakeState().importFromFiles(boardPath1,coloresPath1);
        GameState gameState2=new MakeState().importFromFiles(boardPath2,coloresPath2);
        GameState gameState3=new MakeState().importFromFiles(boardPath3,coloresPath3);
        GameState gameState4=new MakeState().importFromFiles(boardPath4,coloresPath4);
        GameState gameState5=new MakeState().importFromFiles(boardPath5,coloresPath5);
        /*System.err.println(new DFSLoop().DFSSearch(gameState1));
        System.err.println(new DFSRecursion().DFSSearch(gameState1));
        System.err.println(new BFS().BFSSearch(gameState1));
        System.err.println(new UCS().UCSSearch(gameState1));
        System.err.println(new AStar().AStarSearch(gameState1));*/
        SwingUtilities.invokeLater(() -> {
            try {
                GameSolverFrame controlFrame = new GameSolverFrame();
                controlFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error initializing application: " + e.getMessage(), "Fatal Error", JOptionPane.ERROR_MESSAGE);
            }
        });







        //play.playLoop(new MakeState().seed());
    }



    /**
     * يعرض المسار الذي تم إيجاده تدريجيًا باستخدام javax.swing.Timer.
     * هذه هي الدالة المصححة التي تستخدم مبدأ Event Dispatch Thread (EDT) بشكل صحيح.
     * @param endState حالة النهاية (منها يتم بناء المسار إلى البداية).
     */
    public void showUI(GameState endState) {
        // 1. بناء المسار الكامل (مرتب من البداية إلى النهاية)
        final List<GameState> solutionPath = buildPath(endState);
        if (solutionPath.isEmpty()) return;

        // 2. تشغيل إنشاء الإطار وعرضه على الـ EDT
        SwingUtilities.invokeLater(() -> {
            Maze2DPanel panel = new Maze2DPanel();

            // إنشاء الإطار بقائمة فارغة لضمان أننا نضيف الحالات تدريجياً
            SearchFrame frame = new SearchFrame(new ArrayList<>(), panel);
            frame.setVisible(true);

            final int delayMs = 500;
            final int size = solutionPath.size();
            final int[] idx = {0}; // مؤشر الحالة الحالية

            // استخدام Timer لإضافة الحالات تدريجيًا (يعمل على الـ EDT)
            Timer t = new Timer(delayMs, ev -> {
                if (idx[0] < size) {
                    GameState s = solutionPath.get(idx[0]);

                    // addStateAndRefresh آمنة وتضيف الحالة ثم تُحدّث العرض
                    frame.addStateAndRefresh(s);
                    idx[0]++;
                } else {
                    ((Timer) ev.getSource()).stop(); // إيقاف Timer عند الانتهاء
                }
            });
            t.setInitialDelay(0);
            t.start();
        });
    }

    /**
     * تبني قائمة الحالات بالترتيب الصحيح (من البداية إلى النهاية).
     */
    private List<GameState> buildPath(GameState endState) {
        List<GameState> path = new ArrayList<>();
        GameState cur = endState;
        while (cur != null) {
            path.add(cur);
            cur = cur.getParent();
        }
        Collections.reverse(path);
        return path;
    }

}