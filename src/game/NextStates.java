package game;

import basicStructure.Direction;

import java.util.HashMap;
import java.util.Map;

public class NextStates {
    private final Map<Direction, GameState> successors = new HashMap<>();

    public void addState(GameState parent, GameState gameState, Direction direction) {
        gameState.setParent(parent);
        successors.put(direction, gameState);
    }

    public Map<Direction, GameState> getSuccessors() {
        return successors;
    }

    public GameState getValue(Direction direction) {
        return successors.get(direction);
    }
}
