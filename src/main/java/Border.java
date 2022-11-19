import org.joml.Vector2i;
import utils.Colors;

public class Border extends Tile {
    public Border(Vector2i position, String color) {
        super(position, color);
    }

    @Override
    public String toString() {
        return color + "_" + "\t" + Colors.RESET_COLORS;
    }
}
