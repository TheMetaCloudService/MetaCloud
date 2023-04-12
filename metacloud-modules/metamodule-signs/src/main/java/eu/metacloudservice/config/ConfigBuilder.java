package eu.metacloudservice.config;

import java.util.ArrayList;

public class ConfigBuilder {

    private ArrayList<SignLayout> layouts;

    public ConfigBuilder() {
        layouts = new ArrayList<>();
    }
    public ConfigBuilder add(String itemID, String subID, String... lines){
        layouts.add(new SignLayout(lines, itemID, subID));
        return this;
    }

    public ArrayList<SignLayout> build(){
        return layouts;
    }
}
