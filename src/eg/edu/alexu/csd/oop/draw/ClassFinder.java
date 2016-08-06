package eg.edu.alexu.csd.oop.draw;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

public class ClassFinder {

    private Class<?> superClass = null;
    private Set<Class<?>> classes = new HashSet<Class<?>>(10000);

    public ClassFinder(Class<?> superClass) {
        this.superClass = superClass;
    }

    private void addClassName(String className) {
        try {
            Class<?> theClass = Class.forName(className, false, this.getClass()
                    .getClassLoader());

            if (superClass.isAssignableFrom(theClass)
                    && !theClass.isInterface()
                    && !Modifier.isAbstract(theClass.getModifiers())) {
                classes.add(theClass);
            }
        } catch (ClassNotFoundException cnfe) {

        }
    }

    public Set<Class<?>> getClasses() {
        String classpath = System.getProperty("java.class.path");
        String pathSeparator = System.getProperty("path.separator");

        StringTokenizer st = new StringTokenizer(classpath, pathSeparator);

        while (st.hasMoreTokens()) {
            File currentDirectory = new File(st.nextToken());

            processFile(currentDirectory.getAbsolutePath(), "");
        }
        for (Class<?> c : classes) {
            LoadedShapes.getInstance().putClass(c.getName(), c);
        }
        return classes;
    }

    private void processFile(String base, String current) {
        File currentDir = new File(base + File.separatorChar + current);

        if (isJar(currentDir.getName())) {
            try {
                processJar(new JarFile(currentDir));
            } catch (Exception e) {
            }
            return;
        } else {
            Set<File> directories = new HashSet<File>();

            File[] children = currentDir.listFiles();

            if (children == null || children.length == 0) {
                return;
            }

            for (int i = 0; i < children.length; i++) {
                File child = children[i];
                if (child.isDirectory()) {
                    directories.add(children[i]);
                } else {
                    if (child.getName().endsWith(".class")) {
                        String className = getClassName(current
                                + ((current == "") ? "" : File.separator)
                                + child.getName());
                        addClassName(className);
                    }
                }
            }

            for (Iterator<File> i = directories.iterator(); i.hasNext();) {
                processFile(base,
                        current + ((current == "") ? "" : File.separator)
                                + ((File) i.next()).getName());
            }
        }
    }

    private boolean isJar(String name) {
        if (name.endsWith(".jar"))
            return true;
        else
            return false;
    }

    private String getClassName(String fileName) {
        String newName = fileName.replace(File.separatorChar, '.');
        newName = newName.replace('/', '.');
        return newName.substring(0, fileName.length() - 6);
    }

    private void processJar(JarFile file) {
        Enumeration<JarEntry> files = file.entries();

        while (files.hasMoreElements()) {
            Object tfile = files.nextElement();
            JarEntry child = (JarEntry) tfile;
            if (child.getName().endsWith(".class")) {
                addClassName(getClassName(child.getName()));
            }
        }
    }

    public Set<Class<?>> loadPlugin(File jarFile) throws MalformedURLException {
        ClassLoader loader = URLClassLoader.newInstance(new URL[] { jarFile
                .toURI().toURL() }, getClass().getClassLoader());
        String path = jarFile.getPath();
        try {
            JarInputStream jis = new JarInputStream(new FileInputStream(path));
            JarEntry je = jis.getNextJarEntry();
            while (je != null) {
                if (je.getName().endsWith(".class")) {
                    String className = getClassName(je.getName());
                    try {
                        Class<?> toBeLoaded = Class.forName(className, true,
                                loader);
                        if (this.superClass.isAssignableFrom(toBeLoaded)
                                && !toBeLoaded.isInterface()
                                && !Modifier.isAbstract(toBeLoaded
                                        .getModifiers())) {
                            classes.add(toBeLoaded);
                        }
                    } catch (Exception ex) {
                    }
                }
                je = jis.getNextJarEntry();
            }
            jis.close();
        } catch (Exception ex) {
        }
        for (Class<?> c : classes) {
            LoadedShapes.getInstance().putClass(c.getName(), c);
        }
        return classes;
    }
}