package ui;

import algorithim.*;
import game.GameState;
import logic.MakeState;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.awt.event.ActionListener;

public class GameSolverFrame extends JFrame {

    // ❗ 1. الحقل الثابت و Setter الثابت تم وضعهما هنا
    private static GameSolverFrame instance;

    public static void setInstance(GameSolverFrame frame) {
        instance = frame;
    }

    // ❗ 2. دالة الواجهة الثابتة التي تستدعيها الخوارزميات
    public static void showUI(GameState endState) {
        if (instance != null) {
            instance.animateSolution(endState);
        }
    }

    // --------------------------------------------------
    // كود الواجهة والتحكم
    // --------------------------------------------------

    private final Map<String, GameState> availableLevels;
    private final JComboBox<String> levelSelector;
    private final JComboBox<String> algoSelector;
    private final Maze2DPanel mazePanel;
    private final JButton solveButton;
    private final JLabel statusLabel;

    private Timer animationTimer; // javax.swing.Timer

    private final List<String> levelKeys;
    private int currentLevelIndex = 0;

    public GameSolverFrame() {
        // ❗ تعيين الكائن الحالي كـ instance أولاً
        GameSolverFrame.setInstance(this);

        setTitle("Maze Collapse Solver (Unified Interface) 🎮");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 800);
        setLocationRelativeTo(null);

        // تحميل المراحل
        this.availableLevels = loadLevels();
        this.levelKeys = new ArrayList<>(availableLevels.keySet());

        // تهيئة المكونات
        this.mazePanel = new Maze2DPanel();
        this.levelSelector = new JComboBox<>(levelKeys.toArray(new String[0]));
        String[] algorithms = {"DFS Loop", "DFS Recursion", "BFS", "UCS", "A*"};
        this.algoSelector = new JComboBox<>(algorithms);
        this.solveButton = new JButton("Play ▶");
        this.statusLabel = new JLabel("Status: Ready. Select a level and algorithm.");

        // بناء الواجهة
        setLayout(new BorderLayout());
        add(createControlPanel(), BorderLayout.NORTH);
        add(mazePanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        // ضبط الأحداث
        setupListeners();

        // عرض الحالة الافتراضية
        updateDefaultState();
    }

