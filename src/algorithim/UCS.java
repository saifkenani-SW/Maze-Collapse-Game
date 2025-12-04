package algorithim;

import basicStructure.Location;
import basicStructure.Square;
import game.*;
import logic.Direction;

import java.util.*;

public class UCS {
    private final Map<GameState, Integer> bestCost = new HashMap<>();

    public void search(GameState start) {
        start.setCost(0);
        start.setParent(null);

        PriorityQueue<GameState> frontier = new PriorityQueue<>();
        frontier.add(start);
        bestCost.put(start, 0);

        int numNodesVisited = 0, numNodesGenerated = 0;

        while (!frontier.isEmpty()) {
            GameState current = frontier.poll();
            numNodesVisited++;
            if (bestCost.get(current) < current.getCost()) {
                continue;
            }
            if (current.checkWining()) {
                System.out.println(new Common(false).getDirection(current));
                System.err.println("\nGOAL FOUND! \n" +
                        "Cost is " + current.getCost());
                return;
            }

            NextStates nextStates = current.getNextStates(false);
            numNodesGenerated += nextStates.getSuccessors().size();

            for (GameState next : nextStates.getSuccessors().values()) {
                if (next == null) continue;

                int newCost = current.getCost() + getStepCost(next);

                Integer known = bestCost.get(next);
                if (known == null || newCost < known) {
                    GameState copy = next.clone();
                    copy.setCost(newCost);
                    copy.setParent(current);

                    bestCost.put(copy, newCost);
                    frontier.add(copy);
                }
            }
        }

        System.out.println("\n❌ No solution found.");
        System.out.println("Total nodes numNodesVisited: " + numNodesVisited);
        System.out.println("Total nodes numNodesGenerated: " + numNodesGenerated);
    }

    private int getStepCost(GameState state) {
        Location location = state.getPyramid().getLocation();
        Square square = state.getBoard().getSquare(location.getRow(), location.getColumn());
        return square.getColor().getComplexity();
    }
}