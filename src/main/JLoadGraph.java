package src.main;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.Point;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileNameExtensionFilter;

public class JLoadGraph extends JFrame {
    private enum GraphType {
        EDGE_LIST,
        ADJACENCY_MATRIX,
        INCIDENCE_MATRIX,
        ADJACENCY_LIST
    }

    private JLabel mNote;

    private final String edge_list = "<html>Construct a simple collection (array/list) of all edges through breaklines with a number of nodes at the first line. Each entry in the list must contain the two endpoint vertices.</html>";
    private final String adjacency_matrix = "<html>Define a square matrix where rows and columns represent vertices. Mark 1 (or the edge weight) at the intersection if an edge exists between vertex i (row) and vertex j (column); otherwise, mark 0. For undirected graphs, the matrix is symmetric across the diagonal.</html>";
    private final String incidence_matrix = "<html>Define a matrix where rows represent vertices and columns represent edges. For each edge column, mark 1 for the starting vertex, -1 for the ending vertex (in directed graphs), or 1 for both endpoints (in undirected graphs). Mark 0 for all other vertices.</html>";
    private final String adjacency_list = "<html>Create a list for every vertex. For each edge, add the neighboring vertex (or the edge's weight) to the list of the starting vertex. This structure efficiently stores sparse graphs by only mapping directly connected nodes.</html>";

