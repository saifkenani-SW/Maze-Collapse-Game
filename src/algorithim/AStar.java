package algorithim;

import basicStructure.Direction;
import basicStructure.Has;
import basicStructure.Location;
import basicStructure.Square;
import game.Board;
import game.GameState;
import game.Pyramid;

import java.util.*;

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
        if ((locksNumber<pyramid.getNumberOfKey())||(keysNumber==0)||(locksNumber<=pyramid.getNumberOfKey()
        ||(isLockInRectangle(board,pyramidLocation,endLocation)==0)
        ||(isLockInRectangle(board,pyramidLocation,endLocation)<=pyramid.getNumberOfKey()))) {
            return manhattan(pyramidLocation, endLocation);
        }
        else {
            Location nearestKey = findNearest(endLocation, board.getKeys());
             // return manhattan(pyramidLocation, nearestKey) + manhattan(nearestKey, endLocation);

            Location bestKeyLocation=getBestKeyLocation(board,pyramidLocation,endLocation);
            return manhattan(pyramidLocation, nearestKey) + manhattan(nearestKey, endLocation);

        }
    }

    private int manhattan(Location location1, Location location2) {
        return Math.abs(location1.getRow() - location2.getRow()) + Math.abs(location1.getColumn() - location2.getColumn());
    }


    private int isLockInRectangle(Board board,Location pyramidLocation,Location endLocation){
        Square[][]grid= board.getGrid();
        int scale=2;
        int lowRow=Math.min(pyramidLocation.getRow()-scale,endLocation.getRow()-scale);
        int maxRow=Math.max(pyramidLocation.getRow()+scale,endLocation.getRow()+scale);
        int lowCol=Math.min(pyramidLocation.getColumn()-scale,endLocation.getColumn()-scale);
        int maxCol=Math.max(pyramidLocation.getColumn()+scale,endLocation.getColumn()+scale);
        int keysNumber=0;
        int locksNumber=0;

        for (int i = lowRow; i <=maxRow; i++) {
            if (i<0||i>=grid.length)continue;
            for (int j = lowCol; j <=maxCol; j++) {
                if (j<0||j>=grid[0].length)continue;
                if (grid[i][j].isLocked()){
                    locksNumber++;
                }
                if (grid[i][j].getHas()== Has.KEY){
                    keysNumber++;
                }
            }
        }

        return locksNumber;
    }

    private Location findNearest(Location current, List<Location> locations) {
        Location nearest = locations.get(0);
        int minDist = manhattan(current, nearest);

        for (Location location : locations) {
            int distance = manhattan(current, location);
            if (distance < minDist) {
                minDist = distance;
                nearest = location;
            }
        }
        return nearest;
    }

    private Location getBestKeyLocation(Board board,Location pyramidLocation,Location endLocation){
        Location bestKeyLocation = null;
        int minTotalDistance = Integer.MAX_VALUE;

        for (Location key : board.getKeys()) {
            int totalDist = manhattan(pyramidLocation, key) + manhattan(key, endLocation);

            if (totalDist < minTotalDistance) {
                minTotalDistance = totalDist;
                bestKeyLocation = key;
            }
        }
        return bestKeyLocation;
    }

}
