package eg.edu.alexu.csd.oop.draw;

public class ShapeFactory {

    private static ShapeFactory instance;

    private ShapeFactory() {

    }

    public static ShapeFactory getInstance() {
        if (instance == null) {
            return new ShapeFactory();
        }
        return instance;
    }

    public Shape makeShape(String shapeType) {
        try {
            Class<?> shape;
            shape = LoadedShapes.getInstance().getClass(shapeType);
            if (shape == null) {
                shape = Class.forName(shapeType);
            }
            Shape newShape = (Shape) shape.newInstance();
            return newShape;
        } catch (Exception e) {
        }
        return null;
    }
}
