package algorithm.selector;

import algorithm.fitness.Fitness;
import algorithm.model.Chromosome;
import algorithm.model.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class WheelSelector<G, S> extends Selector<G, S> {

    private TreeMap<Future<Double>, Chromosome<G>> mapAllFitness = new TreeMap<>();
    private ArrayList<Future<Double>> scores = new ArrayList<>();
    private final int nThreads;
    public WheelSelector(int nThreads, Fitness<G,S> fitness, S solution) throws ExecutionException, InterruptedException {
        super(fitness,solution);
        this.nThreads=nThreads;
    }

    public void computeAllFitness(Population<G> population) {
        final ExecutorService service = Executors.newFixedThreadPool(this.nThreads);
        for (Chromosome<G> c : population.getChromosomes()) {
            Future<Double> score = service.submit(()->fitness.getFitnessScore(c,solution));
            scores.add(score);
        }
    }
    public double getSumAllFitness() throws ExecutionException, InterruptedException {
        double sum = 0;
        for(Future<Double> score : scores){
            sum+=score.get();
        }
        return sum;
    }

    @Override
    public Chromosome<G> select(Population<G> population) throws ExecutionException, InterruptedException {
        /*double sumFitnessAllChromosomes = 0;
        for (Chromosome<G> c : population.getChromosomes()) {
            sumFitnessAllChromosomes += this.fitness.getFitnessScore(c, this.solution);
        }*/
        double r = Math.random() * getSumAllFitness();
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

