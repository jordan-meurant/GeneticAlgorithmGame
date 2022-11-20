import org.joml.Vector2i;
import utils.Colors;

public class Creature extends Tile {
    private int tick;

    public Creature(Vector2i position, String color) {
        super(position, color);
        this.tick = 0;
    }

    @Override
    public String toString() {
        return color + "\u263b" + "\t" + Colors.RESET_COLORS;
    }

    public void setPosition(Vector2i newPosition){
        this.position = newPosition;
    }

    public int getTick(){
        return this.tick;
    }

    public void incrementTick(){
        this.tick++;
    }public void decrementTick(){
        this.tick--;
    }

    public void resetTick(){
        this.tick = 0;
    }
}
