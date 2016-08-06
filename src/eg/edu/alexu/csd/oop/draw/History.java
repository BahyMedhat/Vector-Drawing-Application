package eg.edu.alexu.csd.oop.draw;

import java.util.LinkedList;
import java.util.Stack;

public class History {

    private static History instance;
    private Stack<LinkedList<Shape>> prev;
    private Stack<LinkedList<Shape>> next;
    private LinkedList<Shape> last;
    private boolean flag = false;

    public static History getInstance() {
        if (instance == null) {
            return new History();
        }
        return instance;
    }

    private History() {
        prev = new Stack<LinkedList<Shape>>();
        next = new Stack<LinkedList<Shape>>();
        last = new LinkedList<Shape>();
    }

    public LinkedList<Shape> getPrev() {
        if (flag && prev.size() == 1) {
            return prev.peek();
        }
        if (prev.isEmpty()) {
            return last;
        }
        next.push(prev.pop());
        if (prev.isEmpty()) {
            return last;
        }
        return prev.peek();
    }

    public LinkedList<Shape> getNext() {
        if (next.isEmpty()) {
            if (prev.isEmpty()) {
                return last;
            }
            return prev.peek();
        }
        prev.push(next.pop());
        if (prev.isEmpty()) {
            return last;
        }
        return prev.peek();
    }

    public void insert(LinkedList<Shape> shapes) {
        int capacity = 20;
        if (flag) {
            capacity = 21;
        }
        prev.push(new LinkedList<Shape>(shapes));
        if (prev.size() > capacity) {
            last = prev.get(0);
            prev.remove(0);
        }
        next.clear();
    }

    public void clear() {
        next.clear();
        prev.clear();
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}