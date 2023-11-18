package eu.metacloudservice.module.loader;

import eu.metacloudservice.Driver;
import eu.metacloudservice.module.config.ModuleConfiguration;
import eu.metacloudservice.module.interfaces.IModuleLoader;
import eu.metacloudservice.terminal.enums.Type;

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

public class ModuleLoader implements IModuleLoader {

    private final String jarName;
    private Properties properties;

    public ModuleConfiguration configuration;
    private final File file;

    public ModuleLoader(String jarName) {
        this.file = new File("./modules/" + jarName + ".jar");
        this.jarName = jarName;
    }

    @Override
    public void load() {
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry entry = jarFile.getJarEntry("module.properties");
                if (entry != null) {
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)) {
                        this.properties = new Properties();
                        this.properties.load(reader);
                        this.configuration = new ModuleConfiguration(properties.getProperty("name"), properties.getProperty("author"), properties.getProperty("main"), properties.getProperty("copy"), properties.getProperty("version"));
                        Class classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                        Method method = classEntry.getDeclaredMethod("load");
                        Object instance = classEntry.newInstance();
                        method.invoke(instance);
                        Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-load")
                                .replace("%module_name%", configuration.getName())
                                .replace("%module_version%", configuration.getVersion())
                                .replace("%module_author%", configuration.getAuthor()));

                    } catch (Exception e) {
                        Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-load-error").replace("%module%", getJarName()));
                        e.printStackTrace();

                    }

                } else {
                    Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-no-config-found").replace("%module%", configuration.getName()));
                }
            } catch (Exception ignore) {

                Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-no-config-found").replace("%module%", getJarName()));
                ignore.printStackTrace();
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void unload() {
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry entry = jarFile.getJarEntry("module.properties");
                if (entry != null) {
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)) {
                        this.properties = new Properties();
                        this.properties.load(reader);
                        this.configuration = new ModuleConfiguration(properties.getProperty("name"), properties.getProperty("author"), properties.getProperty("main"), properties.getProperty("copy"), properties.getProperty("version"));

                        Class classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                        Method method = classEntry.getDeclaredMethod("unload");
                        Object instance = classEntry.newInstance();
                        method.invoke(instance);
                    }
                    Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-unload")
                            .replace("%module%", configuration.getName()));
                } else {
                    Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-no-config-found")
                            .replace("%module%", configuration.getName()));

                }
            } catch (Exception ignore) {
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void reload() {
        try {
            URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry entry = jarFile.getJarEntry("module.properties");
                if (entry != null) {
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)) {
                        this.properties = new Properties();
                        this.properties.load(reader);
                        this.configuration = new ModuleConfiguration(properties.getProperty("name"), properties.getProperty("author"), properties.getProperty("main"), properties.getProperty("copy"), properties.getProperty("version"));


                        Class classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                        Method method = classEntry.getDeclaredMethod("reload");
                        Object instance = classEntry.newInstance();
                        method.invoke(instance);

                    }
                    Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-reload")
                            .replace("%module%", configuration.getName()));

                } else {
                    Driver.getInstance().getTerminalDriver().log(Type.MODULE, Driver.getInstance().getLanguageDriver().getLang().getMessage("module-no-config-found")
                            .replace("%module%", configuration.getName()));
                }
            } catch (Exception ignore) {
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getJarName() {
        return jarName;
    }
}
