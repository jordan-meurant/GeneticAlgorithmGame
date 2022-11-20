package platformer.tiles;

import org.joml.Vector2i;

public class Tile {
    protected Vector2i position;
    protected String color;

    public Tile(Vector2i position, String color) {
        this.position = position;
        this.color = color;
    }

    public Vector2i getPosition() {
        return position;
    }

    //@Override
    //public String toString() {
    //  return color+ "\t"+ Colors.RESET_COLORS;
    //}

    @Override
    public String toString() {
        return "\t";
    }
}
