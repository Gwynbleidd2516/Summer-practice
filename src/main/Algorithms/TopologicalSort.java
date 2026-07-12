package src.main.Algorithms;

import java.io.Serializable;
import java.util.ArrayList;

import src.main.Edge;
import src.main.Node;

public abstract class TopologicalSort implements Serializable {
    protected transient ArrayList<Node> mNodes;
    protected transient ArrayList<Edge> mEdges;
    protected ArrayList<Integer> mResult;

    public TopologicalSort(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        mNodes = nodes;
        mEdges = edges;
        mResult = new ArrayList<>();
    }

    public TopologicalSort() {
        mResult = new ArrayList<>();        
    }

    public void setGraph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        mNodes = nodes;
        mEdges = edges;
    }

    public abstract void init();

    public abstract void stepForward();

    public abstract void stepBack();

    public abstract void reset();

    public abstract Boolean isEnd();

    public abstract Boolean isBegin();

    public ArrayList<Integer> getResult() {
        return mResult;
    }
}
