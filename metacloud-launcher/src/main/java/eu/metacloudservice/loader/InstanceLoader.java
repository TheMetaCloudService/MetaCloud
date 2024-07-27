/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.loader;

import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class InstanceLoader {

    private Properties properties;

    public InstanceLoader(File file) {
        try {
            final URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (final JarFile jarFile = new JarFile(file)) {
                final JarEntry entry = jarFile.getJarEntry("able.properties");
                if (entry != null) {
                    try (final InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)) {
                        this.properties = new Properties();
                        this.properties.load(reader);
                        final Class<?> classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                        final Method method = classEntry.getDeclaredMethod("run");
                        final Object instance = classEntry.newInstance();
                        method.invoke(instance);


                    } catch (Exception e) {
                       e.printStackTrace();

                    }

                }
            } catch (Exception ignore) {
                ignore.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
