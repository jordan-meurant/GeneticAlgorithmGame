import algorithm.GeneticAlgorithm;
import algorithm.builder.ChromosomeBuilder;
import algorithm.builder.GeneticAlgorithmBuilder;
import algorithm.fitness.Fitness;
import algorithm.selector.TournamentSelector;

public class Main {
    public static void main(String[] args) {
        Grid grid = new Grid(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        grid.init(args[2]);

        System.out.println("Number of Command Line Argument = " + args.length);

        for (int i = 0; i < args.length; i++) {
          //  System.out.printf("Command Line Argument %d is %s%n", i, args[i]);
        }

        //moves(args[3],grid);



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
                .fitness(fitness)
                .solution(solution)
                .geneSize(solution.length())
                .chromosomesBuilder(chromosomeBuilder)
                .maxIterations(3000)
                .selector(new TournamentSelector<>(3, fitness, solution))
                .buildGeneticAlgorithm();
        algorithm.run();
    }

    private static void moves(String moves, Grid grid) {
        String[] movesArray = moves.split("");
        System.out.println("Moves : " + String.join("->",movesArray));
        for (String move : movesArray) {
            Direction direction  = determineMove(move);
            System.out.println("Move : " + direction.toString() );
            grid.move(direction);
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