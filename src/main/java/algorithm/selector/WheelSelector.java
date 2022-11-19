package algorithm.selector;

import algorithm.fitness.Fitness;
import algorithm.model.Chromosome;
import algorithm.model.Population;


public class WheelSelector<G, S> extends Selector<G, S> {


    public WheelSelector(Fitness<G,S> fitness, S solution) {
        super(fitness,solution);
    }



    @Override
    public Chromosome<G> select(Population<G> population) {
        double sumFitnessAllChromosomes = 0;
        for (Chromosome<G> c : population.getChromosomes()) {
            sumFitnessAllChromosomes += this.fitness.getFitnessScore(c, this.solution);
        }
        double r = Math.random() * sumFitnessAllChromosomes;
        double sumFitness = 0;
        int index = 0;
        for (int i = 0; i < population.getNbChromosomes(); i++) {
            if (Double.compare(sumFitness, r) >= 0) {
                index = i;
                break;
            } else {
                sumFitness += this.fitness.getFitnessScore(population.getChromosome(i),
                                                           this.solution);
            }
        }
        return population.getChromosome(index);
    }
}

