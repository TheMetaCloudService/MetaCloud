/*
 * This class is by RauchigesEtwas with improvements.
 */

package eu.metacloudservice.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.restapi.ModuleConfig;
import eu.metacloudservice.language.entry.LanguageConfig;
import eu.metacloudservice.language.entry.LanguagePacket;
import eu.metacloudservice.language.entry.LanguagesPacket;
import lombok.SneakyThrows;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageDriver {

    private final LanguagePacket lang;

    public LanguageDriver() {
        lang = new LanguagePacket();
        reload();
    }

    public LanguagePacket getLang() {
        return lang;
    }

    @SneakyThrows
    public ArrayList<String> getSupportedLanguages() {
        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=languages").openStream()) {
            final StringBuilder builder = new StringBuilder();
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String json = builder.toString();
            final ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, LanguagesPacket.class).getLanguages();
        } catch (IOException e) {
           e.printStackTrace();
        }
        return null;
    }

    public void reload() {
        final String language = Driver.getInstance().getMessageStorage().language.toUpperCase();
        final File storageFile = new File("./local/storage/messages.storage");

        if (storageFile.exists()) {
            final LanguageConfig packet = (LanguageConfig) new ConfigDriver("./local/storage/messages.storage").read(LanguageConfig.class);
            lang.update(packet.getMessages());
        } else {
            try {
               final URL apiUrl = new URL("https://metacloudservice.eu/languages/?lang=" + language);
               final HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                        String inputLine;
                        final StringBuilder content = new StringBuilder();

                        while ((inputLine = in.readLine()) != null) {
                            content.append(inputLine);
                        }

                        final ObjectMapper objectMapper = new ObjectMapper();
                        this.lang.update(objectMapper.readValue(content.toString(), HashMap.class));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
