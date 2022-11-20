package algorithm.selector;

import algorithm.model.Chromosome;
import algorithm.model.Population;

import java.util.concurrent.ExecutionException;

public interface Selection<G> {

    Chromosome<G> select(Population<G> population) throws ExecutionException, InterruptedException;
}
