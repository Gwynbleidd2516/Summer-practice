package src.main;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import src.main.Algorithms.*;

public class JRootFrame extends JFrame {
    private ArrayList<Node> mNodes = new ArrayList<>();
    private ArrayList<Edge> mEdges = new ArrayList<>();
    TopologicalSort mTopologicalSort;
    JGraph mGraph;
    JGraphToolBar mToolBar;
    JPlayer mPlayer;

    public JRootFrame() {
        setTitle("Topological sort");
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(JRootFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());
        
        mGraph = new JGraph();
        mGraph.setGraph(mNodes, mEdges);
        mToolBar = new JGraphToolBar(this);
        mPlayer = new JPlayer(this);

        Container c = getContentPane();
        c.add(mGraph, BorderLayout.CENTER);
        c.add(mToolBar, BorderLayout.NORTH);
        c.add(mPlayer, BorderLayout.SOUTH);
        c.add(new JLegend(), BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setMethod(Method m) {
        mTopologicalSort = null;
        switch (m) {
            case QUEUE:
                mTopologicalSort = new TopologicalSortQueue(mNodes, mEdges);
                mPlayer.enable();
                mTopologicalSort.reset();
                mTopologicalSort.init();
                break;
            case STACK:
                mTopologicalSort = new TopologicalSortStack(mNodes, mEdges);
                mPlayer.enable();
                mTopologicalSort.reset();
                mTopologicalSort.init();
                break;
            case NONE:
                mPlayer.disable();
                break;
        }
        mGraph.repaint();
    }

    @Override
    public void repaint() {
        mGraph.repaint();
        if (!mTopologicalSort.isEnd()) {
            mPlayer.setResultText("Result: " + mTopologicalSort.getResult().toString());
        } else {
            mPlayer.setResultText("Result: " + mTopologicalSort.getResult().toString() + " Done!");
        }
        mPlayer.repaint();
    }

    public static void main(String args[]) {
        JRootFrame m = new JRootFrame();

        m.mNodes.add(new Node(100, 100, Color.WHITE));
        m.mNodes.add(new Node(300, 100, Color.WHITE));
        m.mNodes.add(new Node(200, 250, Color.WHITE));
        m.mNodes.add(new Node(400, 250, Color.WHITE));

        m.mEdges.add(new Edge(0, 1));
        m.mEdges.add(new Edge(1, 2));
        m.mEdges.add(new Edge(3, 1));
    }
}
