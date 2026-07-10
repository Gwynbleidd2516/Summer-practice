package src.main;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    readFromFile(fileChooser.getSelectedFile());
                    mGraph.repaint();
                }
            }
        });

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
            } else
                mTopologicalSort.reset();
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

    public void readEdges(String[] edgeStrings) {
        for (int i = 1; i < edgeStrings.length; i++) {
            String[] b = edgeStrings[i].split(" ");
            if (b.length != 2) {
                throw new RuntimeException("Wrong syntaxis: not 2 numbers in edge");
            }
            mEdges.add(new Edge(Integer.parseInt(b[0].trim()), Integer.parseInt(b[1].trim())));
        }
        mEdges.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge arg0, Edge arg1) {
                if (arg0.u > arg1.u)
                    return 1;
                else if (arg0.u < arg1.u)
                    return -1;
                else {
                    if (arg0.v > arg1.v) {
                        return 1;
                    } else
                        return -1;
                }
            }
        });
    }

    public void readNodes(int nodeCapasity) {
    }

    public void readFromFile(File file) {
        ArrayList<Node> buffNodes = (ArrayList<Node>) mNodes.clone();
        ArrayList<Edge> buffEdges = (ArrayList<Edge>) mEdges.clone();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            mNodes.clear();
            mEdges.clear();
            byte[] buff = new byte[1024 * 10];
            fileInputStream.read(buff);
            String[] massStr = new String(buff).trim().split("\n");
            buff = null;
            Integer nodeSise = Integer.valueOf(massStr[0].trim());
            readEdges(massStr);
            readNodes(nodeSise);
        } catch (Exception e) {
            System.out.println(e);
            mNodes.clear();
            mNodes.addAll(buffNodes);
            mEdges.clear();
            mEdges.addAll(buffEdges);
        }
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
    }
}
