package algorithm.model;

import algorithm.builder.ChromosomeBuilder;

import java.util.ArrayList;

public class Population<G> {

    private final ArrayList<Chromosome<G>> chromosomes;

    public Population(){
        chromosomes = new ArrayList<Chromosome<G>>();
    }

    public void init(int populationSize, int geneSize, ChromosomeBuilder<G> chromosomeBuilder){
        for (int i =0; i < populationSize; i++){
                Chromosome<G> chromosome = new Chromosome<>();
                chromosome.init(geneSize, chromosomeBuilder);
                this.chromosomes.add(chromosome);
        }
    }

    public ArrayList<Chromosome<G>> getChromosomes() {
        return chromosomes;
    }

    public void addChromosome(Chromosome<G> chromosome){
        this.chromosomes.add(chromosome);
    }

    public Chromosome<G> getChromosome(int index){
        return this.chromosomes.get(index);
    }

    public int getNbChromosomes(){
        return this.chromosomes.size();
    }

    @Override
    public String toString() {
        return "algorithm.model.Population{" +
                "chromosomes=" + chromosomes +
                '}';
    }
}
