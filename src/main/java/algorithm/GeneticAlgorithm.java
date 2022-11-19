package algorithm;

import algorithm.builder.ChromosomeBuilder;
import algorithm.fitness.Fitness;
import algorithm.model.Chromosome;
import algorithm.model.Population;
import algorithm.selector.Selector;

/**
 * @param <G> type of one gene
 * @param <S> type of the solution
 */
public class GeneticAlgorithm<G, S> {
    private final int maxIterations;
    private final int populationSize;
    private final double crossoverRate;
    private final double mutationRate;
    //private final int nbCutPoints;
    private final ChromosomeBuilder<G> chromosomeBuilder;
    private final Fitness<G, S> fitness;
    private final S solution;
    private final int geneSize;
    //private final ArrayList<algorithm.model.Chromosome<G>> chromosomes;
    private Population<G> population = new Population<>();
    private final Selector<G,S> selector;


    public GeneticAlgorithm(int maxIterations,
                            int geneSize,
                            int populationSize,
                            double crossoverRate,
                            double mutationRate,
                            ChromosomeBuilder<G> chromosomeBuilder,
                            Fitness<G, S> fitness,
                            Selector<G,S> selector,
                            S solution) {
        this.maxIterations = maxIterations;
        this.geneSize = geneSize;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.chromosomeBuilder = chromosomeBuilder;
        this.fitness = fitness;
        this.solution = solution;
        this.selector = selector;
    }

    public void run() {
        population.init(this.populationSize, this.geneSize, this.chromosomeBuilder);
        int generationCount = 1;
        while (generationCount < maxIterations && Double.compare(fitness.getFittestScore(population,solution), 1)!=0) {
            System.out.println("Generation_" + generationCount + " " + fitness.getFittest(population,
                                                                                          solution) + " Score: " + fitness.getFittestScore(
                    population,
                    solution));
            this.population = evolvePopulation(this.population);
            generationCount++;
        }
        System.out.println("Solution found!");
        System.out.println("Generation_" + generationCount);
        System.out.println("algorithm.model.Chromosome : " + fitness.getFittest(population, solution));
        System.out.println("solution : " + solution);

    }

    private Population<G> evolvePopulation(Population<G> pop) {
        Population<G> newPopulation = new Population<>();

        newPopulation.addChromosome(fitness.getFittest(pop, solution));

        for (int i = 1; i < pop.getNbChromosomes(); i++) {
            Chromosome<G> parent1 = this.selector.select(population);
            Chromosome<G> parent2 = this.selector.select(population);
            Chromosome<G> child = crossover(parent1, parent2);
            newPopulation.addChromosome(child);
        }

        for (int i = 1; i < newPopulation.getNbChromosomes(); i++) {
            mutate(newPopulation.getChromosome(i));
        }

        return newPopulation;
    }





    private Chromosome<G> crossover(Chromosome<G> parent1, Chromosome<G> parent2) {
        Chromosome<G> child = new Chromosome<>();
        // TODO NbGenes
        for (int i = 0; i < parent1.getNbGenes(); i++) {
            child.addGene(Math.random() <= this.crossoverRate
                          ? parent1.getGene(i)
                          : parent2.getGene(i));
        }
        return child;
    }

    private void mutate(Chromosome<G> chromosome) {
        for (int i = 0; i < chromosome.getNbGenes(); i++) {
            if (Math.random() <= mutationRate) {
                G gene = chromosomeBuilder.build();
                chromosome.setGenes(i, gene);
            }
        }
    }

    @Override
    public String toString() {
        return "algorithm.GeneticAlgorithm{" + "populationSize=" + populationSize + ", crossoverRate=" + crossoverRate + ", mutationRate=" + mutationRate + '}';
    }
}
