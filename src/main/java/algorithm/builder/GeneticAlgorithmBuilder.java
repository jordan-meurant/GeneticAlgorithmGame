package algorithm.builder;

import algorithm.GeneticAlgorithm;
import algorithm.fitness.Fitness;
import algorithm.selector.Selector;

public class GeneticAlgorithmBuilder<G, S> {
    private int maxIterations = 100;
    private int maxGeneSize = 10;
    private int minGeneSize = 0;


    private int populationSize = 25;
    private double crossoverRate = 0.9;
    private double mutationRate = 0.03;

    private ChromosomeBuilder<G> chromosomeBuilder;
    private Fitness<G, S> fitness;

    private S solution;

    private Selector<G, S> selector;

    public GeneticAlgorithmBuilder() {
    }

    public GeneticAlgorithm<G, S> buildGeneticAlgorithm() {
        if (chromosomeBuilder == null) {
            throw new NullPointerException("You must specify a chromosome Builder");
        }
        if (fitness == null) {
            throw new NullPointerException("You must specify a algorithm.fitness.Fitness");
        }
        if (solution == null) {
            throw new NullPointerException("You must specify a solution");
        }
        if (selector == null) {
            throw new NullPointerException("You must specify a selector");
        }

        return new GeneticAlgorithm<G, S>(maxIterations,
                                          maxGeneSize,
                                          minGeneSize,
                                          populationSize,
                                          crossoverRate,
                                          mutationRate,
                                          chromosomeBuilder,
                                          fitness,
                                          selector,
                                          solution);
    }

    public GeneticAlgorithmBuilder<G, S> populationSize(int populationSize) {
        this.populationSize = populationSize;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> crossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> maxGeneSize(int geneSize) {
        this.maxGeneSize = geneSize;
        return this;
    }public GeneticAlgorithmBuilder<G, S> minGeneSize(int geneSize) {
        this.minGeneSize = geneSize;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> mutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> maxIterations(int nbIterations) {
        this.maxIterations = nbIterations;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> chromosomesBuilder(ChromosomeBuilder<G> chromosomeBuilder) {
        this.chromosomeBuilder = chromosomeBuilder;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> fitness(Fitness<G, S> fitness) {
        this.fitness = fitness;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> solution(S solution) {
        this.solution = solution;
        return this;
    }

    public GeneticAlgorithmBuilder<G, S> selector(Selector<G, S> selector) {
        this.selector = selector;
        return this;
    }
}
