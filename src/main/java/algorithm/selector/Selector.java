package algorithm.selector;

import algorithm.fitness.Fitness;
import algorithm.model.Chromosome;
import algorithm.model.Population;

public class Selector<G,S> implements Selection<G> {

    protected final Fitness<G,S> fitness;
    protected final S solution;

    public Selector(Fitness<G,S> fitness, S solution) {
        this.fitness = fitness;
        this.solution = solution;
    }
    @Override
    public Chromosome<G> select(Population<G> population) {
        return this.fitness.getFittest(population, this.solution);
    }
}