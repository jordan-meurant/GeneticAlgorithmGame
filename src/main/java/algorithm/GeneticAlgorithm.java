package algorithm;

import algorithm.builder.ChromosomeBuilder;
import algorithm.fitness.Fitness;
import algorithm.model.Chromosome;
import algorithm.model.Population;
import algorithm.selector.Selector;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

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
    private final int maxGeneSize;
    private final int minGeneSize;
    private final Selector<G, S> selector;
    //private final ArrayList<algorithm.model.Chromosome<G>> chromosomes;
    private Population<G> population = new Population<>();


    public GeneticAlgorithm(int maxIterations,
                            int maxGeneSize,
                            int minGeneSize,
                            int populationSize,
                            double crossoverRate,
                            double mutationRate,
                            ChromosomeBuilder<G> chromosomeBuilder,
                            Fitness<G, S> fitness,
                            Selector<G, S> selector,
                            S solution) {
        this.maxIterations = maxIterations;
        this.minGeneSize = minGeneSize;
        this.maxGeneSize = maxGeneSize;
        this.populationSize = populationSize;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.chromosomeBuilder = chromosomeBuilder;
        this.fitness = fitness;
        this.solution = solution;
        this.selector = selector;
    }

    public void run() throws ExecutionException, InterruptedException {
        population.init(this.populationSize,
                        this.minGeneSize,
                        this.maxGeneSize,
                        this.chromosomeBuilder);
        selector.computeAllFitness(this.population);
        int iterationCount = 1;
        while (iterationCount < maxIterations && Double.compare(fitness.getFittestScore(population,
                                                                                        solution),
                                                                1) != 0) {

            Chromosome<G> fittest = fitness.getFittest(population, solution);
            System.out.println("Iter_" + iterationCount + "\t"+ "NbGenes: "+fittest.getNbGenes()  + " Score: " + fitness.getFittestScore(population, solution)+ " GenesFittest" + fittest.getGenes());
            this.population = evolvePopulation(this.population);
            iterationCount++;
        }
        System.out.println("Solution found!");
        System.out.println("iter_" + iterationCount);
        System.out.println("Genes of fittest chromosome : " + fitness
                .getFittest(population, solution)
                .getGenes());
        System.out.println("solution : " + solution);

    }

    private Population<G> evolvePopulation(Population<G> pop) throws ExecutionException, InterruptedException {
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
        for (int i = 0; i < parent1.getNbGenes() && i < parent2.getNbGenes(); i++) {
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

    public ArrayList<G> getGenesSolution() {
        Chromosome<G> fittest = fitness.getFittest(population, solution);
        return fittest.getGenes();
    }
}
