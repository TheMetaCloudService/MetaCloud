/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.storage;

import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.restapi.ModuleConfig;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class ModuleLoader {

    public ModuleLoader() {}

    @SneakyThrows
    private ModuleConfig getModules(){
        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=modules").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            ModuleConfig updateConfig = (ModuleConfig) new ConfigDriver().convert(rawJson, ModuleConfig.class);
            return updateConfig;
        }
    }

    @SneakyThrows
    public boolean updateModule(String name){

        if (new File("./modules/metacloud-" + name.toLowerCase() + ".jar").exists()){
            new File("./modules/metacloud-" + name.toLowerCase() + ".jar").delete();
            try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=modules").openStream()) {
                final StringBuilder builder = new StringBuilder();
                BufferedReader bufferedReader;

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
                int counter;
                while ((counter = bufferedReader.read()) != -1) {
                    builder.append((char) counter);
                }
                final String rawJson = builder.toString();
                ModuleConfig updateConfig = (ModuleConfig) new ConfigDriver().convert(rawJson, ModuleConfig.class);

                if (updateConfig.getModules().isEmpty() && !updateConfig.getModules().containsKey(name)) return false;
                String url =  updateConfig.getModules().get(name);
                try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream("./modules/metacloud-" + name.toLowerCase() + ".jar")) {
                    byte[] dataBuffer = new byte[1024];

                    int bytesRead;

                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }
                    return true;

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        return false;
    }

    @SneakyThrows
    public void downloadModule(String name){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=modules").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            ModuleConfig updateConfig = (ModuleConfig) new ConfigDriver().convert(rawJson, ModuleConfig.class);

            if (updateConfig.getModules().isEmpty() && !updateConfig.getModules().containsKey(name)) return;
            String url =  updateConfig.getModules().get(name);
            try (BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("./modules/metacloud-" + name.toLowerCase() + ".jar")) {
                byte[] dataBuffer = new byte[1024];

                int bytesRead;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public void updateAllModules(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=modules").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            ModuleConfig updateConfig = (ModuleConfig) new ConfigDriver().convert(rawJson, ModuleConfig.class);

            if (updateConfig.getModules().isEmpty()) return;
            updateConfig.getModules().forEach((s, s2) -> {
                if (getModuleNames().stream().anyMatch(s1 -> s1.replace("metacloud-", "").equalsIgnoreCase(s))){
                    new File("./modules/metacloud-" + s.toLowerCase() + ".jar").deleteOnExit();
                    try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream("./modules/metacloud-" + s.toLowerCase() + ".jar")) {
                        byte[] dataBuffer = new byte[1024];

                        int bytesRead;

                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        }
    }

    @SneakyThrows
    public void downloadAllModules(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=modules").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            ModuleConfig updateConfig = (ModuleConfig) new ConfigDriver().convert(rawJson, ModuleConfig.class);

            if (updateConfig.getModules().isEmpty()) return;
            updateConfig.getModules().forEach((s, s2) -> {
                try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                     FileOutputStream fileOutputStream = new FileOutputStream("./modules/metacloud-" + s.toLowerCase() + ".jar")) {
                    byte[] dataBuffer = new byte[1024];

                    int bytesRead;

                    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                        fileOutputStream.write(dataBuffer, 0, bytesRead);
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });

        }
    }

    private ArrayList<String> getModuleNames() {
        File file = new File("./modules/");
        File[] files = file.listFiles();
        ArrayList<String> modules = new ArrayList<>();
        for (int i = 0; i != files.length; i++) {
            String FirstFilter = files[i].getName();
            if (FirstFilter.contains(".jar")) {
                String group = FirstFilter.split(".jar")[0];
                modules.add(group);
            }

        }
        return modules;
    }
}
