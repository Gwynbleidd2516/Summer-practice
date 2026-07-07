package src.main.Algorithms;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import src.main.Edge;
import src.main.Node;

public class TopologicalSortStack extends TopologicalSort {
    private enum Statement {
        WHITE,
        GRAY,
        BLACK
    }
    
    private ArrayList<Integer> mVisited;
    private ArrayList<Statement> mColors;
    private Stack<Integer> mStack;

    public TopologicalSortStack(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        super(nodes, edges);
        mVisited = new ArrayList<>(mVisited.size());
        mColors = new ArrayList<>(mVisited.size());
        mStack=new Stack<>();
    }

    @Override
    public void init() {
        for (int i = 0; i < mNodes.size(); i++) {
            mVisited.add(0);
            mColors.add(Statement.WHITE);
            mNodes.get(i).mColor = Color.WHITE;
        }
    }

    @Override
    public void stepForward() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stepForward'");
    }

    @Override
    public void stepBack() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'stepBack'");
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }

    @Override
    public Boolean isEnd() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isEnd'");
    }

    @Override
    public Boolean isBegin() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isBegin'");
    }

}
