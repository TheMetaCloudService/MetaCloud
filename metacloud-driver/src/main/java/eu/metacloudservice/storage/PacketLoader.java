package eu.metacloudservice.storage;

import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.restapi.GeneralConfig;
import eu.metacloudservice.configuration.dummys.restapi.ModuleConfig;
import eu.metacloudservice.configuration.dummys.restapi.SoftwareConfig;
import lombok.SneakyThrows;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class PacketLoader {
    public PacketLoader() {}

    @SneakyThrows
    public void loadLogo(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=general").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            GeneralConfig updateConfig = (GeneralConfig) new ConfigDriver().convert(rawJson, GeneralConfig.class);

            try (BufferedInputStream in = new BufferedInputStream(new URL(updateConfig.getLogo()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("./local/server-icon.png")) {
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
    public void loadAPI(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=general").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            GeneralConfig updateConfig = (GeneralConfig) new ConfigDriver().convert(rawJson, GeneralConfig.class);

            try (BufferedInputStream in = new BufferedInputStream(new URL(updateConfig.getApi()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("./local/GLOBAL/EVERY/plugins/metacloud-api.jar")) {
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
    public void loadPlugin(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=general").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            GeneralConfig updateConfig = (GeneralConfig) new ConfigDriver().convert(rawJson, GeneralConfig.class);

            try (BufferedInputStream in = new BufferedInputStream(new URL(updateConfig.getPlugin()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream("./local/GLOBAL/EVERY/plugins/metacloud-plugin.jar")) {
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
    public void loadModules(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=nodules").openStream()) {
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
                     FileOutputStream fileOutputStream = new FileOutputStream("./modules/metacloud-" + s + ".jar")) {
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

    public List<String> availableBungeecords(){
        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=software").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            SoftwareConfig updateConfig = (SoftwareConfig) new ConfigDriver().convert(rawJson, SoftwareConfig.class);

            return new ArrayList<>(updateConfig.getBungeecords().keySet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void loadBungee(String bungee, String groupname){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=software").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            SoftwareConfig updateConfig = (SoftwareConfig) new ConfigDriver().convert(rawJson, SoftwareConfig.class);

            updateConfig.getBungeecords().forEach((s, s2) -> {
                if (s.equalsIgnoreCase(bungee)) {
                    try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream("./local/templates/" + groupname + "/" + "server" + ".jar")) {
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

    public List<String> availableSpigots(){
        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=software").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            SoftwareConfig updateConfig = (SoftwareConfig) new ConfigDriver().convert(rawJson, SoftwareConfig.class);

            return new ArrayList<>(updateConfig.getSpigots().keySet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public void loadSpigot(String Spigot, String groupname){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=software").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            SoftwareConfig updateConfig = (SoftwareConfig) new ConfigDriver().convert(rawJson, SoftwareConfig.class);

            updateConfig.getSpigots().forEach((s, s2) -> {
                if (s.equalsIgnoreCase(Spigot)) {
                    try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream("./local/templates/" + groupname + "/" + "server" + ".jar")) {
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

}
