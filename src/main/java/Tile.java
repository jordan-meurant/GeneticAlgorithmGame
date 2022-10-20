import org.joml.Vector2i;
import utils.Colors;

import java.awt.geom.Point2D;

public class Tile {
    protected  final Vector2i position;
    protected  String color;

    public  Vector2i getPosition() {
        return position;
    }


    public Tile( Vector2i position, String color) {
        this.position = position;
        this.color = color;
    }

    @Override
    public String toString() {
        return color+ "\t"+ Colors.RESET_COLORS;
    }
}
