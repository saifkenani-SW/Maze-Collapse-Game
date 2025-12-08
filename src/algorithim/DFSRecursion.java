package algorithim;

import game.GameState;
import basicStructure.Direction;

import java.util.*;

public class DFSRecursion {
    private final Set<GameState> visited = new HashSet<>();

    public DFSRecursion() {
    }

    public GameState DFSSearch(GameState state) {
        if (state == null) return null;

        Map<Direction, GameState> successors = state.getNextStates(true).getSuccessors();

        if (state.checkWining()) {
            new Common(false).showUI(state);
            return state;
        }

        if (successors.isEmpty()) {
            return null;
        }

        for (Map.Entry<Direction, GameState> entry : successors.entrySet()) {
            GameState successor = entry.getValue();
            if (addToVisited(state, successor)) {
                GameState found = DFSSearch(successor);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private boolean addToVisited(GameState parent, GameState state) {
        if (!visited.contains(state)) {
            state.setParent(parent);
            visited.add(state);
            return true;
        }
        return false;
    }
}
