package eu.themetacloudservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;
import eu.themetacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;

import java.io.*;
import java.nio.charset.StandardCharsets;

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

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File(this.location), tClass);
    }


    public boolean exists(){
        return new File(this.location).exists();
    }


    public IConfigAdapter convert(String json, Class<? extends IConfigAdapter> tClass){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, tClass);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convert(IConfigAdapter IConfigAdapter){
        return GSON.toJson(IConfigAdapter);
    }

    public void save(IConfigAdapter IConfigAdapter){
        if(IConfigAdapter != null && this.location != null){
            if(!exists()){
                try {
                    new File(this.location).createNewFile();
                } catch (IOException ignored) {}
            }

            try {
                try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(this.location), StandardCharsets.UTF_8)) {
                    GSON.toJson(IConfigAdapter, writer);
                } catch (IOException ignored) {
                }
            }catch (Exception ignored){}
        }else{
            Driver.getInstance().getTerminalDriver().log(Type.ERROR, "not found");
        }
    }

}
