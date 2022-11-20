package platformer.tiles;

import org.joml.Vector2i;
import utils.Colors;

public class Flag extends Tile {

    public Flag(Vector2i position, String color) {
        super(position, color);
    }

    @Override
    public String toString() {
        return color + "\u2691" + "\t" + Colors.RESET_COLORS;
    }
}

