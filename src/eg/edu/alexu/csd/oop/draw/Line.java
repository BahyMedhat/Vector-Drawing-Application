package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

public class Line extends Polygon {

    public Line() {
        position = new Point();
        properties = new HashMap<String, Double>();
        properties.put("Angle", 0.0);
        properties.put("Length", 200.0);
        color = new Color(0, 0, 0); // black
        fillColor = new Color(0, 0, 0);
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(color);
        Double angle = Math.toRadians(properties.get("Angle"));
        Double endX = Math.cos(angle) * properties.get("Length");
        Double endY = Math.sin(-angle) * properties.get("Length");
        canvas.drawLine(position.x, position.y, position.x + endX.intValue(),
                position.y + endY.intValue());
    }

    @Override
    public Shape clone() throws CloneNotSupportedException {
        Line clone = new Line();
        clone.setColor(this.color);
        clone.setFillColor(this.fillColor);
        clone.setPosition(this.position);
        clone.setProperties(new HashMap<String, Double>(properties));
        return clone;
    }
}
