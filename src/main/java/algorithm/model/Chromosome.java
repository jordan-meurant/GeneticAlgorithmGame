package algorithm.model;

import algorithm.builder.ChromosomeBuilder;

import java.util.ArrayList;

public class Chromosome<G> {

    private ArrayList<G> genes = new ArrayList<>();

    public Chromosome() {
    }

    public Chromosome(ArrayList<G> genes) {
        this.genes = genes;
    }

    public void init(int geneSize, ChromosomeBuilder<G> chromosomeBuilder) {
        this.genes = chromosomeBuilder.buildList(geneSize);
    }

    public void setGenes(int index, G gene) {
        this.genes.set(index, gene);
    }

    public int getNbGenes(){
        return this.genes.size();
    }

    public G getGene(int index){
        return this.genes.get(index);
    }

    public void addGene(G gene){
        this.genes.add(gene);
    }

    public ArrayList<G> getGenes(){
        return this.genes;
    }

    @Override
    public String toString() {
        return "algorithm.model.Chromosome{" +
                "genes=" + genes +
                '}';
    }
}
