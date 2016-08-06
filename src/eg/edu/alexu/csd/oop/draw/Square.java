package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

public class Square extends Polygon {

    public Square() {
        position = new Point();
        properties = new HashMap<String, Double>();
        properties.put("Side", 100.0);
        color = new Color(0, 0, 0); // black
        fillColor = new Color(0, 0, 0);
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(color);
        canvas.drawRect(position.x, position.y, properties.get("Side")
                .intValue(), properties.get("Side").intValue());
        canvas.setColor(fillColor);
        canvas.fillRect(position.x, position.y, properties.get("Side")
                .intValue(), properties.get("Side").intValue());
    }

    @Override
    public Shape clone() throws CloneNotSupportedException {
        Square clone = new Square();
        clone.setColor(this.color);
        clone.setFillColor(this.fillColor);
        clone.setPosition(this.position);
        clone.setProperties(new HashMap<String, Double>(properties));
        return clone;
    }

}
