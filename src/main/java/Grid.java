import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.joml.Vector2i;
import utils.Colors;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int width;
    private final int height;
    private final int maxTicks;
    private Tile[][] tiles;
    private Creature creature;
    private boolean atDestination = false;
    private boolean maxTicksReached = false;

    private Vector2i initialCreaturePosition;
    private Vector2i initialFlagPosition;

    private boolean showGrid = false;

    public Grid(int width, int height, int maxTicks) {
        this.width = width;
        this.height = height;
        this.maxTicks = maxTicks;
        this.tiles = new Tile[height][width];
    }

    public void setShowGrid(boolean showGrid) {
        this.showGrid = showGrid;
    }

    public boolean isAtDestination() {
        return atDestination;
    }

    private List<String[]> readAllData(String path) {
        List<String[]> allData = new ArrayList<>();
        try {
            FileReader fileReader = new FileReader(path);

            CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();

            CSVReader csvReader = new CSVReaderBuilder(fileReader).withCSVParser(csvParser).build();

            allData = csvReader.readAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allData;
    }

    private Tile getTypeTile(int x, int y, int state) {
        Tile newTile;
        switch (state) {
            //case 0 -> newTile = new Tile(new Vector2i(x, y), Colors.WHITE_BACKGROUND);
            case 1 -> newTile = new Obstacle(new Vector2i(x, y), Colors.BLACK);
            case 2 -> {
                this.initialCreaturePosition = new Vector2i(x, y);
                this.creature = new Creature(new Vector2i(x, y), Colors.YELLOW);
                newTile = creature;
            }
            case 3 -> newTile = new Border(new Vector2i(x, y), Colors.BLACK);
            case 4 -> {
                this.initialFlagPosition = new Vector2i(x, y);
                newTile = new Flag(new Vector2i(x, y), Colors.BLACK);
            }

            default -> newTile = new Tile(new Vector2i(x, y), Colors.WHITE_BACKGROUND);
        }
        return newTile;
    }

    private void addTiles(List<String[]> data) {
        for (int x = 0; x < data.size(); x++) {
            for (int y = 0; y < data.get(x).length; y++) {
                this.tiles[x][y] = getTypeTile(x, y, Integer.parseInt(data.get(x)[y]));
            }
        }
    }

    public boolean isMaxTicksReached() {
        return maxTicksReached;
    }

    public void move(Direction direction) {
        creature.incrementTick();
        if ((creature.getTick()) > this.maxTicks) {
            this.maxTicksReached = true;
            return;
        }

        Vector2i dst = new Vector2i(creature.getPosition());
        dst.add(direction.getDirection());

        Tile dstTile = tiles[dst.y][dst.x];

        if (!checkCollision(dst)) {
            if (checkVoid(dst)) {
                setCreaturePosition(dst, creature.getPosition());
                addGravity(dst, direction);
            } else {
                setCreaturePosition(dst, creature.getPosition());
                if (dstTile instanceof Flag) {
                    this.atDestination = true;
                }
            }
        } else {
            if (!checkVoid(dst)) {
                if (voidUnderCreature()) {
                    creature.decrementTick();
                    move(Direction.DOWN);
                } else {
                    if (showGrid) System.out.println(printGrid());
                }
            } else {
                switch (direction) {
                    case UP_RIGHT -> move(Direction.RIGHT);
                    case UP_LEFT -> move(Direction.LEFT);
                }
            }
        }
    }

    private boolean voidNextToCreature(Direction direction) {
        return !(tiles[creature.position.y][creature.position.x + direction.getDirection().x] instanceof Obstacle || tiles[creature.position.y][creature.position.x + direction.getDirection().x] instanceof Border);
    }

    private boolean voidUnderCreature() {
        return !(tiles[creature.position.y + 1][creature.position.x] instanceof Obstacle || tiles[creature.position.y + 1][creature.position.x] instanceof Border);
    }

    private boolean checkVoid(Vector2i dst) {
        if (dst.y + 1 >= tiles.length) {
            return false;
        } else {
            return !(tiles[dst.y + 1][dst.x] instanceof Obstacle || tiles[dst.y][dst.x] instanceof Border);
        }
    }

    private boolean checkCollision(Vector2i dst) {
        return tiles[dst.y][dst.x] instanceof Obstacle || tiles[dst.y][dst.x] instanceof Border;
    }

    private void addGravity(Vector2i dst, Direction direction) {
        switch (direction) {
            case RIGHT, UP_RIGHT, DOWN_RIGHT -> {
                //System.out.println("passé dans le SWITCH");
                move(Direction.DOWN_RIGHT);
            }
            case UP_LEFT, LEFT, DOWN_LEFT -> move(Direction.DOWN_LEFT);
            case UP, DOWN -> move(Direction.DOWN);
            default -> System.out.println("ALERT DEFAULT");
        }
    }

    private void setCreaturePosition(Vector2i newPosition, Vector2i oldPosition) {
        creature.setPosition(newPosition);
        this.tiles[newPosition.y][newPosition.x] = creature;
        this.tiles[oldPosition.y][oldPosition.x] = new Tile(new Vector2i(oldPosition.x,
                                                                         oldPosition.y),
                                                            Colors.WHITE_BACKGROUND);

        if (showGrid) System.out.println(printGrid());
    }

    public void reset() {

        if(!creature.getPosition().equals(this.initialCreaturePosition)){
            setCreaturePosition(this.initialCreaturePosition, creature.getPosition());
            this.tiles[this.initialFlagPosition.x][this.initialFlagPosition.y] = new Flag(this.initialFlagPosition,
                                                                                          Colors.BLACK);
        }

        this.creature.resetTick();
        this.maxTicksReached = false;
        this.atDestination = false;;
    }

    private String printGrid() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < this.height; x++) {
            for (int y = 0; y < this.width; y++) {
                result.append(this.tiles[x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public String toString() {
        return printGrid();
    }

    public void init(String path) {
        this.tiles = new Tile[height][width];
        if (path.equals("random")) {
            addRandomTiles();
        } else {
            addTiles(readAllData(path));
        }
        System.out.println("INITIALISATION DE LA GRILLE");
        System.out.println(printGrid());
    }

    private void addRandomTiles() {
        for (int x = 0; x < this.height; x++) {
            for (int y = 0; y < this.width; y++) {
                if (x == 0 || y == 0 || x == this.height - 1 || y == this.width - 1) {
                    this.tiles[x][y] = new Obstacle(new Vector2i(x, y), Colors.BLACK);
                } else if (x == 1) {
                    this.tiles[x][y] = new Tile(new Vector2i(x, y), Colors.BLACK);
                } else if (x < this.height / 2) {
                    this.tiles[x][y] = getRandomMiddleTile(x, y, 0.1);
                } else {
                    Tile tile = getRandomMiddleTile(x, y, 0.3);
                    if (x > 2 && this.tiles[x - 1][y] instanceof Obstacle) {
                        this.tiles[x][y] = new Obstacle(new Vector2i(x, y), Colors.BLACK);
                    } else {
                        if (this.tiles[x][y] == null) {
                            this.tiles[x][y] = tile;
                        }
                    }
                }
            }
        }
        addFlag();
        addCreature();
    }

    private void addCreature() {
        for (int y = 0; y < this.width; y++) {
            for (int x = this.height - 1; x >= 1; x--) {
                if (!(this.tiles[x][y] instanceof Obstacle)) {
                    this.initialCreaturePosition = new Vector2i(x, y);
                    this.creature = new Creature(new Vector2i(x, y), Colors.YELLOW);
                    this.tiles[x][y] = creature;
                    return;
                }
            }
        }
    }

    private void addFlag() {
        for (int y = this.width - 1; y >= 1; y--) {
            for (int x = this.height - 1; x >= 1; x--) {
                if (!(this.tiles[x][y] instanceof Obstacle)) {
                    this.initialFlagPosition = new Vector2i(x, y);
                    this.tiles[x][y] = new Flag(new Vector2i(x, y), Colors.BLACK);
                    return;
                }
            }
        }
    }

    private Tile getRandomMiddleTile(int x, int y, double p) {
        return Math.random() < p
               ? new Obstacle(new Vector2i(x, y), Colors.BLACK)
               : new Tile(new Vector2i(x, y), Colors.WHITE_BACKGROUND);
    }

    private Tile getRandomBorderTile(int x, int y) {
        return Math.random() < 0.5
               ? new Border(new Vector2i(x, y), Colors.BLACK)
               : new Obstacle(new Vector2i(x, y), Colors.BLACK);
    }

    public Vector2i getCreaturePosition() {
        return creature.getPosition();
    }

    public Vector2i getFlagDestination() {
        return this.initialFlagPosition;
    }

    public int getTicksCreature() {
        return this.creature.getTick();
    }

    public int getDistanceBetweenCreatureAndFlag() {
        return Math.abs(creature.getPosition().x - this.initialFlagPosition.x) + Math.abs(creature.getPosition().y - this.initialFlagPosition.y);
    }
}
