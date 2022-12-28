package eu.themetacloudservice.configuration.dummys.restapi;

import eu.themetacloudservice.configuration.interfaces.IConfigAdapter;

import java.util.HashMap;

public class PacketConfig implements IConfigAdapter {

    private String logo;
    private HashMap<String, String> spigots;
    private HashMap<String, String> bungeecords;
    private HashMap<String, String> modules;


    public PacketConfig(){

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
