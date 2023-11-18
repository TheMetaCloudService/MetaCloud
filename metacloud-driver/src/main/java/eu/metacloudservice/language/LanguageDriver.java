/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.language;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.restapi.ModuleConfig;
import eu.metacloudservice.language.entry.LanguagePacket;
import eu.metacloudservice.language.entry.LanguagesPacket;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class LanguageDriver {

    private LanguagePacket lang;

    public LanguageDriver() {
        lang = new LanguagePacket();
        reload();
    }

    public LanguagePacket getLang() {
        return lang;
    }

    @SneakyThrows
    public ArrayList<String> getSupportedLanguages(){
        try (InputStream inputStream = new URL("https://metacloudservice.eu/rest/?type=languages").openStream()) {
            final StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader;

            bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            int counter;
            while ((counter = bufferedReader.read()) != -1) {
                builder.append((char) counter);
            }
            final String json = builder.toString();
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(json, LanguagesPacket.class).getLanguages();
        }
    }


    public void reload(){
        try {
            URL apiUrl = new URL("https://metacloudservice.eu/languages/?lang="  + Driver.getInstance().getMessageStorage().language.toUpperCase());
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
                String inputLine;
                StringBuilder content = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                ObjectMapper objectMapper = new ObjectMapper();
                this.lang.update(objectMapper.readValue(content.toString(), HashMap.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
