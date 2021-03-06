package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

public class Ellipse extends ConicSection {

    public Ellipse() {
        position = new Point();
        properties = new HashMap<String, Double>();
        properties.put("Width", 200.0);
        properties.put("Height", 100.0);
        color = new Color(0, 0, 0); // black
        fillColor = new Color(0, 0, 0);
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(color);
        canvas.drawOval(position.x, position.y, properties.get("Width")
                .intValue(), properties.get("Height").intValue());
        canvas.setColor(fillColor);
        canvas.fillOval(position.x, position.y, properties.get("Width")
                .intValue(), properties.get("Height").intValue());
    }

    @Override
    public Shape clone() throws CloneNotSupportedException {
        Ellipse clone = new Ellipse();
        clone.setColor(this.color);
        clone.setFillColor(this.fillColor);
        clone.setPosition(this.position);
        clone.setProperties(new HashMap<String, Double>(properties));
        return clone;
    }
}
