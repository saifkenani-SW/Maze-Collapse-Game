package algorithim;

import basicStructure.Location;
import basicStructure.Square;
import game.*;
import basicStructure.Direction;

import java.util.*;

public class UCS {
    private final Map<GameState, Integer> bestCost = new HashMap<>();

    public GameState UCSSearch(GameState start) throws InterruptedException {
        start.setCost(0);
        start.setParent(null);

        PriorityQueue<GameState> pq = new PriorityQueue<>();
        pq.add(start);
        bestCost.put(start, 0);

        int numNodesVisited = 0, numNodesGenerated = 0;

        while (!pq.isEmpty()) {
            GameState current = pq.poll();
            if (bestCost.get(current) < current.getCost()) {
                continue;
            }
            if (current.checkWining()) {
                System.out.println(bestCost.get(current));
                new Common(false).showUI(current);

                System.err.println("Solution found \n" +
                        "The cost " + current.getCost());

                System.out.println("numNodesVisited: " + bestCost.size());
                System.out.println("numNodesGenerated: " +numNodesGenerated);
                return current;
            }

            Map<Direction,GameState> successors = current.getNextStates(false).getSuccessors();
            numNodesGenerated +=successors.size();

            for (GameState next : successors.values()) {
                int newCost = current.getCost() + getStepCost(next);

                Integer found = bestCost.get(next);
                if (found == null || newCost < found) {
                    numNodesVisited++;
                    next.setCost(newCost);
                    next.setParent(current);
                    bestCost.put(next, newCost);
                    pq.add(next);
                }
            }
        }

        System.out.println("Not found solution");
        System.out.println("numNodesVisited: " + numNodesVisited);
        System.out.println("numNodesGenerated: " + numNodesGenerated);
        return null;
    }

    private int getStepCost(GameState state) {
        Location location = state.getPyramid().getLocation();
        Square square = state.getBoard().getSquare(location.getRow(), location.getColumn());
        return square.getColor().getComplexity();
    }
}