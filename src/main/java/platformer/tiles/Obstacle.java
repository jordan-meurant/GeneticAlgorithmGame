package platformer.tiles;

import org.joml.Vector2i;
import utils.Colors;

public class Obstacle extends Tile {
    public Obstacle(Vector2i position, String color) {
        super(position, color);
    }

    @Override
    public String toString() {
        return color + "\u26DD" + "\t" + Colors.RESET_COLORS;
    }
}
