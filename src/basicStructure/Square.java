package basicStructure;

public class Square {

    private final Location location;
    private boolean isLocked = false;
    private final Color color;
    private SquareState state = SquareState.NOT_COLLAPSED;
    private SquareType squareType = SquareType.NORMAL;
    private SquareStrength squareStrength = SquareStrength.WEAK;
    private Has has = Has.NOTHING;


    public Square(Location location, Color color, boolean isLocked, SquareState state, SquareType squareType, SquareStrength squareStrength, Has has) {
        this.location = location;
        this.isLocked = isLocked;
        this.color = color;
        this.squareStrength = squareStrength;
        this.squareType = squareType;
        if (squareType == SquareType.VOID) {
            this.state = SquareState.COLLAPSED;
        } else this.state = state;
        this.has = has;
    }

    public Square(Location location) {
        this(location, Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    }

    public Location getLocation() {
        return location;
    }


    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public Color getColor() {
        return color;
    }

    public SquareState getState() {
        return state;
    }

    public void setState(SquareState state) {
        this.state = state;
    }

    public SquareType getSquareType() {
        return squareType;
    }

    public void setSquareType(SquareType squareType) {
        this.squareType = squareType;
    }

    public SquareStrength getSquareStrength() {
        return squareStrength;
    }

    public void setSquareStrength(SquareStrength squareStrength) {
        this.squareStrength = squareStrength;
    }

    public Has getHas() {
        return has;
    }

    public void setHas(Has has) {
        this.has = has;
    }

    public boolean isCollapsed() {
        return state == SquareState.COLLAPSED;
    }

    public boolean isVoid() {
        return squareType == SquareType.VOID;
    }

    public boolean isEnd() {
        return squareType == SquareType.END;
    }


    @Override
    public String toString() {
        return "Square{" +
                "location=" + location +
                ", isLocked=" + isLocked +
                ", color=" + color +
                ", state=" + state +
                ", squareType=" + squareType +
                ", squareStrength=" + squareStrength +
                ", has=" + has +
                '}';
    }
}
