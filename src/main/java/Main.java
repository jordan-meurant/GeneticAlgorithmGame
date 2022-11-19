import algorithm.GeneticAlgorithm;
import algorithm.builder.ChromosomeBuilder;
import algorithm.builder.GeneticAlgorithmBuilder;
import algorithm.fitness.Fitness;
import algorithm.selector.TournamentSelector;

public class Main {
    public static void main(String[] args) {

        int nbThreads = Integer.parseInt(args[0]);
        int width = Integer.parseInt(args[1]);
        int height = Integer.parseInt(args[2]);
        int nbCreatures = Integer.parseInt(args[3]);
        double crossRate = Double.parseDouble(args[4]);
        double mutationRate = Double.parseDouble(args[5]);
        int maxTicks = Integer.parseInt(args[6]);
        String path="random";
        if (args.length == 8){
            path = args[7];
        }
        String moves = "1233322223332333";




        Grid grid = new Grid(width, height,maxTicks);
        grid.init(path);
        // moves
        moves(moves,grid);


        ChromosomeBuilder<Character> chromosomeBuilder = () -> Math.random() < 0.5 ? '0' : '1';
        Fitness<Character, String> fitness = (chromosome, solution) -> {
            int score = 0;
            for (int i = 0; i < chromosome.getNbGenes() && i < solution.length(); i++) {
                if (chromosome.getGene(i) == solution.charAt(i)) {
                    score++;
                }
            }
            int a = chromosome.getNbGenes();
            return (double) score/chromosome.getNbGenes();
        };
        String solution = "1011000100000100010000100000100111001000000100000100000000001111";
        System.out.println("My solution : " + solution);


        GeneticAlgorithm<Character, String> algorithm = new GeneticAlgorithmBuilder<Character, String>()
                .populationSize(50)
                .crossoverRate(0.3)
                .mutationRate(0.1)
                .fitness(fitness)
                .solution(solution)
                .geneSize(solution.length())
                .chromosomesBuilder(chromosomeBuilder)
                .maxIterations(3000)
                .selector(new TournamentSelector<>(3, fitness, solution))
                .buildGeneticAlgorithm();
       // algorithm.run();
    }

    private static void moves(String moves, Grid grid) {
        String[] movesArray = moves.split("");
        System.out.println("Moves : " + String.join("->",movesArray));
        for (String move : movesArray) {
            Direction direction  = determineMove(move);
            System.out.println("Move : " + direction.toString() );
            if (grid.isMaxTicksReached()){
                System.out.println("Nombre de ticks maximum atteint");
                return;
            }else {
                if(grid.isAtDestination()){
                    System.out.println("La créature est arrivé à destination");
                    return;
                }else{
                    grid.move(direction);
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