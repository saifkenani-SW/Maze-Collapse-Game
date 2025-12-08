package algorithim;

import game.GameState;
import basicStructure.Direction;
import ui.GameSolverFrame;


import java.util.*;

public class Common {

    boolean requireCollapseBeforeEnd;

    public Common(boolean requireCollapseBeforeEnd) {
        this.requireCollapseBeforeEnd = requireCollapseBeforeEnd;
    }

    public List<Direction> getDirection(GameState state) {
        List<Direction> directions = new ArrayList<>();
        GameState current = state;

        while (current != null && current.getParent() != null) {
            GameState parent = current.getParent();
            Map<Direction, GameState> successors = parent.getNextStates(requireCollapseBeforeEnd).getSuccessors();

            Direction found = null;
            for (Map.Entry<Direction, GameState> entry : successors.entrySet()) {
                if (entry.getValue().equals(current)) {
                    found = entry.getKey();
                    break;
                }
            }

            if (found == null) break;
            directions.add(found);
            current = parent;
        }

        Collections.reverse(directions);
        return directions;
    }

    public void showUI(GameState endState) {
        GameSolverFrame.showUI(endState);
    }

}