package eg.edu.alexu.csd.oop.draw;

import java.awt.Color;
import java.awt.Point;
//import java.beans.XMLDecoder;
//import java.beans.XMLEncoder;
//import java.io.BufferedInputStream;
//import java.io.BufferedOutputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import java.io.FileWriter;
//import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
//import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
//import java.util.Scanner;
import java.util.regex.*;

public class JSONParser {

    public void write(LinkedList<Shape> shapes, String fileName)
            throws Exception {
        PrintWriter writer = new PrintWriter(new FileWriter(fileName));
        writer.println("{");
        writer.println("\"shapes\":[");
        boolean first = true;
        for (Shape shape : shapes) {
            if (first) {
                writer.printf("\t{\n");
                first = false;
            } else {
                writer.printf("\t,{\n");
            }
            writer.printf("\t\t\"className\": \"%s\" ,\n", shape.getClass()
                    .getName());
            writer.printf("\t\t\"position\": \"%s\" ,\n",
                    Objects.toString(shape.getPosition(), "null"));
            writer.printf("\t\t\"color\": \"%s\" ,\n",
                    Objects.toString(shape.getColor(), "null"));
            writer.printf("\t\t\"fillColor\": \"%s\" ,\n",
                    Objects.toString(shape.getFillColor(), "null"));
            String props = Objects.toString(shape.getProperties(), "null");

            props = props.replace("{", "{\"");
            props = props.replaceAll("=", "\":");
            props = props.replaceAll(", ", ", \"");

            writer.printf("\t\t\"properties\": %s\n", props);
            writer.printf("\t}\n");
        }
        writer.println("]\n");
        writer.println("}");

        writer.close();
    }

    public LinkedList<Shape> read(String filename) throws Exception {
        LinkedList<Shape> shapes = new LinkedList<Shape>();
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        String[] JSONObjects = content.split("\\}\n\t,\\{");
        for (String s : JSONObjects) {
            Shape toShape = null;
            Pattern pattern = Pattern.compile("\"className\": (\".*?\")");
            Matcher matcher = pattern.matcher(s);
            if (matcher.find()) {
                String path = matcher.group(1);
                path = path.replaceAll("\"", "");
                try {
                    toShape = ShapeFactory.getInstance().makeShape(path);
                } catch (Exception e) {
                }
            }

            pattern = Pattern.compile("\"position\": \"(.*?)\"");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String pos = matcher.group(1);
                if (pos.equals("null")) {
                    toShape.setPosition(null);
                } else {
                    pattern = Pattern
                            .compile("\"position\": \"java.awt.Point\\[x=(.*?),y=(.*?)\\]\"");
                    matcher = pattern.matcher(s);

                    if (matcher.find()) {
                        String posX = matcher.group(1);
                        String posY = matcher.group(2);
                        toShape.setPosition(new Point(Integer.parseInt(posX),
                                Integer.parseInt(posY)));
                    }
                }
            }

            pattern = Pattern.compile("\"color\": \"(.*?)\"");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String color = matcher.group(1);
                if (color.equals("null")) {
                    toShape.setColor(null);
                } else {
                    pattern = Pattern
                            .compile("\"color\": \"java.awt.Color\\[r=(.*?),g=(.*?),b=(.*?)\\]\"");
                    matcher = pattern.matcher(s);

                    if (matcher.find()) {
                        String colorR = matcher.group(1);
                        String colorG = matcher.group(2);
                        String colorB = matcher.group(3);
                        toShape.setColor(new Color(Integer.parseInt(colorR),
                                Integer.parseInt(colorG), Integer
                                        .parseInt(colorB)));
                    }
                }
            }
            pattern = Pattern.compile("\"fillColor\": \"(.*?)\"");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String color = matcher.group(1);
                if (color.equals("null")) {
                    toShape.setFillColor(null);
                } else {
                    pattern = Pattern
                            .compile("\"fillColor\": \"java.awt.Color\\[r=(.*?),g=(.*?),b=(.*?)\\]\"");
                    matcher = pattern.matcher(s);

                    if (matcher.find()) {
                        String colorR = matcher.group(1);
                        String colorG = matcher.group(2);
                        String colorB = matcher.group(3);
                        toShape.setFillColor(new Color(
                                Integer.parseInt(colorR), Integer
                                        .parseInt(colorG), Integer
                                        .parseInt(colorB)));
                    }
                }
            }

            pattern = Pattern.compile("\"properties\": (.*)");
            matcher = pattern.matcher(s);
            if (matcher.find()) {
                String color = matcher.group(1);
                if (color.equals("null")) {
                    toShape.setProperties(new HashMap<String, Double>());
                    shapes.add(toShape);
                } else {
                    Map<String, Double> map = new HashMap<String, Double>();
                    for (String key : toShape.getProperties().keySet()) {
                        String keyPattern = "\"";
                        keyPattern += key;
                        keyPattern += "\":(\\d+\\.\\d+)";
                        pattern = Pattern.compile(keyPattern);
                        matcher = pattern.matcher(s);
                        if (matcher.find()) {
                            String prop = matcher.group(1);
                            map.put(key, Double.parseDouble(prop));
                        }
                    }
                    toShape.setProperties(map);
                    shapes.add(toShape);
                }
            }

        }
        return shapes;
    }

}
