package ui;

import game.GameState;
import game.Levels;
import game.Pyramid;
import logic.Direction;
import logic.Move;

import javax.swing.*;
import java.awt.*;

public class MazeGUI3D extends JFrame {

    private GameState gameState;
    private final Move move = new Move();
    private final Levels levels = new Levels();
    private Maze3DPanel drawPanel;
    private Direction playerDir = Direction.UP;

    public MazeGUI3D(GameState state){
        this.gameState = state;

        setTitle("Maze Collapse – 3D View");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 900);
        setLocationRelativeTo(null);

        drawPanel = new Maze3DPanel();
        drawPanel.setFocusable(true);
        add(drawPanel);

        setupControls();
        setVisible(true);
        drawPanel.requestFocusInWindow();
    }

    private void setupControls() {
        drawPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent e) {
                switch (e.getKeyCode()) {
                    case java.awt.event.KeyEvent.VK_UP, java.awt.event.KeyEvent.VK_W -> applyMove(Direction.UP);
                    case java.awt.event.KeyEvent.VK_DOWN, java.awt.event.KeyEvent.VK_S -> applyMove(Direction.DOWN);
                    case java.awt.event.KeyEvent.VK_LEFT, java.awt.event.KeyEvent.VK_A -> applyMove(Direction.LEFT);
                    case java.awt.event.KeyEvent.VK_RIGHT, java.awt.event.KeyEvent.VK_D -> applyMove(Direction.RIGHT);
                    case java.awt.event.KeyEvent.VK_R -> restart();
                }
            }
        });
    }

    private void restart(){
        gameState = levels.getGameStateSolvableNonCentral();
        playerDir = Direction.UP;
        drawPanel.repaint();
    }

    private void applyMove(Direction dir){
        GameState next = move.move(gameState, dir);
        if (next != null){
            gameState = next;
            playerDir = dir;
            drawPanel.repaint();

            if (gameState.checkWining()) {
                JOptionPane.showMessageDialog(this, "🎉 You WIN!");
            }

            if (gameState.getNextStates().getSuccessors().isEmpty() && !gameState.checkWining()) {
                JOptionPane.showMessageDialog(this, "❌ No moves left — You LOSE!");
            }
        }
    }

    private class Maze3DPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            var grid = gameState.getBoard().getGrid();
            int R = grid.length;
            int C = grid[0].length;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int tileW = 90;
            int tileH = 45;
            int tileDepth = 40;

            int startX = getWidth()/2 - (C * tileW)/2;
            int startY = 120;

            Pyramid pyramid = gameState.getPyramid();
            int pr = pyramid.getLocation().getRow();
            int pc = pyramid.getLocation().getColumn();

            for (int r = 0; r < R; r++){
                for (int c = 0; c < C; c++){
                    Color col = getColor(grid[r][c]);
                    int x = startX + (c - r) * tileW / 2;
                    int y = startY + (c + r) * tileH / 2;

                    // مكعبات VOID أو COLLAPSED
                    if (grid[r][c].getType() == basicStructure.SquareType.VOID) {
                        drawCollapsedBase(g2, x, y, tileW, tileH, tileDepth);
                    } else if (grid[r][c].getState() != basicStructure.SquareState.COLLAPSED) {
                        drawCube(g2, x, y, tileW, tileH, tileDepth, col);
                    }

                    // رسم الظل على المكعب الذي عليه اللاعب
                    if (r == pr && c == pc){
                        drawShadowAboveCube(g2, x, y, tileW);
                    }

                    // رسم الهرم فوق المكعب
                    if (r == pr && c == pc){
                        drawPyramid(g2, x, y - tileDepth, pyramid);
                    }
                }
            }
        }

        private void drawCube(Graphics2D g, int x, int y, int w, int h, int depth, Color topColor){
            Color side = topColor.darker();
            Color front = topColor.darker().darker();

            Polygon top = new Polygon();
            top.addPoint(x, y);
            top.addPoint(x + w/2, y + h/2);
            top.addPoint(x, y + h);
            top.addPoint(x - w/2, y + h/2);

            Polygon left = new Polygon();
            left.addPoint(x - w/2, y + h/2);
            left.addPoint(x, y + h);
            left.addPoint(x, y + h + depth);
            left.addPoint(x - w/2, y + h/2 + depth);

            Polygon right = new Polygon();
            right.addPoint(x + w/2, y + h/2);
            right.addPoint(x, y + h);
            right.addPoint(x, y + h + depth);
            right.addPoint(x + w/2, y + h/2 + depth);

            g.setColor(topColor); g.fillPolygon(top);
            g.setColor(side); g.fillPolygon(left);
            g.setColor(front); g.fillPolygon(right);

            g.setColor(Color.BLACK);
            g.drawPolygon(top); g.drawPolygon(left); g.drawPolygon(right);
        }

        private void drawCollapsedBase(Graphics2D g, int x, int y, int w, int h, int depth){
            Polygon base = new Polygon();
            base.addPoint(x - w/2, y + h/2 + depth/2);
            base.addPoint(x + w/2, y + h/2 + depth/2);
            base.addPoint(x, y + h + depth/2);

            g.setColor(new Color(30,30,30));
            g.fillPolygon(base);
            g.setColor(Color.BLACK);
            g.drawPolygon(base);
        }

        private void drawPyramid(Graphics2D g, int x, int y, Pyramid pyramid){
            int size = 30;
            int ax = x;
            int ay = y - size;
            int bx = x - size/2;
            int by = y;
            int cx = x + size/2;
            int cy = y;
            int dx = x;
            int dy = y + size/3;

            Color face1 = new Color(255, 230, 90);
            Color face2 = new Color(235, 200, 60);
            Color face3 = new Color(200, 170, 45);

            Polygon leftFace = new Polygon(); leftFace.addPoint(ax, ay); leftFace.addPoint(bx, by); leftFace.addPoint(dx, dy);
            Polygon rightFace = new Polygon(); rightFace.addPoint(ax, ay); rightFace.addPoint(cx, cy); rightFace.addPoint(dx, dy);
            Polygon frontFace = new Polygon(); frontFace.addPoint(bx, by); frontFace.addPoint(cx, cy); frontFace.addPoint(dx, dy);

            g.setColor(face3); g.fillPolygon(frontFace);
            g.setColor(face1); g.fillPolygon(leftFace);
            g.setColor(face2); g.fillPolygon(rightFace);

            g.setColor(Color.BLACK);
            g.drawPolygon(leftFace); g.drawPolygon(rightFace); g.drawPolygon(frontFace);

            // سهم الاتجاه
            g.setColor(Color.RED);
            int arrowLength = 15;
            switch(playerDir){
                case UP -> g.fillPolygon(new int[]{ax, ax-5, ax+5}, new int[]{ay-20, ay-5, ay-5}, 3);
                case DOWN -> g.fillPolygon(new int[]{ax, ax-5, ax+5}, new int[]{ay+5, ay+20, ay+20}, 3);
                case LEFT -> g.fillPolygon(new int[]{ax-20, ax-5, ax-5}, new int[]{ay, ay-5, ay+5}, 3);
                case RIGHT -> g.fillPolygon(new int[]{ax+20, ax+5, ax+5}, new int[]{ay, ay-5, ay+5}, 3);
            }

            // عرض عدد المفاتيح 🔑
            int keyCount = pyramid.getNumberOfKey();
            if(keyCount > 0){
                g.setColor(Color.YELLOW);
                g.setFont(new Font("Segoe UI", Font.BOLD, 20));
                g.drawString("🔑 x" + keyCount, ax - 20, ay - 35);
            }
        }

        // ظل على المكعب الذي عليه اللاعب
        private void drawShadowAboveCube(Graphics2D g, int x, int y, int tileW){
            g.setColor(new Color(0,0,0,80));
            g.fillOval(x - tileW/4, y + 5, tileW/2, 12);
        }

        private Color getColor(basicStructure.Square sq){
            if (sq.getType() == basicStructure.SquareType.VOID) return new Color(40,40,40);
            if (sq.getState() == basicStructure.SquareState.COLLAPSED) return new Color(0,0,0,0);
            if (sq instanceof basicStructure.SpecialSquare) return new Color(170,110,250);
            if (sq.isEnd()) return new Color(70,210,140);
            if (sq.isLocked()) return new Color(210,145,60);
            if (sq.getHas() == basicStructure.Has.KEY) return new Color(250,225,95);

            return switch (sq.getStrength()) {
                case STRONG -> new Color(75,130,220);
                case MEDIUM -> new Color(120,170,245);
                case WEAK   -> new Color(180,215,255);
            };
        }
    }

    public static void main(String[] args) {
        Levels lv = new Levels();
        GameState st = lv.getGameStateSolvableNonCentral();
        new MazeGUI3D(st);
    }
}
