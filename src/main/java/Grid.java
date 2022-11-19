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
    private final Tile[][] tiles;
    private Creature creature;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.tiles = new Tile[height][width];
        //addTiles(readAllData("/Users/jordanmeurant/IdeaProjects/GeneticAlgorithmGame/src/main/java/env.csv"));
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

    private Tile addTile(int x, int y, int state) {
        Tile newTile;
        switch (state) {
            //case 0 -> newTile = new Tile(new Vector2i(x, y), Colors.WHITE_BACKGROUND);
            case 1 -> newTile = new Obstacle(new Vector2i(x, y), Colors.BLACK);
            case 2 -> {
                this.creature = new Creature(new Vector2i(x, y), Colors.YELLOW);
                newTile = creature;
            }
            default -> newTile = new Tile(new Vector2i(x, y), Colors.WHITE_BACKGROUND);
        }
        return newTile;
    }

    private void addTiles(List<String[]> data) {
        for (int x = 0; x < data.size(); x++) {
            for (int y = 0; y < data.get(x).length; y++) {
                this.tiles[x][y] = addTile(x, y, Integer.parseInt(data.get(x)[y]));
            }
        }
    }

    public void move(Direction direction) {
        creature.incrementTick();
        //System.out.println(creature.getTick());

        //System.out.println(tick);
        Vector2i dst = new Vector2i(creature.getPosition());
        dst.add(direction.getDirection());

        Tile dstTile = tiles[dst.y][dst.x];

        if (!checkColision(dst)) {
            if (checkVoid(dst)) {
                setCreaturePosition(dst, creature.getPosition());
                addGravity(dst, direction);
            } else {
                //System.out.println("PREMIER FINI");
                setCreaturePosition(dst, creature.getPosition());
            }
        } else {
            //setCreaturePosition(dst, creature.getPosition());
            if (!checkVoid(dst)) {
                if (voidUnderCreature()) {
                    move(Direction.DOWN);
                } /*else if (voidNextToCreature(direction)) {
                    switch (direction) {
                        case UP_RIGHT -> {
                            move(Direction.RIGHT);
                        }
                        case UP_LEFT-> move(Direction.LEFT);
                    }
                    System.out.println(printGrid());
                }*/ else {
                    System.out.println("2e FINI");
                    System.out.println(printGrid());
                }
            } else {
                System.out.println("3e FINI");
                //setCreaturePosition(dst, creature.getPosition());
                switch (direction) {
                    case UP_RIGHT -> move(Direction.RIGHT);
                    case UP_LEFT -> move(Direction.LEFT);
                }
            }
        }
    }

    private boolean voidNextToCreature(Direction direction) {
        return !(tiles[creature.position.y][creature.position.x + direction.getDirection().x] instanceof Obstacle);
    }

    private boolean voidUnderCreature() {
        return !(tiles[creature.position.y + 1][creature.position.x] instanceof Obstacle);
    }

    private boolean checkVoid(Vector2i dst) {
        if (dst.y + 1 >= tiles.length) {
            return false;
        } else {
            return !(tiles[dst.y + 1][dst.x] instanceof Obstacle);
        }
        //boolean x = !(tiles[dst.y + 1][dst.x] instanceof Obstacle);
        //System.out.println("Vide : " + x);
    }

    private boolean checkColision(Vector2i dst) {
        //boolean x = tiles[dst.y][dst.x] instanceof Obstacle;
        //System.out.println("Colision : " + x);
        return tiles[dst.y][dst.x] instanceof Obstacle;
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

    public void setCreaturePosition(Vector2i newPosition, Vector2i oldPosition) {
        creature.setPosition(newPosition);
        this.tiles[newPosition.y][newPosition.x] = creature;
        this.tiles[oldPosition.y][oldPosition.x] = new Tile(new Vector2i(oldPosition.x, oldPosition.y),
                                                            Colors.WHITE_BACKGROUND);
        System.out.println(printGrid());
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
        addTiles(readAllData(path));
        System.out.println("INITIALISATION DE LA GRILLE");
        System.out.println(printGrid());
    }
}
