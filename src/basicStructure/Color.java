package basicStructure;

public enum Color {
    RED(3),
    BLACK(5),
    WHITE(1),
    BLUE(5),
    GREEN(4),
    YELLOW(2),
    PINK(3),
    DARK(10);

   private int complexity;
    Color(int complexity) {
        this.complexity = complexity;
    }

    public int getComplexity() {
        return complexity;
    }
}
