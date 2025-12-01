package algorithim;

import game.GameState;
import logic.Direction;

import java.util.*;

public class DFSRecursion {
   private final Set<GameState> visited=new HashSet<>();

    public DFSRecursion(GameState root) {
        root.setParent(null);
        visited.add(root);
        recursion(root);
    }

    private void recursion(GameState state) {
        Map<Direction,GameState>successors=state.getNextStates(true).getSuccessors();
        if (state.checkWining()){
            System.out.println(state);
          //  System.out.println(Common.getPath(state));
            ArrayList<Direction>path=new Common(true).getDirection(state);
            System.out.println(path);
        }
        if (successors.isEmpty()){
            return;
        }
        for (Map.Entry<Direction,GameState>entry: successors.entrySet()){
           GameState successor=entry.getValue();
            if (addToVisited(state,successor)){
            recursion(successor);
            }
        }

    }

    private boolean addToVisited(GameState parent ,GameState state) {
        if (!visited.contains(state)){
          state.setParent(parent);
         visited.add(state);
         return true;
        }
        return false;
    }
}
