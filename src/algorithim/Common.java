package algorithim;

import game.GameState;
import logic.Direction;
import java.util.List;

public class Common {
    public static String getPath(GameState state) {
        StringBuilder path = new StringBuilder();
        List<Direction> directions = state.directions();

        for (Direction direction : directions) {
            path.append(direction.toString()).append(" ");
        }

        return path.toString().trim();
    }

}
