package eu.themetacloudservice.storage;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.restapi.PacketConfig;
import eu.themetacloudservice.configuration.dummys.restapi.UpdateConfig;
import lombok.SneakyThrows;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.BiConsumer;

public class PacketLoader {
    public PacketLoader() {}

    @SneakyThrows
    public void loadLogo(){
        final InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream();
        final StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

            try (BufferedInputStream in = new BufferedInputStream(new URL(updateConfig.getLogo()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(  "./local/server-icon.png")) {
                byte dataBuffer[] = new byte[1024];

                int bytesRead;

                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } finally {
            bufferedReader.close();
            inputStream.close();
        }
    }


    @SneakyThrows
    public void loadModules(String modules){
        final InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream();
        final StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

            updateConfig.getModules().forEach((s, s2) -> {
                if (s.equalsIgnoreCase(modules)){
                    try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(  "./modules/" + s+ ".jar")) {
                        byte dataBuffer[] = new byte[1024];

                        int bytesRead;

                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } finally {
            bufferedReader.close();
            inputStream.close();
        }
    }


    @SneakyThrows
    public void loadBungee(String bungee, String groupname){
        final InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream();
        final StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

            updateConfig.getBungeecords().forEach((s, s2) -> {
                if (s.equalsIgnoreCase(bungee)){
                    try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(  "./local/templates/"+groupname+"/" + "server"+ ".jar")) {
                        byte dataBuffer[] = new byte[1024];

                        int bytesRead;

                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } finally {
            bufferedReader.close();
            inputStream.close();
        }
    }

    @SneakyThrows
    public void loadSpigot(String Spigot, String groupname){
        final InputStream inputStream = new URL("https://metacloudservice.eu/rest/cloudloader.php").openStream();
        final StringBuilder builder = new StringBuilder();

        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String rawJson = builder.toString();
            PacketConfig updateConfig = (PacketConfig) new ConfigDriver().convert(rawJson, PacketConfig.class);

            updateConfig.getSpigots().forEach((s, s2) -> {
                if (s.equalsIgnoreCase(Spigot.replace("-",""))){
                    try (BufferedInputStream in = new BufferedInputStream(new URL(s2).openStream());
                         FileOutputStream fileOutputStream = new FileOutputStream(  "./local/templates/"+groupname+"/" + "server"+ ".jar")) {
                        byte dataBuffer[] = new byte[1024];

                        int bytesRead;

                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            fileOutputStream.write(dataBuffer, 0, bytesRead);
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });

        } finally {
            bufferedReader.close();
            inputStream.close();
        }
    }

}
