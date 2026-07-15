package src.main;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;

public class Node implements Serializable {
    public Point mPoint;
    public Color mColor;

    public Node() {
    }

    public Node(Integer x, Integer y, Color color) {
        mPoint = new Point(x, y);
        mColor = color;
    }

    public Node(Point point, Color color) {
        this.mPoint = point;
        this.mColor = color;
    }
}