    // --- بناء شريط التحكم العلوي ---
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));

        // التحكم في المستوى
        JPanel levelControl = new JPanel(new FlowLayout(FlowLayout.LEFT));
        levelControl.add(new JLabel("Level:"));
        JButton prevLevel = new JButton("◀");
        prevLevel.addActionListener(e -> changeLevel(false));
        JButton nextLevel = new JButton("▶");
        nextLevel.addActionListener(e -> changeLevel(true));

        levelControl.add(prevLevel);
        levelControl.add(levelSelector);
        levelControl.add(nextLevel);

        controlPanel.add(levelControl);
        controlPanel.add(new JLabel("Algorithm:"));
        controlPanel.add(algoSelector);
        controlPanel.add(solveButton);

        return controlPanel;
    }

    // --- الدوال المساعدة ---
    private Map<String, GameState> loadLevels() {
        MakeState makeState = new MakeState();
        try {
            Map<String, GameState> levels = new LinkedHashMap<>();
            levels.put("Level 1", makeState.importFromFiles("src/levels/board1.txt", "src/levels/color1.txt"));
            levels.put("Level 2", makeState.importFromFiles("src/levels/board2.txt", "src/levels/color2.txt"));
            levels.put("Level 3", makeState.importFromFiles("src/levels/board3.txt", "src/levels/color3.txt"));
            levels.put("Level 4", makeState.importFromFiles("src/levels/board4.txt", "src/levels/color4.txt"));
            levels.put("Level 5", makeState.importFromFiles("src/levels/board5.txt", "src/levels/color5.txt"));
            return levels;
        } catch (IOException e) {
            throw new RuntimeException("Could not load level files. Check paths: " + e.getMessage(), e);
        }
    }

    private void setupListeners() {
        solveButton.addActionListener(e -> startSolver());
        levelSelector.addActionListener(e -> updateDefaultState());
        algoSelector.addActionListener(e -> updateDefaultState());
    }

    private void changeLevel(boolean isNext) {
        int newIndex = isNext ? (currentLevelIndex + 1) % levelKeys.size() : (currentLevelIndex - 1 + levelKeys.size()) % levelKeys.size();
        levelSelector.setSelectedIndex(newIndex);
        currentLevelIndex = newIndex;
        updateDefaultState();
    }

    private void updateDefaultState() {
        String selectedKey = (String) levelSelector.getSelectedItem();
        if (selectedKey == null || !availableLevels.containsKey(selectedKey)) return;

        GameState initialState = availableLevels.get(selectedKey);
        mazePanel.setGameState(initialState);
        mazePanel.clearSearchOverlay();
        statusLabel.setText("Status: Ready to solve " + selectedKey + " using " + algoSelector.getSelectedItem() + ".");

        stopAnimation();
    }

    private void stopAnimation() {
        if (animationTimer != null) {
            animationTimer.stop();
            solveButton.setText("Play ▶");
            solveButton.setEnabled(true);
        }
    }

    private void startSolver() {
        if (animationTimer != null && animationTimer.isRunning()) {
            stopAnimation();
            statusLabel.setText("Status: Animation Stopped by user.");
            return;
        }

        String selectedLevelKey = (String) levelSelector.getSelectedItem();
        String selectedAlgorithm = (String) algoSelector.getSelectedItem();

        if (selectedLevelKey == null || selectedAlgorithm == null) return;

        solveButton.setEnabled(false);
        statusLabel.setText("Status: Solving using " + selectedAlgorithm + "... Please wait.");

        GameState initialGameState = availableLevels.get(selectedLevelKey);
        GameState startState = (GameState) initialGameState.clone();

        new Thread(() -> {
            try {
                executeAlgorithm(selectedAlgorithm, startState);
            } catch (Exception ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Status: Error during search. Check console.");
                    JOptionPane.showMessageDialog(this, "Solver Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                });
            } finally {
                if (animationTimer == null || !animationTimer.isRunning()) {
                    SwingUtilities.invokeLater(() -> solveButton.setEnabled(true));
                }
            }
        }).start();
    }

    private void executeAlgorithm(String algorithm, GameState startState) throws Exception {
        // جميع الخوارزميات تستدعي new Common().showUI(endState) عند إيجاد الحل
        switch (algorithm) {
            case "DFS Loop":
                new DFSLoop().DFSSearch(startState);
                break;
            case "DFS Recursion":
                new DFSRecursion().DFSSearch(startState);
                break;
            case "BFS":
                new BFS().BFSSearch(startState);
                break;
            case "UCS":
                new UCS().UCSSearch(startState);
                break;
            case "A*":
                new AStar().AStarSearch(startState);
                break;
        }
    }

    // --- دالة عرض المسار (يتم استدعاؤها من الدالة الثابتة showUI) ---
    public void animateSolution(GameState endState) {
        // استخدام دالة بناء المسار الموجودة في SearchFrame سابقًا (يجب نقلها أو إعادة كتابتها هنا)
        final List<GameState> solutionPath = buildPath(endState);
        if (solutionPath.isEmpty()) {
            SwingUtilities.invokeLater(() -> statusLabel.setText("Status: Solution found, but path is empty."));
            return;
        }

        SwingUtilities.invokeLater(() -> {
            stopAnimation();

            final int delayMs = 250;
            final int size = solutionPath.size();
            final int[] idx = {0};

            solveButton.setText("Stop ⏹");
            solveButton.setEnabled(true);
            statusLabel.setText("Status: Animating solution path (" + size + " steps)...");

            animationTimer = new Timer(delayMs, ev -> {
                if (idx[0] < size) {
                    GameState s = solutionPath.get(idx[0]);
                    mazePanel.setGameState(s);
                    idx[0]++;
                } else {
                    ((Timer) ev.getSource()).stop();
                    solveButton.setText("Play ▶");
                    statusLabel.setText("Status: Animation finished. Total steps: " + size);
                }
            });
            animationTimer.setInitialDelay(0);
            animationTimer.start();
        });
    }

    /**
     * تبني قائمة الحالات بالترتيب الصحيح (من البداية إلى النهاية).
     * (منقولة من SearchFrame/Common)
     */
    public static List<GameState> buildPath(GameState endState) {
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