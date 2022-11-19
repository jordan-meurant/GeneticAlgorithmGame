package algorithm.selector;

import algorithm.model.Chromosome;
import algorithm.model.Population;

public interface Selection<G> {

    Chromosome<G> select(Population<G> population) ;
}
