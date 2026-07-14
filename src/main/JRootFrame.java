package src.main;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

// import src.main.Algorithms.TopologicalSort;
import src.main.Algorithms.*;

public class JRootFrame extends JFrame {
    ArrayList<Node> mNodes = new ArrayList<>();
    ArrayList<Edge> mEdges = new ArrayList<>();
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
        JPopupMenu popupMenu = new JPopupMenu();

        JButton menu = new JButton("Menu");
        menu.addActionListener(e -> {
            popupMenu.show(menu, 0, menu.getHeight());
        });
        mToolBar.add(menu);
        JMenuItem open = new JMenuItem("Load graph");
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                new JLoadGraph(JRootFrame.this)
            }
        });

        JMenuItem save = new JMenuItem("Save state");
        save.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Statement Files (*.stm)", "stm"));
            int responce = fileChooser.showSaveDialog(null);
            if (responce == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                // Ensure the file path ends with the proper extension if missing
                String filePath = fileToSave.getAbsolutePath();
                if (!filePath.toLowerCase().endsWith(".stm")) {
                    fileToSave = new File(filePath + ".stm");
                }
                saveState(fileToSave);
            }
        });

        JMenuItem loadState = new JMenuItem("Load state");
        loadState.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Statement Files (*.stm)", "stm"));
            int responce = fileChooser.showOpenDialog(null);
            if (responce == JFileChooser.APPROVE_OPTION) {
                readState(fileChooser.getSelectedFile());
                repaint();
            }
        });

        Method[] mMethods = { Method.QUEUE, Method.STACK, Method.NONE };
        JComboBox<Method> comboBox = new JComboBox<>(mMethods);
        comboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMethod((Method) comboBox.getSelectedItem());
            }
        });

        popupMenu.add(open);
        popupMenu.add(new JSeparator());
        popupMenu.add(save);
        popupMenu.add(loadState);
        popupMenu.add(new JSeparator());
        popupMenu.add(new JMenuItem("Exit"));

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
        // Базовые параметры алгоритма
        double k = 100.0; // Идеальная длина пружины
        double c = 0.1; // Сила отталкивания
        double temperature = 100.0;
        final double coolingRate = 0.95;

        ArrayList<Point> forces = new ArrayList<>();

        // 1. Инициализация случайными координатами
        for (int i = 0; i < nodeCapasity; i++) {
            mNodes.add(new Node(((int) (Math.random() * mGraph.getSize().width)),
                    ((int) (Math.random() * mGraph.getSize().height)),
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
                    Point ni = mNodes.get(i).mPoint;
                    Point nj = mNodes.get(j).mPoint;

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
            for (Edge edge : mEdges) {
                Point nu = mNodes.get(edge.u).mPoint;
                Point nv = mNodes.get(edge.v).mPoint;

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
                Point node = mNodes.get(i).mPoint;
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

    public void saveState(File file) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fileOutputStream)) {
            oos.writeObject(mNodes);
            oos.writeObject(mEdges);
            oos.writeObject(((JComboBox) mToolBar.getComponent(1)).getSelectedItem());
            if (mTopologicalSort != null) {
                oos.writeObject(mTopologicalSort);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void readState(File file) {
        ArrayList<Node> buffNodes = (ArrayList<Node>) mNodes.clone();
        ArrayList<Edge> buffEdges = (ArrayList<Edge>) mEdges.clone();
        try (FileInputStream fileOutputStream = new FileInputStream(file);
                ObjectInputStream oos = new ObjectInputStream(fileOutputStream)) {
            mNodes.clear();
            mEdges.clear();
            mNodes.addAll((ArrayList<Node>) oos.readObject());
            mEdges.addAll(((ArrayList<Edge>) oos.readObject()));
            Method method = (Method) oos.readObject();
            ((JComboBox) mToolBar.getComponent(1)).setSelectedItem(method);
            switch (method) {
                case QUEUE:
                    mTopologicalSort = new TopologicalSortQueue(mNodes, mEdges);
                    mTopologicalSort = ((TopologicalSortQueue) oos.readObject());
                    break;
                case STACK:
                    mTopologicalSort = new TopologicalSortStack(mNodes, mEdges);
                    mTopologicalSort = ((TopologicalSortStack) oos.readObject());
                    break;
                case NONE:
                    mTopologicalSort = null;
                    break;
                default:
                    throw new RuntimeException("Unknown method type!!!");

            }

        } catch (Exception e) {
            System.out.println(e);
            mNodes.clear();
            mNodes.addAll(buffNodes);
            mEdges.clear();
            mEdges.addAll(buffEdges);
            JOptionPane.showMessageDialog(this, "State is loaded unsuccessfully!", "State load",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        JOptionPane.showMessageDialog(this, "State is loaded successfully!", "State load",
                JOptionPane.INFORMATION_MESSAGE);
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
