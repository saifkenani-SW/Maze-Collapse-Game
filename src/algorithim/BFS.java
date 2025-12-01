package algorithim;

import game.GameState;
import game.NextStates;
import logic.Direction;

import javax.swing.plaf.nimbus.State;
import java.util.*;

public class BFS {

   private final Set<GameState> visited=new HashSet<>();
    int numNodesVisited=0;
    int numNodesGenerated=0;


    public BFS(GameState gameState) {
            BFSSearch(gameState);
    }


    public void  BFSSearch(GameState root) {
        visited.add(root);
        numNodesVisited++;
        Queue<GameState> queue =new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {

            GameState state = queue.poll();
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
            for (Map.Entry<logic.Direction, GameState> entry : nextStates.getSuccessors().entrySet()) {
                addToQueue(queue, state, entry.getValue());
            }
        }
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
