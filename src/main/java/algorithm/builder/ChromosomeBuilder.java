package algorithm.builder;

import java.util.ArrayList;

public interface ChromosomeBuilder<G> {

    default ArrayList<G> buildList(int minGeneSize, int maxGeneSize) {
        int r = (int) (Math.random() * (maxGeneSize - minGeneSize)) + minGeneSize;
        ArrayList<G> list = new ArrayList<>();
        for (int i = 0; i < r; i++) {
            list.add(build());
        }
        return list;
    }

    /**
     * Build a gene
     *
     * @return  <G> a gene
     */
    G build();
}
