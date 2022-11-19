package algorithm.builder;

import java.util.ArrayList;

public interface ChromosomeBuilder<G> {

    default ArrayList<G> buildList(int geneSize) {
        ArrayList<G> list = new ArrayList<>();
        for (int i = 0; i < geneSize; i++) {
            list.add(build());
        }
        return list;
    }

    G build();
}
