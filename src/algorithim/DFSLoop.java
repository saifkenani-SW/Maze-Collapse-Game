package algorithim;

import game.GameState;
import game.NextStates;
import logic.Direction;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class DFSLoop {
int numNodesVisited=0;
int numNodesGenerated=0;
    private final Set visited = new HashSet();

    public DFSLoop(GameState root) {
        visited.add(root);
        numNodesVisited++;
        Stack<GameState> stack = new Stack<>();
        stack.push(root);
        while (!stack.isEmpty()) {

            GameState state = stack.pop();
            System.out.println(state);
            if (state.checkWining()) {
                System.out.println(new Common(true).getDirection(state));
                System.out.println("number of visited node is +"+ visited.size());// == numNodesVisited
               // System.out.println("number of visited node is +"+ numNodesVisited);// == numNodesVisited
                System.out.println("number of Generated node is +"+ numNodesGenerated);
                return;
            }
            NextStates nextStates = state.getNextStates(true);
            numNodesGenerated+=nextStates.getSuccessors().size();
            for (Map.Entry<Direction, GameState> entry : nextStates.getSuccessors().entrySet()) {
                pushToStack(stack, state, entry.getValue());
            }
        }
    }

    private void pushToStack(Stack<GameState> stack, GameState parent, GameState state) {
        if (!visited.contains(state)) {
            numNodesVisited++;
            state.setParent(parent);
            visited.add(state);
            stack.push(state);
            }
    }

}
