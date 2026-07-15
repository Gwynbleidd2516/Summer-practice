package src.main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class JGraph extends JPanel {
    private ArrayList<Node> nodes = new ArrayList<>();
    private ArrayList<Edge> edges = new ArrayList<>();
    private final Integer mVertSize = 20;

    private double zoomFactor = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private Point startDrag;

    public JGraph() {
        addMouseWheelListener(e -> {
            int notches = e.getWheelRotation();
            Point mousePos = e.getPoint();
            double oldZoom = zoomFactor;

            if (notches < 0) {
                zoomFactor *= 1.1;
            } else {
                zoomFactor /= 1.1;
            }
            zoomFactor = Math.max(0.1, Math.min(10.0, zoomFactor)); // clamp

            offsetX = mousePos.x - (mousePos.x - offsetX) * (zoomFactor / oldZoom);
            offsetY = mousePos.y - (mousePos.y - offsetY) * (zoomFactor / oldZoom);

            repaint();
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startDrag = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - startDrag.x;
                double dy = e.getY() - startDrag.y;
                offsetX += dx;
                offsetY += dy;
                startDrag = e.getPoint();
                repaint();
            }
        });
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform at = new AffineTransform();
        at.translate(offsetX, offsetY);
        at.scale(zoomFactor, zoomFactor);
        g2.transform(at);

        g2.setColor(Color.LIGHT_GRAY);
        int gridSpacing = 50;
        for (int x = -2000; x < 2000; x += gridSpacing) {
            g2.drawLine(x, -2000, x, 2000);
        }
        for (int y = -2000; y < 2000; y += gridSpacing) {
            g2.drawLine(-2000, y, 2000, y);
        }

        g2.setColor(Color.BLACK);
        for (Edge iter : edges) {
            Point a1 = nodes.get(iter.u).mPoint;
            Point a2 = nodes.get(iter.v).mPoint;

            drawArrow(g2, a1.x, a1.y, a2.x, a2.y);
        }

        for (Node iter : nodes) {
            g2.setColor(Color.BLACK);
            int outLineSize = 2;
            g2.fillOval(iter.mPoint.x - (mVertSize + outLineSize) / 2, iter.mPoint.y - (mVertSize + outLineSize) / 2,
                    (mVertSize + outLineSize), (mVertSize + outLineSize));
            g2.setColor(iter.mColor);
            g2.fillOval(iter.mPoint.x - mVertSize / 2, iter.mPoint.y - mVertSize / 2, mVertSize, mVertSize);
            g2.setColor(new Color(iter.mColor.hashCode() ^ 0x00FFFFFF));
            g2.drawString(Integer.toString(nodes.indexOf(iter)), iter.mPoint.x - mVertSize / 4,
                    iter.mPoint.y + mVertSize / 4);
        }
    }

    public void setGraph(ArrayList<Node> nodes, ArrayList<Edge> edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    private void drawArrow(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        int arrowHeadSize = 6;

        double dx = x2 - x1;
        double dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        AffineTransform oldTransform = g2d.getTransform();

        g2d.translate(x1, y1);
        g2d.rotate(angle);

        g2d.drawLine(0, 0, len, 0);

        int[] xPoints = { len / 2, len / 2 - arrowHeadSize, len / 2 - arrowHeadSize, len / 2 };
        int[] yPoints = { 0, -arrowHeadSize, arrowHeadSize, 0 };
        g2d.fillPolygon(xPoints, yPoints, 4);

        g2d.setTransform(oldTransform);
    }

    public static void main(String[] args) {
        ArrayList<Node> n = new ArrayList<>();
        ArrayList<Edge> e = new ArrayList<>();
        n.add(new Node(100, 100, Color.DARK_GRAY));
        n.add(new Node(300, 100, Color.GRAY));
        n.add(new Node(200, 250, Color.WHITE));

        e.add(new Edge(0, 1));
        e.add(new Edge(1, 2));
        e.add(new Edge(2, 0));

        JFrame frame = new JFrame("Визуализация графа");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JGraph graph = new JGraph();
        frame.add(graph);
        graph.setGraph(n, e);
        frame.setSize(400, 350);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
