package algorithim;

import game.GameState;
import game.NextStates;
import basicStructure.Direction;

import java.util.*;

public class BFS {

   private final Set<GameState> visited=new HashSet<>();
    int numNodesVisited=0;
    int numNodesGenerated=0;


    public BFS() throws InterruptedException {
    }


    public GameState  BFSSearch(GameState root) throws InterruptedException {
        visited.add(root);
        Queue<GameState> queue =new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {

            GameState state = queue.poll();

            if (state.checkWining()) {
                new Common(true).showUI(state);
                System.out.println("number of visited node is +"+ visited.size());// == numNodesVisited
                System.out.println("number of Generated node is +"+ numNodesGenerated);
                return state;
            }
            NextStates nextStates = state.getNextStates(true);
            numNodesGenerated+=nextStates.getSuccessors().size();
            for (Map.Entry<Direction, GameState> entry : nextStates.getSuccessors().entrySet()) {
                addToQueue(queue, state, entry.getValue());
            }
        }
        return null;
    }

    private void addToQueue(Queue<GameState> queue, GameState parent,GameState state) {
        if (!visited.contains(state)) {
            numNodesVisited++;
            state.setParent(parent);
            visited.add(state);
            queue.add(state);
        }
    }

}
