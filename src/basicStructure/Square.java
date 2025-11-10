package basicStructure;

import game.Board;

import java.util.Objects;

public class Square {

    // private final Location location;
    private boolean isLocked = false;
    private final Color color;
    private SquareState state;
    private SquareType squareType;
    private SquareStrength squareStrength;
    private Has has = Has.NOTHING;


    public Square( /*Location location,*/ Color color, boolean isLocked, SquareState state, SquareType squareType, SquareStrength squareStrength, Has has) {
        // this.location = location;
        this.isLocked = isLocked;
        this.color = color;
        this.squareStrength = squareStrength;
        this.squareType = squareType;
        if (squareType == SquareType.VOID) {
            this.state = SquareState.COLLAPSED;
        } else this.state = state;
        this.has = has;
    }

    public Square(/*Location location*/) {
        this(/*location,*/ Color.WHITE, false, SquareState.NOT_COLLAPSED, SquareType.NORMAL, SquareStrength.WEAK, Has.NOTHING);
    }

//    public Location getLocation() {
//        return location;
//    }


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

    public SquareType getType() {
        return squareType;
    }

    public void setType(SquareType squareType) {
        this.squareType = squareType;
    }

    public SquareStrength getStrength() {
        return squareStrength;
    }

    public void setStrength(SquareStrength squareStrength) {
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

    public boolean isStart() {
        return squareType == SquareType.START;
    }

    public Square clonSquare() {
        return new Square(
                this.color,
                this.isLocked,
                this.state,
                this.squareType,
                this.squareStrength,
                this.has
        );
    }


    @Override
    public String toString() {
        return "Square{" +
                // "location=" + location +
                ", isLocked=" + isLocked +
                ", color=" + color +
                ", state=" + state +
                ", squareType=" + squareType +
                ", squareStrength=" + squareStrength +
                ", has=" + has +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Square obj)) return false;

        return obj.isLocked == isLocked &&
                obj.getStrength() == squareStrength &&
                obj.getType() == squareType &&
                obj.getHas() == has &&
                obj.getColor() == color &&
                obj.getState() == state;

    }

    @Override
    public int hashCode() {
        return Objects.hash(state, squareType, squareStrength, has, isLocked, color);
    }

}
