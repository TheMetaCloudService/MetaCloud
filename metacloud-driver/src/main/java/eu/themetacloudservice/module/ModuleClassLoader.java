package eu.themetacloudservice.module;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;

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
    private Properties properties;

    public ModuleClassLoader(String moduleName){
        this.file = new File("./INFO/" + moduleName + ".jar");
        this.modulename = moduleName;
    }



    public Properties readProperties(){
        try (JarFile jarFile = new JarFile(file)) {
            JarEntry jarEntry = jarFile.getJarEntry("module.properties");
            if (jarEntry != null) {
                try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                    properties = new Properties();
                    return properties;
                } catch (Exception ignored) {}
            } else {

                Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "die '§fmodule.config§r' wurde in der File '§f" + file + "§r nicht gefunden", "the '§fmodule.config§r' was not found in the file '§f " + file + "§r");

            }

        } catch (Exception ignored) {
        }
        return null;
    }

    public void reloadModule(){
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.config");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        Properties properties = new Properties();
                        properties.load(reader);
                        Class classtoLoad = Class.forName(properties.getProperty("module_main_class"), true, classLoader);
                        Method method = classtoLoad.getDeclaredMethod("reloadModule");
                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+properties.getProperty("module_name")+"§r' wurde geladen 'dev's: §f"+properties.getProperty("module_authors")+"§r, version: §f"+properties.getProperty("module_current_version")+"§r'", "the module '§f"+properties.getProperty("module_name")+"§r' was loaded 'dev's: §f"+properties.getProperty("module_authors")+"§r, version: §f"+properties.getProperty("module_current_version")+"§r'");
                    }catch (Exception ee) {

                    }

                }else{

                    Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "die '§fmodule.config§r' wurde in der File '§f"+file+"§r nicht gefunden", "the '§fmodule.config§r' was not found in the file '§f "+file+"§r");
                }
            }catch (IOException ignored){}
        }catch (MalformedURLException ignored) {}



    }

    public void enableModule(){
        if(file == null){
            return;
        }
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.config");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {


                        Properties properties = new Properties();
                        properties.load(reader);


                        Class classtoLoad = Class.forName(properties.getProperty("mainclass"), true, classLoader);

                        Method method = classtoLoad.getDeclaredMethod("enableModule");

                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);

                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+properties.getProperty("module_name")+"§r' wurde geladen 'dev's: §f"+properties.getProperty("module_authors")+"§r, version: §f"+properties.getProperty("module_current_version")+"§r'", "the module '§f"+properties.getProperty("module_name")+"§r' was loaded 'dev's: §f"+properties.getProperty("module_authors")+"§r, version: §f"+properties.getProperty("module_current_version")+"§r'");

                    }catch (Exception ee) {

                    }

                }else{
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "die '§fmodule.config§r' wurde in der File '§f"+file+"§r nicht gefunden", "the '§fmodule.config§r' was not found in the file '§f "+file+"§r");
                }
            }catch (IOException ignored){}
        }catch (MalformedURLException ignored) {}
    }


    public void disableModule(){
        if(file == null){
            return;
        }
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.config");
                if (jarEntry != null){
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {


                        Properties properties = new Properties();
                        properties.load(reader);
                        Class classtoLoad = Class.forName(properties.getProperty("mainclass"), true, classLoader);
                        Method method = classtoLoad.getDeclaredMethod("disableModule");
                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);
                        Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Das Module '§f"+properties.getProperty("module_name")+"§r' wurde deaktiviert", "the module '§f"+properties.getProperty("module_name")+"§r' was disabled");
                    }catch (Exception ee) {

                    }

                }else{
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "die '§fmodule.config§r' wurde in der File '§f"+file+"§r nicht gefunden", "the '§fmodule.config§r' was not found in the file '§f "+file+"§r");
                }
            }catch (IOException ignored){}
        }catch (MalformedURLException ignored) {}
    }


}
