package eu.metacloudservice.configuration;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

public class ConfigDriver {

    protected static final Gson GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
    private String location;
    private ObjectMapper mapper;

    public ConfigDriver(String location) {
        this.location = location;
        this.mapper = new ObjectMapper();
    }

    public ConfigDriver() {
        this.location = "";
        this.mapper = new ObjectMapper();
    }

    @SneakyThrows
    public IConfigAdapter read(Class<? extends IConfigAdapter> tClass){
        try (InputStream inputStream = new FileInputStream(this.location)) {
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return mapper.readValue(inputStream, tClass);
        } catch (IOException e) {
            return null;
        }
    }

    public boolean exists(){
        return new File(this.location).exists();
    }



    public boolean canBeRead(Class<? extends IConfigAdapter> tClass){
        try {
            Object obj = this.mapper.readValue(new File(this.location), tClass);
            return true;
        } catch (Exception exception){
            return false;
        }
    }

    @SneakyThrows
    public IConfigAdapter convert(String json, Class<? extends IConfigAdapter> tClass){
        try {
            return mapper.readValue(json, tClass);
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
