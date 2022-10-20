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
        addTiles(readAllData("C:\\Users\\jorda\\Desktop\\GeneticAlgorithmGame\\src\\main\\java\\env.csv"));
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
            case 0 -> newTile = new Tile(new Vector2i(x, y), Colors.WHITE_BACKGROUND);
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

    public String move(Direction direction) {
        Vector2i dst = creature.getPosition().add(direction.getDirection());
        Tile dstTile = tiles[dst.y][dst.x];
        if (dstTile instanceof Obstacle) {
            return "OBSTACLE";
        } else {
            return "NOTHING";
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int x = 0; x < this.height; x++) {
            for (int y = 0; y < this.width; y++) {
                result.append(this.tiles[x][y]);
            }
            result.append("\n");
        }
        return result.toString();
    }
}
