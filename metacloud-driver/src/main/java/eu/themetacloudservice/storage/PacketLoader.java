package eu.themetacloudservice.storage;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.restapi.PacketConfig;
import lombok.SneakyThrows;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class PacketLoader {
    public PacketLoader() {}

    @SneakyThrows
    public void loadLogo(){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

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
    public void loadBungee(String bungee, String groupname){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

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

    @SneakyThrows
    public void loadSpigot(String Spigot, String groupname){

        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

            updateConfig.getSpigots().forEach((s, s2) -> {
                if (s.equalsIgnoreCase(Spigot.replace("-", ""))) {
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
