package src.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.Statement;
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
    JButton stepB;
    JButton stepS;
    JButton stepF;
    Timer mPlayertimer;
    JLabel mAnswear;

    public JRootFrame() {
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
                setMethod((Method) comboBox.getSelectedItem(), comboBox);
            }
        });
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                switch (((Method) value)) {
                    case QUEUE:
                        setText("Queue method");
                        break;
                    case STACK:
                        setText("Stack method");
                        break;
                    case NONE:
                        setText("Choose method!");
                        break;

                    default:
                        break;
                }
                return this;
            }
        });
        mToolBar.add(comboBox);

        mPlayer = new JToolBar();
        mPlayer.setFloatable(false);

        stepB = new JButton("<");
        stepB.addActionListener(e -> {
            if (!mTopologicalSort.isBegin()) {
                mTopologicalSort.stepBack();
            }
            repaint();
        });
        mPlayer.add(stepB);

        stepS = new JButton("Stop");
        mPlayertimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!mTopologicalSort.isEnd()) {
                    mTopologicalSort.stepForward();
                }
                else {
                    mPlayertimer.stop();
                    mPlayertimer.restart();
                }
                mGraph.repaint();
            }
        });
        stepS.addActionListener(e -> {
            if (mPlayertimer.isRunning()) {
                stepS.setText("Stop");
                mPlayertimer.stop();
            } else {
                stepS.setText("Resume");
                mPlayertimer.start();
            }
        });
        mPlayer.add(stepS);
        stepF = new JButton(">");
        stepF.addActionListener(e -> {
            if (!mTopologicalSort.isEnd()) {
                mTopologicalSort.stepForward();
            }
            repaint();
        });
        mPlayer.add(stepF);

        mAnswear = new JLabel("Result: ");
        mPlayer.add(mAnswear);

        setTitle("Topological sort");
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

    public void setMethod(Method m, JComboBox comboBox) {
        mTopologicalSort = null;
        switch (m) {
            case QUEUE:
                mTopologicalSort = new TopologicalSortQueue(mNodes, mEdges);
                stepB.setEnabled(true);
                stepS.setEnabled(true);
                stepF.setEnabled(true);
                mTopologicalSort.reset();
                mTopologicalSort.init();
                break;
            case STACK:
                mTopologicalSort = new TopologicalSortStack(mNodes, mEdges);
                stepB.setEnabled(true);
                stepS.setEnabled(true);
                stepF.setEnabled(true);
                mTopologicalSort.reset();
                mTopologicalSort.init();
                break;
            case NONE:
                stepB.setEnabled(false);
                stepS.setEnabled(false);
                stepF.setEnabled(false);
                break;
        }
        mGraph.repaint();
    }

    @Override
    public void repaint() {
        mGraph.repaint();
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
