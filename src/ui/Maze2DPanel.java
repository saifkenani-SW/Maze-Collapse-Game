package ui;

import game.GameState;
import game.Pyramid;
import logic.Direction;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.List;

/**
 * Maze2DPanel - لوحة عرض مستقلة لِـ GameState ثنائية الأبعاد.
 * - تدعم تحميل الصور من classpath (/ui/images/) أو من مجلد خارجي
 * - واجهة عامة لربط خوارزميات البحث (setVisited, setFrontier, setPath)
 *
 * استخدام: أضفها إلى أي container ثم نادِ setGameState() لعرض الحالة.
 */
public class Maze2DPanel extends JPanel {

    private GameState gameState;
    private Direction playerDir = Direction.UP;

    private final int tileSize = 80;
    private final int tilePadding = 10;
    private int imageInset = 12; // المسافة التي تتركها لاظهار لون الخلية حول الصورة

    private final Map<String, BufferedImage> images = new HashMap<>();

    // للرسم أثناء تجارب البحث
    private final Set<Point> visited = new HashSet<>();    // نقاط زارها الباحث
    private final Set<Point> frontier = new HashSet<>();   // حدود البحث
    private final List<Point> path = new ArrayList<>();    // مسار الحل (إن وجد)

    public Maze2DPanel() {
        setPreferredSize(new Dimension(600, 500));
        loadImages(); // يحاول من classpath ثم ./src/ui/images ثم يترك المستخدم يختار
    }

    // -------------------------
    // Public API المستخدمة من الخوارزمية/المشغّل
    // -------------------------
    public void setGameState(GameState st) {
        this.gameState = st;
        repaint();
    }

    public void setPlayerDir(Direction d) {
        this.playerDir = d;
        repaint();
    }

    public void setVisited(Collection<Point> pts) {
        this.visited.clear();
        if (pts != null) this.visited.addAll(pts);
        repaint();
    }

    public void setFrontier(Collection<Point> pts) {
        this.frontier.clear();
        if (pts != null) this.frontier.addAll(pts);
        repaint();
    }

    public void setPath(List<Point> p) {
        this.path.clear();
        if (p != null) this.path.addAll(p);
        repaint();
    }

    public void clearSearchOverlay() {
        visited.clear();
        frontier.clear();
        path.clear();
        repaint();
    }

