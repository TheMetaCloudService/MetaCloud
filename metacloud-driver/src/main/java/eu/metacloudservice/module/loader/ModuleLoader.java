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
    private File file;

    public ModuleLoader(String jarName) {
        this.file = new File("./modules/" + jarName + ".jar");
        this.jarName = jarName;
    }

    @Override
    public void load() {
        if (this.file == null){
            return;
        }else {
            try {
                URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                try (JarFile jarFile = new JarFile(file)){
                    JarEntry entry = jarFile.getJarEntry("module.properties");
                    if (entry != null){
                        try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)){
                            this.properties = new Properties();
                            this.properties.load(reader);
                            this.configuration = new ModuleConfiguration(properties.getProperty("name"), properties.getProperty("author"), properties.getProperty("main"), properties.getProperty("copy"), properties.getProperty("version"));
                            Class classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                            Method method = classEntry.getDeclaredMethod("load");
                            Object instance = classEntry.newInstance();
                            method.invoke(instance);
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+configuration.getName()+"§r' wurde geladen 'version: §f"+configuration.getVersion()+" §r| author: §f"+configuration.getAuthor()+"§r'",
                                    "The module '§f"+this.configuration.getName()+"§r' was loaded 'version: §f"+this.configuration.getVersion()+"§r | author: §f"+this.configuration.getAuthor()+"§r'.");

                        }catch (Exception e){
                            Driver.getInstance().getTerminalDriver().logSpeed(Type.WARN, "Im Modul '§f"+getJarName()+"§r' ist ein Fehler aufgetreten, bitte überprüfen Sie Ihre Module",
                                    "An error has occurred in module '§f"+getJarName()+"§r', please check your modules");
                            e.printStackTrace();

                        }

                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.WARN, "Es wurde keine Modulkonfiguration gefunden", "No modules configuration was found");
                    }
                }catch (Exception ignore){

                    Driver.getInstance().getTerminalDriver().logSpeed(Type.WARN, "Im Modul '§f"+getJarName()+"§r' ist ein Fehler aufgetreten, bitte überprüfen Sie Ihre Module (#ERROR_12092)",
                            "An error has occurred in module '§f"+getJarName()+"§r', please check your modules (#ERROR_12092)");
                    ignore.printStackTrace();
                }
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void unload() {
        if (this.file == null){
            return;
        }else {
            try {
                URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                try (JarFile jarFile = new JarFile(file)){
                    JarEntry entry = jarFile.getJarEntry("module.properties");
                    if (entry != null){
                        try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)){
                            this.properties = new Properties();
                            this.properties.load(reader);
                            this.configuration = new ModuleConfiguration(properties.getProperty("name"), properties.getProperty("author"), properties.getProperty("main"), properties.getProperty("copy"), properties.getProperty("version"));

                            Class classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                            Method method = classEntry.getDeclaredMethod("unload");
                            Object instance = classEntry.newInstance();
                            method.invoke(instance);
                        }
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+configuration.getName()+"§r' wurde gestoppt",
                                "The module '§f"+this.configuration.getName()+"§r' was stopped");
                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.WARN, "Es wurde keine Modulkonfiguration gefunden", "No modules configuration was found");
                    }
                }catch (Exception ignore){}
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void reload() {
        if (this.file == null){
            return;
        }else {
            try {
                URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
                try (JarFile jarFile = new JarFile(file)){
                    JarEntry entry = jarFile.getJarEntry("module.properties");
                    if (entry != null){
                        try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(entry), StandardCharsets.UTF_8)){
                            this.properties = new Properties();
                            this.properties.load(reader);
                            this.configuration = new ModuleConfiguration(properties.getProperty("name"), properties.getProperty("author"), properties.getProperty("main"), properties.getProperty("copy"), properties.getProperty("version"));



                            Class classEntry = Class.forName(this.properties.getProperty("main"), true, loader);
                            Method method = classEntry.getDeclaredMethod("reload");
                            Object instance = classEntry.newInstance();
                            method.invoke(instance);

                        }
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+configuration.getName()+"§r' wurde neu geladen",
                                "The module '§f"+this.configuration.getName()+"§r' was reloaded");

                    }else {
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.WARN, "Es wurde keine Modulkonfiguration gefunden", "No modules configuration was found");
                    }
                }catch (Exception ignore){}
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String getJarName() {
        return jarName;
    }
}
