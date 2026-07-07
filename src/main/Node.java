package src.main;

import java.awt.Color;
import java.awt.Point;

public class Node {
    public Point mPoint;
    public Color mColor;

    public Node() {
    }

    public Node(Integer x, Integer y, Color color)
    {
        mPoint = new Point(x, y);
        mColor = color;
    }
}