    public boolean loadImagesFromFolder(File folder) {
        if (folder == null || !folder.isDirectory()) return false;
        Map<String, String> imageFiles = Map.of(
                "tile", "Tile.png",
                "player", "Player.png",
                "key", "Key.png",
                "lock", "Lock.png",
                "end", "End.png",
                "special", "Special.png",
                "void", "Void.png",
                "stone", "Stone.png",
                "rock", "Rock.png"
        );
        int ok = 0;
        for (var e : imageFiles.entrySet()) {
            File f = new File(folder, e.getValue());
            if (f.exists() && f.isFile()) {
                try {
                    BufferedImage img = ImageIO.read(f);
                    if (img != null) {
                        images.put(e.getKey(), img);
                        ok++;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        // fallback rock->stone
        if (!images.containsKey("stone") && images.containsKey("rock")) {
            images.put("stone", images.get("rock"));
        }
        repaint();
        return ok > 0;
    }

    public void reloadImages() {
        images.clear();
        loadImages();
        repaint();
    }

    // -------------------------
    // تحميل الصور (classpath ثم ./src/ui/images ثم يطلب مجلد خارجي عند الحاجة)
    // -------------------------
    private void loadImages() {
        Map<String, String> imageFiles = Map.of(
                "tile", "Tile.png",
                "player", "Player.png",
                "key", "Key.png",
                "lock", "Lock.png",
                "end", "End.png",
                "special", "Special.png",
                "void", "Void.png",
                "stone", "Stone.png",
                "rock", "Rock.png"
        );

        // 1) من classpath /ui/images/
        for (var e : imageFiles.entrySet()) {
            String key = e.getKey();
            String resourcePath = "/ui/images/" + e.getValue();
            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                if (is != null) {
                    BufferedImage img = ImageIO.read(is);
                    if (img != null) images.put(key, img);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        // 2) مجلد مشروع ./src/ui/images
        File projectImages = new File("./src/ui/images");
        if (projectImages.exists() && projectImages.isDirectory()) {
            for (var e : imageFiles.entrySet()) {
                String key = e.getKey();
                if (images.containsKey(key)) continue;
                File f = new File(projectImages, e.getValue());
                if (f.exists() && f.isFile()) {
                    try {
                        BufferedImage img = ImageIO.read(f);
                        if (img != null) images.put(key, img);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        // fallback rock->stone
        if (!images.containsKey("stone") && images.containsKey("rock")) {
            images.put("stone", images.get("rock"));
        }

        // لو بعض الصور ناقصة - لا نرسم بدائل هنا (كما طلبت) لكن نعرض ملخص في الـ console
        System.out.println("[Maze2DPanel] loadImages summary:");
        for (var k : imageFiles.keySet()) {
            System.out.println("  " + k + " -> " + (images.containsKey(k) ? "OK" : "MISSING"));
        }
    }

    // -------------------------
    // الرسم
    // -------------------------
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == null) return;

        var grid = gameState.getBoard().getGrid();
        int R = grid.length;
        int C = grid[0].length;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            int totalW = C * (tileSize + tilePadding) - tilePadding;
            int totalH = R * (tileSize + tilePadding) - tilePadding;
            int startX = Math.max(20, getWidth() / 2 - totalW / 2);
            int startY = Math.max(20, getHeight() / 2 - totalH / 2);

            Pyramid pyramid = gameState.getPyramid();
            int pr = pyramid.getLocation().getRow();
            int pc = pyramid.getLocation().getColumn();

            int iconSize = (int) (tileSize * 0.8);

            for (int r = 0; r < R; r++) {
                for (int c = 0; c < C; c++) {
                    int x = startX + c * (tileSize + tilePadding);
                    int y = startY + r * (tileSize + tilePadding);

                    basicStructure.Square sq = grid[r][c];

                    // background color (shows cell color)
                    Color bg = getColor(sq);
                    g2.setColor(bg);
                    g2.fillRoundRect(x, y, tileSize, tileSize, 8, 8);

                    // thick border
                    Stroke oldStroke = g2.getStroke();
                    g2.setStroke(new BasicStroke(3.0f));
                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.drawRoundRect(x, y, tileSize, tileSize, 8, 8);
                    g2.setStroke(oldStroke);

                    // collapsed / void -> stone or void image
                    boolean isVoidOrCollapsed = sq.getType() == basicStructure.SquareType.VOID
                            || sq.getState() == basicStructure.SquareState.COLLAPSED;
                    if (isVoidOrCollapsed) {
                        BufferedImage stone = images.get("stone");
                        BufferedImage v = images.get("void");
                        if (stone != null) {
                            int stoneInset = Math.max(3, imageInset / 2);
                            g2.drawImage(stone, x + stoneInset, y + stoneInset, tileSize - stoneInset * 2, tileSize - stoneInset * 2, null);
                        } else if (v != null) {
                            int stoneInset = Math.max(3, imageInset / 2);
                            g2.drawImage(v, x + stoneInset, y + stoneInset, tileSize - stoneInset * 2, tileSize - stoneInset * 2, null);
                        }
                        // don't draw icons over collapsed cells
                        continue;
                    }

                    // draw tile image inset (if exists)
                    BufferedImage tileImg = images.get("tile");
                    if (tileImg != null) {
                        g2.drawImage(tileImg, x + imageInset, y + imageInset, tileSize - imageInset * 2, tileSize - imageInset * 2, null);
                    }

                    // if cell strength != WEAK draw rock background for icons (optional)
                    if (!isWeak(sq)) {
                        BufferedImage rock = images.get("rock");
                        if (rock != null) {
                            int rockSize = (int) (iconSize * 0.8);
                            int rockX = x + (tileSize - rockSize) / 2;
                            int rockY = y + (tileSize - rockSize) / 2;
                            g2.drawImage(rock, rockX, rockY, rockSize, rockSize, null);
                        }
                    }

                    // draw overlays for search visualization: visited (light tint), frontier (stronger tint), path (bold)
                    Point p = new Point(c, r); // x=col, y=row
                    if (path.contains(p)) {
                        // path highlight
                        g2.setColor(new Color(255, 215, 60, 140));
                        g2.fillRoundRect(x + 6, y + 6, tileSize - 12, tileSize - 12, 6, 6);
                    } else if (frontier.contains(p)) {
                        g2.setColor(new Color(255, 120, 0, 90));
                        g2.fillRoundRect(x + 6, y + 6, tileSize - 12, tileSize - 12, 6, 6);
                    } else if (visited.contains(p)) {
                        g2.setColor(new Color(60, 140, 200, 70));
                        g2.fillRoundRect(x + 6, y + 6, tileSize - 12, tileSize - 12, 6, 6);
                    }

                    // icons
                    int iconX = x + (tileSize - iconSize) / 2;
                    int iconY = y + (tileSize - iconSize) / 2;

                    if (sq.getHas() == basicStructure.Has.KEY) {
                        // cost bar under icon
                        drawCostBar(g2, iconX, iconY, iconSize, bg);
                        BufferedImage img = images.get("key");
                        if (img != null) g2.drawImage(img, iconX, iconY, iconSize, iconSize, null);
                    }

                    if (sq.isLocked()) {
                        drawCostBar(g2, iconX, iconY, iconSize, bg);
                        BufferedImage img = images.get("lock");
                        if (img != null) g2.drawImage(img, iconX, iconY, iconSize, iconSize, null);
                    }

                    if (sq instanceof basicStructure.SpecialSquare) {
                        BufferedImage img = images.get("special");
                        if (img != null) g2.drawImage(img, iconX, iconY, iconSize, iconSize, null);
                    }

                    if (sq.isEnd()) {
                        drawCostBar(g2, iconX, iconY, iconSize, bg);
                        BufferedImage img = images.get("end");
                        if (img != null) g2.drawImage(img, iconX, iconY, iconSize, iconSize, null);
                    }

                    if (r == pr && c == pc) {
                        BufferedImage pimg = images.get("player");
                        if (pimg != null) g2.drawImage(pimg, iconX, iconY, iconSize, iconSize, null);
                        else {
                            // if no player image we intentionally draw nothing (as requested)
                        }
                    }
                }
            }

            // draw HUD text (keys, direction)
            drawHUD(g2);

        } finally {
            g2.dispose();
        }
    }

    private void drawHUD(Graphics2D g2) {
        if (gameState == null) return;
        Pyramid pyramid = gameState.getPyramid();
        int keys = pyramid.getNumberOfKey();

        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.setColor(new Color(0, 0, 0, 200));
        String dir = switch (playerDir) {
            case UP -> "↑";
            case DOWN -> "↓";
            case LEFT -> "←";
            default -> "→";
        };
        String text = "Keys: " + keys + "   Direction: " + dir;
        g2.drawString(text, 10, 18);
    }

    private void drawCostBar(Graphics2D g2, int iconX, int iconY, int iconSize, Color bg) {
        int barH = Math.max(8, iconSize / 6);
        int barW = iconSize;
        int barX = iconX;
        int barY = iconY + iconSize - barH;
        Color fill = new Color(bg.getRed(), bg.getGreen(), bg.getBlue(), 220);
        g2.setColor(fill);
        g2.fillRoundRect(barX, barY, barW, barH, 6, 6);
        g2.setColor(new Color(0, 0, 0, 90));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(barX, barY, barW, barH, 6, 6);
        g2.setStroke(new BasicStroke(1f));
    }

    private boolean isWeak(basicStructure.Square sq) {
        try {
            return sq.getStrength() == basicStructure.SquareStrength.WEAK;
        } catch (Throwable t) {
            // older enum name fallback
            try {
                return sq.getStrength() == basicStructure.SquareStrength.WEAK;
            } catch (Throwable ex) {
                return false;
            }
        }
    }

    private Color getColor(basicStructure.Square sq) {
        // try to use square color enum if available
        try {
            basicStructure.Color color = sq.getColor();
            if (color == basicStructure.Color.WHITE) return new Color(255, 255, 255);
            if (color == basicStructure.Color.BLUE) return new Color(0, 42, 255);
            if (color == basicStructure.Color.RED) return new Color(255, 0, 0);
            if (color == basicStructure.Color.DARK) return new Color(18, 18, 18);
        } catch (Throwable ignored) {}

        if (sq.getType() == basicStructure.SquareType.VOID) return new Color(78, 78, 78);
        if (sq.getState() == basicStructure.SquareState.COLLAPSED) return new Color(70, 70, 70);
        if (sq instanceof basicStructure.SpecialSquare) return new Color(200, 150, 255);
        if (sq.isEnd()) return new Color(100, 200, 130);
        if (sq.isLocked()) return new Color(210, 145, 60);
        if (sq.getHas() == basicStructure.Has.KEY) return new Color(250, 225, 95);

        try {
            // strength enum might be named differently in your codebase
            if (sq.getStrength().toString().equalsIgnoreCase("STRONG")) return new Color(160, 190, 230);
            if (sq.getStrength().toString().equalsIgnoreCase("MEDIUM")) return new Color(190, 210, 235);
            if (sq.getStrength().toString().equalsIgnoreCase("WEAK")) return new Color(220, 230, 245);
        } catch (Throwable ignored) {}

        return new Color(200, 200, 200);
    }


}
