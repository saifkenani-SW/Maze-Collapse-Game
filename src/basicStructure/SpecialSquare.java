package basicStructure;


import java.util.HashMap;
import java.util.Map;

public class SpecialSquare extends  Square{
    Map<Location,Square> successors=new HashMap<Location,Square>();

    public SpecialSquare() {

    }
    public SpecialSquare(Color color, boolean isLocked, SquareState state, SquareType squareType, SquareStrength squareStrength, Has has, Map<Location, Square> children) {
        super(color, isLocked, state, squareType, squareStrength, has);
        this.successors = children;
    }
    public SpecialSquare(Square square, Map<Location, Square> successors) {
        this(square.getColor(),square.isLocked(),square.getState(),square.getType(),square.getStrength(),square.getHas(),successors);
        this.successors = successors;
    }

    public Map<Location, Square> getChildren() {
        return successors;
    }

    public void setChildren(Map<Location, Square> children) {
        this.successors = children;
    }

    public SpecialSquare(Map<Location, Square> children) {
        this.successors = children;
    }

    public void addChild(Location location,Square square) {
        successors.put(location, square);
    }

    public Map<Location, Square> getSuccessors() {
        return successors;
    }

    public Square getValue(Location location) {
        return successors.get(location);
    }
    public SpecialSquare clone(){
        Map <Location,Square>cloneMap=new HashMap<>();
        for (Map.Entry<Location, Square> entry : successors.entrySet()) {
            cloneMap.put(entry.getKey().clone(),entry.getValue().clonSquare());
        }
        return new SpecialSquare(super.clone(),cloneMap);
    }


}
