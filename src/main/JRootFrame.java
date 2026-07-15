package src.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

// import src.main.Algorithms.TopologicalSort;
import src.main.Algorithms.*;

public class JRootFrame extends JFrame {
    private ArrayList<Node> mNodes = new ArrayList<>();
    private ArrayList<Edge> mEdges = new ArrayList<>();
    TopologicalSort mTopologicalSort;
    JGraph mGraph;
    JToolBar mToolBar;
    JToolBar mPlayer;
    JLabel mAnswear;

    public JRootFrame() {
        // mTopologicalSort = new TopologicalSortQueue(mNodes, mEdges);
        // mTopologicalSort.init();
        mGraph = new JGraph();
        mGraph.setGraph(mNodes, mEdges);

        mToolBar = new JToolBar();
        mToolBar.setFloatable(false);
        JButton open = new JButton("open");
        JButton save = new JButton("save");
        mToolBar.add(open);
        mToolBar.add(save);
        Method[] mMethods = { Method.QUEUE, Method.STACK, Method.NONE };
        JComboBox<Method> comboBox = new JComboBox<>(mMethods);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMethod((Method) comboBox.getSelectedItem());
            }
        });
        mToolBar.add(comboBox);

        mPlayer = new JToolBar();
        mPlayer.setFloatable(false);

        JButton stepB = new JButton("<");
        stepB.addActionListener(e -> {
            if (!mTopologicalSort.isBegin()) {
                mTopologicalSort.stepBack();
            }
            mGraph.repaint();
            repaint();
        });
        mPlayer.add(stepB);

        JButton stepS = new JButton("Stop");
        mPlayer.add(stepS);
        JButton stepF = new JButton(">");
        stepF.addActionListener(e -> {
            if (!mTopologicalSort.isEnd()) {
                mTopologicalSort.stepForward();
            }
            mGraph.repaint();
            repaint();
        });
        mPlayer.add(stepF);

        mAnswear = new JLabel("Result: ");
        mPlayer.add(mAnswear);

        setTitle("Lessoon");
        setSize(new Dimension(800, 600));
        setDefaultCloseOperation(JRootFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        Container c = getContentPane();

        c.add(mGraph, BorderLayout.CENTER);
        c.add(mToolBar, BorderLayout.NORTH);
        c.add(mPlayer, BorderLayout.SOUTH);

        JPanel legendPanel = new JPanel();
        legendPanel.setLayout(new BoxLayout(legendPanel, BoxLayout.Y_AXIS));
        legendPanel.setBorder(BorderFactory.createTitledBorder("Legend")); // Border titled "Legend"

        // Add legend items
        legendPanel.setPreferredSize(new Dimension(150, 0));
        legendPanel.add(createLegendItem(Color.BLACK, "Обработано"));
        legendPanel.add(createLegendItem(Color.GRAY, "В обработке"));
        legendPanel.add(createLegendItem(Color.WHITE, "Ещё не тронутые"));

        add(legendPanel, BorderLayout.EAST);

        setLocationRelativeTo(null);
        setVisible(true);

        comboBox.setSelectedItem(Method.NONE);
    }

    private static JPanel createLegendItem(Color color, String text) {
        JPanel itemPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create a 16x16 colored square icon
        Icon colorIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                g.setColor(color);
                g.fillRect(x, y, getIconWidth(), getIconHeight());
            }

            @Override
            public int getIconWidth() {
                return 16;
            }

            @Override
            public int getIconHeight() {
                return 16;
            }
        };

        // Combine icon and text into a JLabel
        JLabel label = new JLabel(text, colorIcon, JLabel.LEFT);
        itemPanel.add(label);
        return itemPanel;
    }

    public void setMethod(Method m) {
        switch (m) {
            case QUEUE:
                mTopologicalSort = new TopologicalSortQueue(mNodes, mEdges);
                break;
            case STACK:
                mTopologicalSort = new TopologicalSortStack(mNodes, mEdges);
                break;
            default:
                mTopologicalSort = null;
                return;
        }
        mTopologicalSort.reset();
        mTopologicalSort.init();
        mGraph.repaint();
    }

    @Override
    public void repaint() {
        if (!mTopologicalSort.isEnd()) {
            mAnswear.setText("Result: " + mTopologicalSort.getResult().toString());
        } else {
            mAnswear.setText("Result: " + mTopologicalSort.getResult().toString() + " Done!");
        }
        super.repaint();
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
