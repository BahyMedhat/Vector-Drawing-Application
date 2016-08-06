package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

public class Triangle extends Polygon {

    public Triangle() {
        position = new Point();
        properties = new HashMap<String, Double>();
        properties.put("Side", 100.0);
        color = new Color(0, 0, 0); // black
        fillColor = new Color(0, 0, 0);
    }

    @Override
    public void draw(Graphics canvas) {
        canvas.setColor(color);

        Point pos = new Point();
        Integer sideLength = properties.get("Side").intValue();
        pos.x = position.x + sideLength / 2;
        Double bottomY = position.y + (sideLength * ((Math.sqrt(3)) / 2.0));
        pos.y = bottomY.intValue();

        int[] x = new int[] { pos.x, pos.x - (sideLength / 2),
                pos.x + (sideLength / 2) };
        Double topY = (pos.y - (sideLength * ((Math.sqrt(3)) / 2.0)));
        int[] y = new int[] { topY.intValue(), pos.y, pos.y };
        canvas.drawPolygon(x, y, 3);
        canvas.setColor(fillColor);
        canvas.fillPolygon(x, y, 3);

    }

    @Override
    public Shape clone() throws CloneNotSupportedException {
        Triangle clone = new Triangle();
        clone.setColor(this.color);
        clone.setFillColor(this.fillColor);
        clone.setPosition(this.position);
        clone.setProperties(new HashMap<String, Double>(properties));
        return clone;
    }

}
