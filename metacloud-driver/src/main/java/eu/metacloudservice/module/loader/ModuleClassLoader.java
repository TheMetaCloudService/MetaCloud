package eu.metacloudservice.module.loader;

import eu.metacloudservice.Driver;
import eu.metacloudservice.module.configuration.ModuleClassConfig;
import eu.metacloudservice.terminal.enums.Type;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


public class ModuleClassLoader {

    private final File file;
    public String modulename;
    public ModuleClassConfig moduleClassConfig;

    public ModuleClassLoader(String moduleName){
        this.file = new File("./INFO/" + moduleName + ".jar");
        this.modulename = moduleName;
    }


    public ModuleClassConfig readProperties(){
       return moduleClassConfig;
    }



    public void enableModule(){
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.config");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {

                        Properties properties = new Properties();
                        properties.load(reader);
                        Class<?> classLoad = Class.forName(properties.getProperty("module_main"), true, classLoader);
                        Method method = classLoad.getDeclaredMethod("enableModule");
                        Object instance = classLoad.newInstance();
                        this.moduleClassConfig = new ModuleClassConfig(properties.getProperty("module_name"),
                                properties.getProperty("module_current_version"),
                                properties.getProperty("module_authors"),
                                properties.getProperty("module_copy"));
                        method.invoke(instance);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+properties.getProperty("module_name")+"§r' wurde geladen 'dev's: §f"+properties.getProperty("module_authors")+"§r, version: §f"+properties.getProperty("module_current_version")+"§r'", "the module '§f"+properties.getProperty("module_name")+"§r' was loaded 'dev's: §f"+properties.getProperty("module_authors")+"§r, version: §f"+properties.getProperty("module_current_version")+"§r'");
                    }catch (Exception ignored) {}
                }else{
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Die '§fmodule.config§r' wurde in der File '§f"+file+"§r nicht gefunden", "the '§fmodule.config§r' was not found in the file '§f "+file+"§r");
                }
            }catch (IOException ignored){}
        }catch (MalformedURLException ignored) {}
    }


    public void disableModule(){
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.config");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        Properties properties = new Properties();
                        properties.load(reader);
                        Class<?> classLoad = Class.forName(properties.getProperty("module_main"), true, classLoader);
                        Method method = classLoad.getDeclaredMethod("disableModule");
                        Object instance = classLoad.newInstance();
                        method.invoke(instance);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+properties.getProperty("module_name")+"§r' wurde deaktiviert", "the module '§f"+properties.getProperty("module_name")+"§r' was disabled");
                    }catch (Exception ignored) {}
                }else{
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "die '§fmodule.config§r' wurde in der File '§f"+file+"§r nicht gefunden", "the '§fmodule.config§r' was not found in the file '§f "+file+"§r");
                }
            }catch (IOException ignored){}
        }catch (MalformedURLException ignored) {}
    }


}
