package eu.themetacloudservice.module;

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

    private File file;
    public String modulename;
    private Properties properties;

    public ModuleClassLoader(String moduleName){
        this.file = new File("./modules/" + moduleName + ".jar");
        this.modulename = moduleName;
    }



    public Properties readProperties(){
        if (this.file == null) {
            return null;
        }
        try {
            URLClassLoader classLoader = new URLClassLoader(new URL[]{file.toURI().toURL()}, this.getClass().getClassLoader());
            try (JarFile jarFile = new JarFile(file)) {
                JarEntry jarEntry = jarFile.getJarEntry("module.properties");
                if (jarEntry != null) {
                    try (InputStreamReader reader = new InputStreamReader(jarFile.getInputStream(jarEntry), StandardCharsets.UTF_8)) {
                        properties = new Properties();
                        return properties;
                    } catch (Exception ignored) {
                    }

                } else {
                    //todo: IMPORTENT_LVL: 1 | Create a reterned message that says that the Module is not found!!
                }

            } catch (Exception ignored) {
            }
        } catch (Exception e) {

        }
        return null;
    }

    public void reloadModule(){
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

                        Method method = classtoLoad.getDeclaredMethod("reloadModule");

                        Object instance = classtoLoad.newInstance();
                        Object resuls = method.invoke(instance);

                        //todo:  IMPORTENT_LVL: 1 | Create a reterned message that says that the Module is loaded

                    }catch (Exception ee) {

                    }

                }else{
                    //todo: IMPORTENT_LVL: 1 | Create a reterned message that says that the Module is not found!!
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


                    }catch (Exception ee) {

                    }

                }else{
                    //todo: IMPORTENT_LVL: 1 | Create a reterned message that says that the Module is not found!!
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


                    }catch (Exception ee) {

                    }

                }else{
                    //todo: IMPORTENT_LVL: 1 | Create a reterned message that says that the Module is not found!!
                }
            }catch (IOException ignored){}
        }catch (MalformedURLException ignored) {}
    }


}
