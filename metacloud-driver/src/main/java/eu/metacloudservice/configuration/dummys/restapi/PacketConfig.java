package eu.metacloudservice.configuration.dummys.restapi;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import java.util.HashMap;

public class PacketConfig implements IConfigAdapter {

    private String logo;
    private String plugin;
    private String api;
    private HashMap<String, String> spigots;
    private HashMap<String, String> bungeecords;
    private HashMap<String, String> modules;


    public PacketConfig(){

    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public HashMap<String, String> getSpigots() {
        return spigots;
    }

    public void setSpigots(HashMap<String, String> spigots) {
        this.spigots = spigots;
    }

    public HashMap<String, String> getBungeecords() {
        return bungeecords;
    }

    public void setBungeecords(HashMap<String, String> bungeecords) {
        this.bungeecords = bungeecords;
    }

    public HashMap<String, String> getModules() {
        return modules;
    }

    public void setModules(HashMap<String, String> modules) {
        this.modules = modules;
    }
}
