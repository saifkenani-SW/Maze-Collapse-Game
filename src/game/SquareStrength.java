package game;

public enum SquareStrength {
    WEAK(1),
    MEDIUM(2),
    STRONG(3);

    private final int maxSteps;

    SquareStrength(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public int getMaxSteps() {
        return maxSteps;
    }
}
