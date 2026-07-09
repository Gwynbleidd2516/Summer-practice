package src.main.Algorithms;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import src.main.Edge;
import src.main.Node;
import java.awt.Color;

public class TopologicalSortQueue extends TopologicalSort {

    private Queue<Integer> mQueue;
    private ArrayList<Integer> mInDegree;

    public TopologicalSortQueue(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        super(nodes, edges);
        mQueue = new LinkedList<Integer>();
        mInDegree = new ArrayList<>(mNodes.size());
    }

    @Override
    public void init() {
        for (Node iterable : mNodes) {
            iterable.mColor = Color.WHITE;
        }
        for (int i = 0; i < mNodes.size(); i++) {
            mInDegree.add(0);
            for (Edge edge : mEdges) {
                if (edge.v == i) {
                    mInDegree.set(i, mInDegree.get(i) + 1);
                }
            }
        }
        for (int i = 0; i < mNodes.size(); i++) {
            if (mInDegree.get(i) == 0) {
                mQueue.offer(i);
                mNodes.get(i).mColor = Color.gray;
            }
        }
    }

    @Override
    public void stepForward() {
        int u = mQueue.poll();
        mResult.add(u);
        mNodes.get(u).mColor = Color.BLACK;
        for (Edge it : mEdges) {
            if (it.u == u) {
                mInDegree.set(it.v, mInDegree.get(it.v) - 1);
                if (mInDegree.get(it.v) == 0) {
                    mQueue.offer(it.v);
                    mNodes.get(it.v).mColor = Color.gray;
                }
            }
        }
    }

    @Override
    public void stepBack() {
        int u = mResult.get(mResult.size() - 1);
        mResult.remove(mResult.size() - 1);

        for (Edge edge : mEdges) {
            if (edge.u == u) {
                int v = edge.v;
                int newDegree = mInDegree.get(v) + 1;
                mInDegree.set(v, newDegree);

                if (newDegree == 1) {
                    ((LinkedList<Integer>) mQueue).removeFirstOccurrence(v);
                    mNodes.get(v).mColor = Color.WHITE;
                }
            }
        }

        ((LinkedList<Integer>) mQueue).addFirst(u);
        mNodes.get(u).mColor = Color.GRAY;
    }

    @Override
    public void reset() {
        mQueue.clear();
        mResult.clear();
    }

    @Override
    public Boolean isEnd() {
        return mResult.size() == mNodes.size();
    }

    @Override
    public Boolean isBegin() {
        return mResult.isEmpty();
    }

}