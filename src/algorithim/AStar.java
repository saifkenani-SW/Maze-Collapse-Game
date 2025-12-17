package algorithim;

import basicStructure.Direction;
import basicStructure.Location;
import basicStructure.Square;
import game.Board;
import game.GameState;
import game.Pyramid;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class AStar {

    Map<GameState, Integer> bestCost = new HashMap<>();
    int numVisited = 0, numGenerated = 0;
    public AStar() {
    }

    public GameState AStarSearch(GameState root) {
        root.setParent(null);
        root.setCost(0);

        PriorityQueue<GameState> pq = new PriorityQueue<>(
                Comparator.comparingInt(state -> state.getCost() + heuristic(state))
        );
        bestCost.put(root, 0);
        pq.add(root);


        while (!pq.isEmpty()) {
            GameState current = pq.poll();
            if (bestCost.get(current) < current.getCost()) {
                continue;
            }
            if (current.checkWining()) {
                System.currentTimeMillis();
                //  System.out.println(bestCost.get(current));
                new Common(false).showUI(current);

                System.err.println("Solution found A* \n" +
                        "The cost " + current.getCost());

                System.err.println("numNodesVisited: " + bestCost.size());
                System.err.println("numNodesGenerated: " + numGenerated);
                System.err.println("______________________________________");
                System.out.println();
                System.out.println();
                return current;
            }


            Map<Direction, GameState> nextStates = current.getNextStates(false).getSuccessors();
            numGenerated += nextStates.size();
            for (Map.Entry<Direction, GameState> entry : nextStates.entrySet()) {
                GameState next = entry.getValue();
                addToDQ(pq, current, next);

            }

        }

        return null;
    }

    private void addToDQ(PriorityQueue<GameState> pq, GameState parent, GameState current) {
        current.setCost(getStateCost(parent, current));
        int cost = current.getCost();
        System.out.println();
        if (bestCost.get(current) == null || bestCost.get(current) > cost) {
            bestCost.put(current, cost);
            numVisited++;
            current.setParent(parent);
            pq.add(current);
        }
    }

    private int getStateCost(GameState parent, GameState current) {
        Square[][] grid = current.getBoard().getGrid();
        Location location = current.getPyramid().getLocation();

        return parent.getCost() + grid[location.getRow()][location.getColumn()].getColor().getComplexity();
    }


    private int heuristic(GameState state){

        Board board = state.getBoard();
        Pyramid pyramid=state.getPyramid();
        Location pyramidLocation = state.getPyramid().getLocation();
        Location endLocation = board.getEndLocation();
        int keysNumber=board.getKeys().size();
        int locksNumber=board.getLocks().size();
        if ((locksNumber<pyramid.getNumberOfKey())||(keysNumber==0)||(locksNumber<=pyramid.getNumberOfKey())) {
           int row =Math.abs(pyramidLocation.getRow()-endLocation.getRow());
           int col =Math.abs(pyramidLocation.getColumn()-endLocation.getColumn());

            return manhattan(pyramidLocation, endLocation);
        }




        return 0;
    }

    private int manhattan(Location location1, Location location2) {
        return Math.abs(location1.getRow() - location2.getRow()) + Math.abs(location1.getColumn() - location2.getColumn());
    }
}
