package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

public abstract class Polygon implements Shape {
    protected Point position;
    protected Map<String, Double> properties;
    protected Color color;
    protected Color fillColor;

    public void setPosition(java.awt.Point position) {
        this.position = position;
    }

    public java.awt.Point getPosition() {
        return position;
    }

    /* update shape specific properties (e.g., radius) */
    public void setProperties(java.util.Map<String, Double> properties) {
        this.properties = new HashMap<String, Double>(properties);
    }

    public java.util.Map<String, Double> getProperties() {
        return properties;
    }

    public void setColor(java.awt.Color color) {
        this.color = color;
    }

    public java.awt.Color getColor() {
        return color;
    }

    public void setFillColor(java.awt.Color color) {
        this.fillColor = color;
    }

    public java.awt.Color getFillColor() {
        return fillColor;
    }

    /* redraw the shape on the canvas */
    public abstract void draw(java.awt.Graphics canvas);

    /* create a deep clone of the shape */
    public abstract Shape clone() throws CloneNotSupportedException;
}
