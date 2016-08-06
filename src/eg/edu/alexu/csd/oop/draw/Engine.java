package eg.edu.alexu.csd.oop.draw;

import java.util.LinkedList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Engine implements DrawingEngine {

    private LinkedList<Shape> shapes;
    private History history = History.getInstance();
    private XMLParser xml = new XMLParser();
    private JSONParser json = new JSONParser();

    public Engine() {
        shapes = new LinkedList<Shape>();
    }

    /* redraw all filenameshapes on the canvas */
    public void refresh(java.awt.Graphics canvas) {
        for (Shape s : shapes) {
            s.draw(canvas);
        }
    }

    public void addShape(Shape shape) {
        if (shape == null) {
            throw new RuntimeException("Cannot add null shape");
        }
        shapes.add(shape);
        history.insert(shapes);
    }

    public void removeShape(Shape shape) {
        if (shape == null) {
            throw new RuntimeException("Cannot add null shape");
        }
        shapes.remove(shape);
        history.insert(shapes);
    }

    public void updateShape(Shape oldShape, Shape newShape) {
        if (oldShape == null || newShape == null) {
            throw new RuntimeException("Cannot add null shape");
        }
        int index = shapes.indexOf(oldShape);
        if (index == -1) {
            throw new RuntimeException("Element not found");
        }
        shapes.remove(oldShape);
        shapes.add(index, newShape);
        history.insert(shapes);
    }

    /* return the created shapes objects */
    public Shape[] getShapes() {
        if (shapes.size() == 0) {
            return new Shape[] {};
        }
        Shape[] shapesArray = shapes.toArray(new Shape[shapes.size()]);
        return shapesArray;
    }

    /*
     * return the classes (types) of supported shapes that can be dynamically
     * loaded at runtime (see Part 3)
     */

    @SuppressWarnings("unchecked")
    public java.util.List<Class<? extends Shape>> getSupportedShapes() {

        LinkedList<Class<? extends Shape>> supportedShapes = new LinkedList<Class<? extends Shape>>();
        Class<?> shapeClass = Shape.class;
        ClassFinder cls = new ClassFinder(shapeClass);
        Set<Class<?>> classes = cls.getClasses();
        for (@SuppressWarnings("rawtypes")
        Class c : classes) {
            supportedShapes.add(c);
        }
        return supportedShapes;
    }

    /*
     * limited to 20 steps. Only consider in undo & redo these actions:
     * addShape, removeShape, updateShape
     */
    @SuppressWarnings("unchecked")
    public void undo() {
        LinkedList<Shape> tmp = new LinkedList<Shape>(history.getPrev());
        shapes = (LinkedList<Shape>) tmp.clone();
    }

    @SuppressWarnings("unchecked")
    public void redo() {
        LinkedList<Shape> tmp = new LinkedList<Shape>(history.getNext());
        shapes = (LinkedList<Shape>) tmp.clone();
    }

    /*
     * /* use the file extension to determine the type, or throw runtime
     * exception when unexpected extension
     */
    public void save(String path) {
        Pattern pattern = Pattern.compile("(\\.[^.]+)$");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String extension = matcher.group(1);
            if (extension.equalsIgnoreCase(".json")) {
                try {
                    json.write(shapes, path);
                } catch (Exception e) {

                }
            } else if (extension.equalsIgnoreCase(".xml")) {
                try {
                    xml.write(shapes, path);
                } catch (Exception e) {

                }
            } else {
                throw new RuntimeException("Invalid file name");
            }
        } else {
            throw new RuntimeException("Invalid file name");
        }
    }

    public void load(String path) {
        Pattern pattern = Pattern.compile("(\\.[^.]+)$");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            String extension = matcher.group(1);
            if (extension.equalsIgnoreCase(".json")) {
                try {
                    shapes = new LinkedList<Shape>(json.read(path));
                    history.clear();
                    history.insert(shapes);
                    history.setFlag(true);

                } catch (Exception e) {

                }
            } else if (extension.equalsIgnoreCase(".xml")) {
                try {
                    shapes = new LinkedList<Shape>(xml.read(path));

                    history.clear();
                    history.insert(shapes);
                    history.setFlag(true);

                } catch (Exception e) {

                }
            } else {
                throw new RuntimeException("Invalid file name");
            }
        } else {
            throw new RuntimeException("Invalid file name");
        }
    }

}