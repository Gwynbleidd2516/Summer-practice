package src.main;

import java.io.Serializable;

public class Edge implements Serializable {
    public Integer u, v;

    Edge() {
    }

    Edge(Integer u, Integer v) {
        this.u = u;
        this.v = v;
    }
}
