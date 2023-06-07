package eu.metacloudservice.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ConfigDriver {

    protected static final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    private String location;

    public ConfigDriver(String location) {
        this.location = location;
    }

    public ConfigDriver() {
    }

    @SneakyThrows
    public IConfigAdapter read(Class<? extends IConfigAdapter> tClass){

        try (InputStream inputStream = new FileInputStream(this.location)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(inputStream, tClass);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean exists(){
        return new File(this.location).exists();
    }

    @SneakyThrows
    public IConfigAdapter convert(String json, Class<? extends IConfigAdapter> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String convert(IConfigAdapter IConfigAdapter){
        return GSON.toJson(IConfigAdapter);
    }

    public void save(IConfigAdapter IConfigAdapter){
        CompletableFuture.runAsync(() -> {
            try {
                File file = new File(this.location);

                if (!file.exists()) {
                    file.createNewFile();
                }

                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
                    GSON.toJson(IConfigAdapter, writer);
                    writer.flush(); // Manuell Puffer leeren, um sicherzustellen, dass Daten geschrieben werden
                }
            } catch (IOException ignored) {}
        });

    }
}
