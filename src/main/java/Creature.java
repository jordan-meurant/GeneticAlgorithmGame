import org.joml.Vector2i;
import utils.Colors;

public class Creature extends Tile {

    public Creature(Vector2i position, String color) {
        super(position, color);
    }

    @Override
    public String toString() {
        return color + "\u263b" + "\t" + Colors.RESET_COLORS;
    }
}
