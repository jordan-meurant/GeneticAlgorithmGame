package algorithm.selector;

import algorithm.fitness.Fitness;
import algorithm.model.Chromosome;
import algorithm.model.Population;

public class TournamentSelector<G, S> extends Selector<G, S> implements Selection<G> {

    private final int sizeTournament;

    public TournamentSelector(int sizeTournament, Fitness<G,S> fitness, S solution) {
        super(fitness, solution);
        this.sizeTournament = sizeTournament;
    }

    @Override
    public Chromosome<G> select(Population<G> population) {
        Population<G> tournament = new Population<>();
        for (int i = 0; i < this.sizeTournament; i++) {
            int randomIndex = (int) (Math.random() * this.sizeTournament);
            Chromosome<G> selectedChromosome = population.getChromosome(randomIndex);
            tournament.addChromosome(selectedChromosome);
        }

        return this.fitness.getFittest(tournament, this.solution);
    }
}
