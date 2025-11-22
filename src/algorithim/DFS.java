package algorithim;

import game.GameState;
import game.NextState;
import logic.Direction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DFS {

    private final Set visited = new HashSet();

    public DFS(GameState root) {
        visited.add(root);
        Stack<GameState> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {

            GameState state = stack.pop();
            if (state.checkWining()) {
                System.out.println(Common.getPath(state));
                return;
            }
            NextState nextState = state.getNextStates();
            for (Map.Entry<Direction, GameState> entry : nextState.getSuccessors().entrySet()) {
                addToStack(stack, state, entry.getValue());
            }
        }
    }

    private void addToStack(Stack<GameState> stack, GameState parent, GameState state) {
        if (!visited.contains(state)) {
            state.setParent(parent);
            visited.add(state);
            stack.push(state);
            }
    }

}
