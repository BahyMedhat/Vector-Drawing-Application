package eg.edu.alexu.csd.oop.draw;

import java.util.HashMap;
import java.util.Map;

public class LoadedShapes {

    private Map<String, Class<?>> shapes = new HashMap<String, Class<?>>();

    private static LoadedShapes instance;

    public static LoadedShapes getInstance() {
        if (instance == null) {
            instance = new LoadedShapes();
        }
        return instance;
    }

    public void putClass(String x, Class<?> c) {
        shapes.put(x, c);
    }

    public Class<?> getClass(String x) {
        return shapes.get(x);
    }

}
