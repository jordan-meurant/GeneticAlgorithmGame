import org.joml.Vector2i;

public enum Direction {
    UP_LEFT(-1, 1),
    DOWN_LEFT(-1, -1),
    LEFT(-1, 0),
    UP_RIGHT(1, 1),
    DOWN_RIGHT(1, -1),
    RIGHT(0, 1),
    UP(1, 0),
    DOWN(0, 1);


    private final Vector2i direction;

    public Vector2i getDirection() {
        return direction;
    }

    Direction(int x, int y) {
         this.direction = new Vector2i(x,y);
    }
}

// 3 3
// 4 3

// 0 for down, 1 for down-right, 2 for right, 3 for up-right,
//4 for up, 5 for up-left, 6 for left and 7 for down-left