    public JLoadGraph(JRootFrame frame) {
        mNote = new JLabel();

        JToolBar menuItem = new JToolBar();
        menuItem.setFloatable(false);
        menuItem.setMargin(new Insets(10, 10, 0, 10));
        // menuItem.setSize(new Dimension(150, 200));
        // menuItem.setLocation(50, 0);
        menuItem.setLayout(new BoxLayout(menuItem, BoxLayout.Y_AXIS));

        Button button;
        button = new Button("Edge list");
        button.addActionListener(e -> {
            loadGraph(frame, GraphType.EDGE_LIST);
        });
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mNote.setText(edge_list);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mNote.setText("");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        menuItem.add(button);
        button = new Button("Adjacency matrix");
        button.addActionListener(e -> {
            loadGraph(frame, GraphType.ADJACENCY_MATRIX);
        });
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mNote.setText(adjacency_matrix);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mNote.setText("");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        menuItem.add(button);
        button = new Button("Incidence matrix");
        button.addActionListener(e -> {
            loadGraph(frame, GraphType.INCIDENCE_MATRIX);
        });
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mNote.setText(incidence_matrix);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mNote.setText("");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        menuItem.add(button);
        button = new Button("Adjacency list");
        button.addActionListener(e -> {
            loadGraph(frame, GraphType.ADJACENCY_LIST);
        });
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mNote.setText(adjacency_list);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mNote.setText("");
            }

            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
            }

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        menuItem.add(button);

        add(menuItem, BorderLayout.NORTH);
        add(mNote, BorderLayout.SOUTH);

        setSize(new Dimension(300, 250));
        setLocationRelativeTo(frame);
        setResizable(false);
        setTitle("Select a type of graph");
        setVisible(true);
    }

    private void loadGraph(JRootFrame rootFrame, GraphType graphType) {

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));

        int response = fileChooser.showOpenDialog(null);
        if (response != JFileChooser.APPROVE_OPTION) {
            return;
        }

        ArrayList<Node> buffNodes = new ArrayList<>();
        ArrayList<Edge> buffEdges = new ArrayList<>();
        try (FileInputStream fileInputStream = new FileInputStream(fileChooser.getSelectedFile())) {

            switch (graphType) {
                case EDGE_LIST:
                    setNodes(buffNodes, buffEdges,
                            loadEdgeList(fileInputStream, buffEdges), rootFrame.mGraph);
                    break;
                case ADJACENCY_MATRIX:
                    setNodes(buffNodes, buffEdges,
                            loadAdjacencyMatrix(fileInputStream, buffEdges), rootFrame.mGraph);
                    break;
                case INCIDENCE_MATRIX:
                    loadIncidenceMatrix(fileInputStream, buffNodes, buffEdges);
                    break;
                case ADJACENCY_LIST:
                    loadAdjacencyList(fileInputStream, buffNodes, buffEdges);
                    break;

                default:
                    break;
            }
            buffEdges.sort(new Comparator<Edge>() {
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
            rootFrame.mNodes.clear();
            rootFrame.mNodes.addAll(buffNodes);
            rootFrame.mEdges.clear();
            rootFrame.mEdges.addAll(buffEdges);
            rootFrame.mGraph.repaint();

            JOptionPane.showMessageDialog(this, "Graph is loaded", "Load graph", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.toString(), "Load graph", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Integer loadEdgeList(FileInputStream fileInputStream, ArrayList<Edge> edges)
            throws Exception {
        byte[] buff = new byte[1024 * 10];
        fileInputStream.read(buff);
        String[] massStr = new String(buff).trim().split("\n");
        buff = null;
        Integer nodeSise = Integer.valueOf(massStr[0].trim());
        for (int i = 1; i < massStr.length; i++) {
            String[] b = massStr[i].split(" ");
            if (b.length != 2) {
                throw new RuntimeException("Wrong syntaxis: not 2 numbers in edge");
            }
            edges.add(new Edge(Integer.parseInt(b[0].trim()), Integer.parseInt(b[1].trim())));
        }
        return nodeSise;
    }

    private Integer loadAdjacencyMatrix(FileInputStream fileInputStream, ArrayList<Edge> edges)
            throws Exception {
        byte[] buff = new byte[1024 * 15];
        fileInputStream.read(buff);
        String[] massStr = new String(buff).trim().split("\n");
        if (massStr.length == 0) {
            return 0;
        }
        buff = null;
        Integer nodeSize = massStr[0].trim().split(" ").length;
        for (int i = 0; i < nodeSize; i++) {
            String[] str = massStr[i].trim().split(" ");
            if (str.length != nodeSize) {
                throw new RuntimeException("The matrix is broken at " + nodeSize + "line");
            }
            for (int j = 0; j < str.length; j++) {
                Integer num = Integer.parseInt(str[j]);
                if (num != 0 && num != 1) {
                    throw new RuntimeException("The syntax is broken: Matrix[" + i + "][" + j + "] = " + num);
                }
                if (num == 1) {
                    edges.add(new Edge(i, num));
                }
            }
        }
        return nodeSize;
    }

    private void loadIncidenceMatrix(FileInputStream fileInputStream, ArrayList<Node> nodes, ArrayList<Edge> edges) {

    }

    private void loadAdjacencyList(FileInputStream fileInputStream, ArrayList<Node> nodes, ArrayList<Edge> edges) {

    }

    private void setNodes(ArrayList<Node> nodes, ArrayList<Edge> edges, Integer nodeCapasity, JGraph graph) {
        // Базовые параметры алгоритма
        double k = 100.0; // Идеальная длина пружины
        double c = 0.1; // Сила отталкивания
        double temperature = 100.0;
        final double coolingRate = 0.95;

        ArrayList<Point> forces = new ArrayList<>();

        // 1. Инициализация случайными координатами
        for (int i = 0; i < nodeCapasity; i++) {
            nodes.add(new Node(((int) (Math.random() * graph.getSize().width)),
                    ((int) (Math.random() * graph.getSize().height)),
                    Color.WHITE));
            forces.add(new Point(0, 0));
        }

        // Основной цикл симуляции
        for (int iter = 0; iter < 100; iter++) {
            // Очистка сил
            for (int i = 0; i < nodeCapasity; i++) {
                forces.get(i).setLocation(0, 0);
            }

            // 2. Вычисление сил отталкивания
            for (int i = 0; i < nodeCapasity; i++) {
                for (int j = 0; j < nodeCapasity; j++) {
                    if (i == j)
                        continue;
                    Point ni = nodes.get(i).mPoint;
                    Point nj = nodes.get(j).mPoint;

                    double dx = ni.x - nj.x;
                    double dy = ni.y - nj.y;
                    double dist = Math.max(0.01, Math.hypot(dx, dy));

                    // Закон Кулона с инверсией
                    double repForce = (c * k * k) / dist;

                    forces.get(i).x += (dx / dist) * repForce;
                    forces.get(i).y += (dy / dist) * repForce;
                }
            }

            // 3. Вычисление сил притяжения
            for (Edge edge : edges) {
                Point nu = nodes.get(edge.u).mPoint;
                Point nv = nodes.get(edge.v).mPoint;

                double dx = nu.x - nv.x;
                double dy = nu.y - nv.y;
                double dist = Math.max(0.01, Math.hypot(dx, dy));

                // Закон Гука
                double attForce = (dist * dist) / k;

                forces.get(edge.u).x -= (dx / dist) * attForce;
                forces.get(edge.u).y -= (dy / dist) * attForce;
                forces.get(edge.v).x += (dx / dist) * attForce;
                forces.get(edge.v).y += (dy / dist) * attForce;
            }

            // 4. Применение сил и ограничение температурой
            for (int i = 0; i < nodeCapasity; i++) {
                Point node = nodes.get(i).mPoint;
                Point force = forces.get(i);

                double forceLength = Math.max(0.01, Math.hypot(force.x, force.y));
                double step = Math.min(forceLength, temperature);

                node.x += (force.x / forceLength) * step;
                node.y += (force.y / forceLength) * step;
            }

            // 5. Охлаждение
            temperature *= coolingRate;
        }
    }

    public static void main(String[] args) {
        new JLoadGraph(null);
    }
}
