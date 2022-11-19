package algorithm.fitness;

import algorithm.model.Chromosome;
import algorithm.model.Population;

public interface Fitness<G,S> {

    double getFitnessScore(Chromosome<G> chromosome, S solution);

    default Chromosome<G> getFittest(Population<G> population, S solution) {
       Chromosome<G> fittest = population.getChromosome(0);
        for (int i = 1; i < population.getNbChromosomes(); i++) {
            if (getFitnessScore(fittest, solution) <= getFitnessScore(population.getChromosome(i),
                                                                      solution)) {
                fittest = population.getChromosome(i);
            }

        }
        return fittest;
    }

    default double getFittestScore(Population<G> population, S solution){
        Chromosome<G> fittest = getFittest(population,solution);
        return getFitnessScore(fittest,solution);
    }
}
