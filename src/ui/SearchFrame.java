package ui;

import game.GameState;
import logic.Direction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Frame مخصص لعرض تتبّع خوارزمية البحث.
 * يأخذ قائمة من GameState ويعرضها عبر Maze2DPanel خطوة بخطوة.
 *
 * ملاحظة: يحتوي على addStateAndRefresh(GameState) لاستخدام العرض التدريجي.
 */
public class SearchFrame extends JFrame {

    private Maze2DPanel panel;

    private final JButton btnPrev = new JButton("⟸ Prev");
    private final JButton btnNext = new JButton("Next ⟹");
    private final JButton btnPlay = new JButton("Play ▶");
    private final JLabel lblIndex = new JLabel("0/0");

    // states يمكن أن يكون null أو قائمة قابلة للتغيير
    private java.util.List<GameState> states;
    private int idx = 0;

    private Timer playTimer;

    public SearchFrame(java.util.List<GameState> states, Maze2DPanel panel) {
        this.states = states;
        this.panel = panel;

        setTitle("Search Visualization");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 900);
        setLocationRelativeTo(null);

        initUI();
        loadInitialState();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        controls.add(btnPrev);
        controls.add(btnNext);
        controls.add(btnPlay);
        controls.add(lblIndex);
        add(controls, BorderLayout.SOUTH);

        btnPrev.addActionListener(this::onPrev);
        btnNext.addActionListener(this::onNext);
        btnPlay.addActionListener(this::onPlay);
    }

    private void loadInitialState() {
        if (states == null || states.isEmpty()) {
            panel.setGameState(null);
            lblIndex.setText("0/0");
            return;
        }
        idx = 0;
        updateView();
    }

    private void updateView() {
        if (states == null || states.isEmpty()) {
            panel.setGameState(null);
            lblIndex.setText("0/0");
            return;
        }
        if (idx < 0) idx = 0;
        if (idx >= states.size()) idx = states.size() - 1;

        GameState st = states.get(idx);
        panel.setGameState(st);
        // player direction preserved in GameState or set externally; default to UP
        panel.setPlayerDir(Direction.UP);

        lblIndex.setText((idx + 1) + "/" + states.size());
        panel.repaint();
    }

    private void onPrev(ActionEvent e) {
        if (states == null || states.isEmpty()) return;
        idx = Math.max(0, idx - 1);
        updateView();
    }

    private void onNext(ActionEvent e) {
        if (states == null || states.isEmpty()) return;
        idx = Math.min(states.size() - 1, idx + 1);
        updateView();
    }

    private void onPlay(ActionEvent e) {
        if (states == null || states.isEmpty()) return;

        if (playTimer != null && playTimer.isRunning()) {
            playTimer.stop();
            playTimer = null;
            btnPlay.setText("Play ▶");
            return;
        }

        btnPlay.setText("Stop ⏹");
        playTimer = new Timer(300, ev -> {
            if (idx < states.size() - 1) {
                idx++;
                updateView();
            } else {
                ((Timer) ev.getSource()).stop();
                btnPlay.setText("Play ▶");
            }
        });
        playTimer.setInitialDelay(0);
        playTimer.start();
    }

    /**
     * إضافة حالة جديدة إلى نهاية القائمة ثم عرضها فوراً.
     * مفيد عندما تريد بث الحالات خطوة بخطوة أثناء عمل الخوارزمية.
     */
    public void addStateAndRefresh(GameState s) {
        if (s == null) return;
        if (this.states == null) this.states = new java.util.ArrayList<>();
        this.states.add(s);
        this.idx = this.states.size() - 1;
        // عرض الحالة المضافة
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * استبدال حالات العرض بكامل القائمة الجديدة
     */
    public void setStates(List<GameState> newStates) {
        this.states = newStates;
        this.idx = 0;
        SwingUtilities.invokeLater(this::updateView);
    }

    /**
     * استبدال الـ panel بسهولة
     */
    public void replacePanel(Maze2DPanel newPanel) {
        SwingUtilities.invokeLater(() -> {
            getContentPane().remove(panel);
            this.panel = newPanel;
            getContentPane().add(panel, BorderLayout.CENTER);
            revalidate();
            repaint();
        });
    }

    // ----------------------------
    // الدوال المساعدة المضمّنة داخل SearchFrame
    // ----------------------------

    /**
     * يعرض المسار الذي تم إيجاده تدريجيًا باستخدام javax.swing.Timer.
     * هذه الدالة ثابتة (static) لتسهيل استخدامها كما في الكود الأصلي.
     * @param endState حالة النهاية (منها يتم بناء المسار إلى البداية).
     */
    public static void showUI(GameState endState) {
        // 1. بناء المسار الكامل (مرتب من البداية إلى النهاية)
        final List<GameState> solutionPath = buildPath(endState);
        if (solutionPath.isEmpty()) return;

        // 2. تشغيل إنشاء الإطار وعرضه على الـ EDT
        SwingUtilities.invokeLater(() -> {
            Maze2DPanel panel = new Maze2DPanel();

            // إنشاء الإطار بقائمة فارغة لضمان أننا نضيف الحالات تدريجيًا
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
