package algorithim;

import game.GameState;
import logic.Direction;
import ui.GameSolverFrame;
import ui.Maze2DPanel;
import ui.SearchFrame;

import java.util.*;

public class Common {

    boolean requireCollapseBeforeEnd;

    public Common(boolean requireCollapseBeforeEnd) {
        this.requireCollapseBeforeEnd = requireCollapseBeforeEnd;
    }

    public List<Direction> getDirection(GameState state) {
        List<Direction> directions = new ArrayList<>();
        GameState cur = state;

        while (cur != null && cur.getParent() != null) {
            GameState parent = cur.getParent();
            Map<Direction, GameState> succ = parent.getNextStates(requireCollapseBeforeEnd).getSuccessors();

            Direction found = null;
            for (Map.Entry<Direction, GameState> e : succ.entrySet()) {
                if (e.getValue().equals(cur)) {
                    found = e.getKey();
                    break;
                }
            }

            if (found == null) break;
            directions.add(found);
            cur = parent;
        }

        Collections.reverse(directions);
        return directions;
    }

    public void showUI(GameState endState) {
        GameSolverFrame.showUI(endState);
    }

}