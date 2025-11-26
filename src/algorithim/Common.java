package algorithim;

import game.GameState;
import logic.Direction;

import java.util.*;

public class Common {
   private ArrayList<Direction> directions = new ArrayList<>();
    /*public static String getPath(GameState state) {
        StringBuilder path = new StringBuilder();
        List<Direction> directions = state.directions();

        for (Direction direction : directions) {
            path.append(direction.toString()).append(" ");
        }

        return path.toString().trim();
    }*/

    public ArrayList<Direction> getDirection(GameState state){
        if (state.getParent() == null) {
/*
            System.out.println(state+"\n FFFFIIIIRRRRCCCC");
*/
            Collections.reverse(directions);

            return directions;
        }
        Map<Direction, GameState> map = state.getParent().getNextStates().getSuccessors();
        for (Map.Entry<Direction, GameState> entry : map.entrySet()) {
            if (entry.getValue().equals(state)) {
               // System.out.println(state);
                directions.add(entry.getKey());
                return getDirection(state.getParent());
            }
        }
        Collections.reverse(directions);

        return directions;
    }

}
