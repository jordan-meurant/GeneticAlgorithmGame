import algorithm.GeneticAlgorithm;
import algorithm.builder.ChromosomeBuilder;
import algorithm.builder.GeneticAlgorithmBuilder;
import algorithm.fitness.Fitness;
import algorithm.selector.WheelSelector;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int nbThreads = Integer.parseInt(args[0]);
        int width = Integer.parseInt(args[1]);
        int height = Integer.parseInt(args[2]);
        int nbCreatures = Integer.parseInt(args[3]);
        double crossRate = Double.parseDouble(args[4]);
        double mutationRate = Double.parseDouble(args[5]);
        int maxTicks = Integer.parseInt(args[6]);
        String path = "random";
        if (args.length == 8) {
            path = args[7];
        }
        Grid grid = new Grid(width, height, maxTicks);
        grid.init(path);


        // Paramètres algorithme
        ChromosomeBuilder<Direction> directions = () -> {
            int random = (int) (Math.random() * 8);
            return determineMove(String.valueOf(random));
        };

        Vector2i s = grid.getFlagDestination();
        double initialDistance = grid.getDistanceBetweenCreatureAndFlag();
        Fitness<Direction, Vector2i> fitness = (chromosome, solution) -> {
            int moves = 1;
            double distanceScore = 0;
            double movesScore = 0;
            double ticksScore = 1;

            for (int i = 0; i < chromosome.getNbGenes(); i++) {
                int distance = grid.getDistanceBetweenCreatureAndFlag();
                int ticks = grid.getTicksCreature();
                distanceScore += 1 - ((initialDistance - Math.abs(initialDistance - distance)) / initialDistance);
                movesScore += 1 - ((chromosome.getNbGenes() - Math.abs(chromosome.getNbGenes() - moves)) / chromosome.getNbGenes());
                ticksScore += 1 - ((maxTicks - Math.abs(maxTicks - ticks)) / maxTicks);
                if (grid.isMaxTicksReached()){
                    ticksScore = 0;
                    break;
                }else{
                    if (grid.isAtDestination()){
                        distanceScore = chromosome.getNbGenes();
                        break;
                    }else {
                        grid.move(chromosome.getGene(i));
                        moves++;
                    }
                }
            }

            distanceScore /= chromosome.getNbGenes();
            movesScore /= chromosome.getNbGenes();
            ticksScore /= chromosome.getNbGenes();

            grid.reset();
            return (distanceScore + movesScore + ticksScore) / 3;
        };

        GeneticAlgorithm<Direction, Vector2i> algorithm = new GeneticAlgorithmBuilder<Direction, Vector2i>()
                .fitness(fitness)
                .solution(s)
                .maxGeneSize(15)
                .minGeneSize(10)
                .populationSize(50)
                .chromosomesBuilder(directions)
                .maxIterations(5000)
                .selector(new WheelSelector<>(nbThreads,fitness, s))
                .buildGeneticAlgorithm();

        algorithm.run();


        moves(algorithm.getGenesSolution(),grid);

    }

    private static void movesWithStrings(String moves, Grid grid) {
        String[] movesArray = moves.split("");
        System.out.println("Moves : " + String.join("->", movesArray));
        grid.reset();
        grid.setShowGrid(true);
        ArrayList<Direction> directions = new ArrayList<>();
        for (String move : movesArray) {
            Direction direction = determineMove(move);
            directions.add(direction);
        }
        moves(directions, grid);
    }

    private static void moves(ArrayList<Direction> directions, Grid grid) {
        grid.reset();
        grid.setShowGrid(true);
        for (Direction d : directions) {
            System.out.println("Move : " + d);
            if (grid.isMaxTicksReached()) {
                System.out.println("Nombre de ticks maximum atteint");
                return;
            } else {
                if (grid.isAtDestination()) {
                    System.out.println("La créature est arrivé à destination");
                    return;
                } else {
                    grid.move(d);
                }
            }
        }
    }

    private static Direction determineMove(String move) {
        return switch (move) {
            case "1" -> Direction.DOWN_RIGHT;
            case "2" -> Direction.RIGHT;
            case "3" -> Direction.UP_RIGHT;
            case "4" -> Direction.UP;
            case "5" -> Direction.UP_LEFT;
            case "6" -> Direction.LEFT;
            case "7" -> Direction.DOWN_LEFT;
            default -> Direction.DOWN;
        };
    }

}

// 10 4 5 env.txt 231235
// w h env.txt maxtick moves