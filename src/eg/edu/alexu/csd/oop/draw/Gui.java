package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.JLabel;

public class Gui {

    private JFrame frame;
    private CustomPanel customPanel;
    private DrawingEngine engine = new Engine();
    private Shape currentShape;
    private Color currentColor = Color.black;
    private Color currentFillColor = Color.black;
    private Color updatedColor = Color.black;
    private Color updatedFillColor = Color.black;
    private ShapeFactory factory = ShapeFactory.getInstance();
    private JFileChooser browse = new JFileChooser();

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Gui window = new Gui();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    private Gui() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setBackground(new Color(245, 245, 245));
        frame.setBounds(100, 100, 1425, 778);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);
        frame.setTitle("Paint");

        final JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setOpaque(true);
        toolBar.setBackground(new Color(245, 245, 245));
        toolBar.setBounds(520, 0, 1000, 70);
        frame.getContentPane().add(toolBar);

        JButton undo = new JButton("Undo");
        undo.setBounds(0, 200, 100, 50);
        undo.setBackground(new Color(220, 220, 220));
        undo.setFont(new Font("Arial", Font.BOLD, 20));
        frame.getContentPane().add(undo);
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                engine.undo();
                customPanel.getGraphics().setColor(Color.GRAY);
                customPanel.getGraphics().clearRect(1, 1, 1598, 848);
                engine.refresh(customPanel.getGraphics());
            }
        });

        JButton redo = new JButton("Redo");
        redo.setFont(new Font("Arial", Font.BOLD, 20));
        redo.setBackground(new Color(220, 220, 220));
        redo.setBounds(100, 200, 100, 50);
        frame.getContentPane().add(redo);
        redo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                engine.redo();
                customPanel.getGraphics().setColor(Color.GRAY);
                customPanel.getGraphics().clearRect(1, 1, 1598, 848);
                engine.refresh(customPanel.getGraphics());
            }
        });

        JButton save = new JButton("Save");
        save.setFont(new Font("Arial", Font.BOLD, 20));
        save.setBackground(new Color(220, 220, 220));
        save.setBounds(0, 250, 100, 50);
        frame.getContentPane().add(save);
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = JOptionPane.showInputDialog(frame,
                        "Enter file name");
                engine.save(path);
            }
        });

        JButton load = new JButton("Load");
        load.setFont(new Font("Arial", Font.BOLD, 20));
        load.setBackground(new Color(220, 220, 220));
        load.setBounds(100, 250, 100, 50);
        frame.getContentPane().add(load);
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String path = JOptionPane.showInputDialog(frame,
                        "Enter file name");
                engine.load(path);
                customPanel.getGraphics().setColor(Color.GRAY);
                customPanel.getGraphics().clearRect(1, 1, 1598, 848);
                engine.refresh(customPanel.getGraphics());
            }
        });

        final Dimension dim = new Dimension();
        dim.width = 150;
        dim.height = 70;

        for (final Class<?> c : engine.getSupportedShapes()) {
            JButton btn = new JButton(c.getSimpleName());
            btn.setBackground(new Color(220, 220, 220));
            btn.setFont(new Font("Arial", Font.BOLD, 20));

            btn.setMinimumSize(dim);
            btn.setMaximumSize(dim);

            toolBar.add(btn);

            btn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    JButton btn = (JButton) arg0.getSource();
                    currentShape = factory.makeShape(c.getName());
                    btn.getModel().setPressed(false);
                }
            });
        }
        final JButton addShape = new JButton("Add Shape(s)");
        addShape.setBackground(new Color(220, 220, 220));
        addShape.setFont(new Font("Arial", Font.BOLD, 20));
        addShape.setMinimumSize(dim);
        addShape.setMaximumSize(dim);

        toolBar.add(addShape);
        addShape.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnValue = browse.showOpenDialog(addShape);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File file = browse.getSelectedFile();
                    ClassFinder cls = new ClassFinder(Shape.class);
                    try {
                        Set<Class<?>> plugins = cls.loadPlugin(file);
                        toolBar.remove(addShape);
                        for (final Class<?> c : plugins) {
                            JButton btn = new JButton(c.getSimpleName());
                            btn.setBackground(new Color(220, 220, 220));
                            btn.setFont(new Font("Arial", Font.BOLD, 20));

                            btn.setMinimumSize(dim);
                            btn.setMaximumSize(dim);

                            toolBar.add(btn);

                            btn.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent arg0) {
                                    JButton btn = (JButton) arg0.getSource();

                                    currentShape = factory.makeShape(c
                                            .getName());

                                    btn.getModel().setPressed(false);
                                }
                            });
                        }
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    toolBar.add(addShape);
                }
            }
        });

        final JButton btnColor = new JButton("");
        btnColor.setBackground(Color.black);
        btnColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JColorChooser colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
                for (AbstractColorChooserPanel accp : panels) {
                    if (accp.getDisplayName().equals("HSV")) {
                        JOptionPane.showMessageDialog(frame, accp,
                                "Choose Color", JOptionPane.OK_OPTION);
                        currentColor = colorChooser.getColor();
                        btnColor.setBackground(currentColor);
                    }
                }
            }

        });
        btnColor.setBounds(140, 100, 60, 50);
        frame.getContentPane().add(btnColor);

        final JButton btnFillColor = new JButton("");
        btnFillColor.setBackground(Color.black);

        btnFillColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JColorChooser colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
                for (AbstractColorChooserPanel accp : panels) {
                    if (accp.getDisplayName().equals("HSV")) {
                        JOptionPane.showMessageDialog(frame, accp,
                                "Choose Color", JOptionPane.OK_OPTION);
                        currentFillColor = colorChooser.getColor();
                        btnFillColor.setBackground(currentFillColor);
                        currentShape.setFillColor(currentFillColor);
                    }
                }
            }
        });
        btnFillColor.setBounds(140, 150, 60, 50);
        frame.getContentPane().add(btnFillColor);

        JLabel setColor = new JLabel("Set Color:");
        setColor.setHorizontalAlignment(SwingConstants.CENTER);
        setColor.setFont(new Font("Arial", Font.BOLD, 20));
        setColor.setBounds(0, 100, 140, 50);
        frame.getContentPane().add(setColor);

        JLabel setFillColor = new JLabel("Set Fill Color:");
        setFillColor.setHorizontalAlignment(SwingConstants.CENTER);
        setFillColor.setFont(new Font("Arial", Font.BOLD, 20));
        setFillColor.setBounds(0, 150, 140, 50);
        frame.getContentPane().add(setFillColor);

        customPanel = new CustomPanel();
        customPanel.setBounds(220, 70, 1600, 850);
        frame.getContentPane().add(customPanel);
    }

    private class CustomPanel extends JPanel {

        /**
         * This variable is to suppress warning
         */
        private static final long serialVersionUID = 1L;

        private CustomPanel() {
            setBackground(Color.white);
            setBorder(BorderFactory.createLineBorder(Color.black));
            addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    Shape[] drawnShapes = engine.getShapes();
                    if (currentShape != null) {
                        Shape newShape = null;
                        try {
                            newShape = (Shape) currentShape.clone();
                        } catch (Exception s) {
                        }
                        newShape.setPosition(new Point(e.getX(), e.getY()));
                        newShape.setColor(currentColor);
                        newShape.setFillColor(currentFillColor);
                        Map<String, Double> properties = newShape
                                .getProperties();
                        JPanel myPanel = new JPanel();
                        Map<String, JTextField> input = new HashMap<String, JTextField>();
                        if(properties == null){
                            return;
                        }
                        for (String key : properties.keySet()) {
                            myPanel.add(new JLabel(key));
                            JTextField field = new JTextField(5);
                            myPanel.add(field);
                            input.put(key, field);
                        }
                        int choosenOption = JOptionPane.showConfirmDialog(
                                frame, myPanel, "Set Shape Properties",
                                JOptionPane.OK_CANCEL_OPTION);
                        if (choosenOption == 2 || choosenOption == -1) {
                            currentShape = null;
                            return;
                        }
                        for (String key : input.keySet()) {
                            if (isNumeric(input.get(key).getText())) {
                                properties.put(key, Double.parseDouble(input
                                        .get(key).getText()));
                            } else {
                                newShape.setProperties(currentShape
                                        .getProperties());
                                break;
                            }
                        }
                        try {
                            engine.addShape((Shape) newShape.clone());
                            currentShape = null;
                        } catch (Exception f) {
                        }

                        customPanel.getGraphics().setColor(Color.GRAY);
                        customPanel.getGraphics().clearRect(1, 1, 1598, 848);
                        engine.refresh(customPanel.getGraphics());// put it in
                                                                  // the update
                                                                  // method
                    } else {
                        Point selected = new Point(e.getX(), e.getY());
                        for (int i = drawnShapes.length - 1; i >= 0; i--) {
                            if (isNear(drawnShapes[i], selected)) {
                                break;
                            }
                        }
                    }
                }
            });
        }

        public void paintComponent(Graphics g) {
            customPanel.getGraphics().setColor(Color.GRAY);
            customPanel.getGraphics().clearRect(1, 1, 1598, 848);
            engine.refresh(g);
            for (Shape s : engine.getShapes()) {
                s.draw(g);
            }
        }
    }

    private boolean isNear(Shape shape, Point selected) {
        Map<String, Double> prop = shape.getProperties();
        Point p = shape.getPosition();
        String str = shape.getClass().getSimpleName();

        if (str.equals("Line")) {
            Double u = prop.get("Length");
            Point p1 = shape.getPosition();
            Double dis = Math.sqrt(Math.pow((selected.x - p1.x), 2)
                    + Math.pow((selected.y - p1.y), 2));
            Integer r1 = dis.intValue();

            Double angle = Math.toRadians(prop.get("Angle"));
            Double endX = p1.x + Math.cos(angle) * u;
            Double endY = p1.y + Math.sin(-angle) * u;

            Point p2 = new Point(endX.intValue(), endY.intValue());
            dis = Math.sqrt(Math.pow((selected.x - p2.x), 2)
                    + Math.pow((selected.y - p2.y), 2));
            Integer r2 = dis.intValue();

            Double x = (p1.x + endX) / 2;
            Double y = (p1.y + endY) / 2;
            Point mid = new Point(x.intValue(), y.intValue());
            dis = Math.sqrt(Math.pow((selected.x - mid.x), 2)
                    + Math.pow((selected.y - mid.y), 2));
            Integer rMid = dis.intValue();

            if (r1 <= u / 8 || r2 <= u / 8 || rMid <= u / 8) {
                showWindow(shape, prop);
                return true;
            }
        } else {
            Double sides[] = new Double[50];
            int i = 0;
            for (String key : prop.keySet()) {
                sides[i] = prop.get(key);
                i++;
            }
            if (prop.size() == 1) {
                sides[1] = sides[0];
            }
            if (selected.x >= p.x && selected.x <= p.x + sides[0]
                    && selected.y >= p.y && selected.y <= p.y + sides[1]) {
                showWindow(shape, prop);
                return true;
            }
        }

        return false;
    }

    private void showWindow(Shape shape, Map<String, Double> prop) {
        String[] buttons = { "Remove", "Update", "Cancel" };
        int choosenOption = JOptionPane.showOptionDialog(null,
                "Remove or Update this Shape?", "Choose Operation",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                buttons, buttons);
        if (choosenOption == 0) {
            engine.removeShape(shape);
            customPanel.getGraphics().setColor(Color.GRAY);
            customPanel.getGraphics().clearRect(1, 1, 1598, 848);
            engine.refresh(customPanel.getGraphics());
        } else if (choosenOption == 1) {
            update(shape, prop);
        }
    }

    private void update(Shape oldShape, Map<String, Double> prop) {
        JPanel myPanel = new JPanel();
        Map<String, Double> newProp = new HashMap<String, Double>();
        Shape newShape = null;
        try {
            newShape = (Shape) oldShape.clone();
        } catch (CloneNotSupportedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Map<String, JTextField> input = new HashMap<String, JTextField>();

        JTextField coox = new JTextField(5);
        JTextField cooy = new JTextField(5);
        myPanel.add(new JLabel("New Position-X"));
        myPanel.add(coox);
        myPanel.add(new JLabel("New Position-Y"));
        myPanel.add(cooy);
        for (String key : prop.keySet()) {
            myPanel.add(new JLabel(key));
            JTextField field = new JTextField(5);
            myPanel.add(field);
            input.put(key, field);
        }
        myPanel.add(new JLabel("Choose Color"));

        final JButton btnColor = new JButton("");
        btnColor.setBackground(Color.black);
        btnColor.setBounds(453, 6, 42, 36);
        updatedColor = Color.black;
        btnColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JColorChooser colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
                for (AbstractColorChooserPanel accp : panels) {
                    if (accp.getDisplayName().equals("HSV")) {
                        JOptionPane.showMessageDialog(null, accp,
                                "Choose Color", JOptionPane.OK_OPTION);
                        updatedColor = colorChooser.getColor();
                        btnColor.setBackground(updatedColor);
                    }
                }
            }
        });

        myPanel.add(btnColor);

        myPanel.add(new JLabel("Choose Fill Color"));

        final JButton btnFillColor = new JButton("");
        btnFillColor.setBackground(Color.black);
        btnFillColor.setBounds(453, 6, 42, 36);
        updatedFillColor = Color.black;
        btnFillColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                JColorChooser colorChooser = new JColorChooser();
                AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
                for (AbstractColorChooserPanel accp : panels) {
                    if (accp.getDisplayName().equals("HSV")) {
                        JOptionPane.showMessageDialog(null, accp,
                                "Choose Color", JOptionPane.OK_OPTION);
                        updatedFillColor = colorChooser.getColor();
                        btnFillColor.setBackground(updatedFillColor);
                    }
                }
            }
        });

        myPanel.add(btnFillColor);

        int choosenOption = JOptionPane.showConfirmDialog(null, myPanel,
                "Set new Shape Properties", JOptionPane.OK_CANCEL_OPTION);
        if (choosenOption == 2 || choosenOption == -1) {
            return;
        }
        Point in = new Point();
        if (isNumeric(coox.getText()) && isNumeric(cooy.getText())) {
            in = new Point(Integer.parseInt(coox.getText()),
                    Integer.parseInt(cooy.getText()));
        } else {
            Point current = oldShape.getPosition();
            in = new Point(current.x, current.y);
        }
        newShape.setPosition(in);
        newShape.setColor(updatedColor);
        newShape.setFillColor(updatedFillColor);
        for (String key : input.keySet()) {
            if (isNumeric(input.get(key).getText())) {
                newProp.put(key, Double.parseDouble(input.get(key).getText()));
                newShape.setProperties(newProp);
            } else {
                newShape.setProperties(oldShape.getProperties());
                break;
            }
        }
        engine.updateShape(oldShape, newShape);
        customPanel.getGraphics().setColor(Color.GRAY);
        customPanel.getGraphics().clearRect(1, 1, 1598, 848);
        engine.refresh(customPanel.getGraphics());
    }

    private boolean isNumeric(String str) {
        try {
            @SuppressWarnings("unused")
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
