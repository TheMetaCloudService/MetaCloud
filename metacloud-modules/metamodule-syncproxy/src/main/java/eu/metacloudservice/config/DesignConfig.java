package eu.metacloudservice.config;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class DesignConfig {


    private String targetGroup;
    private boolean motdEnabled;
    private boolean tabEnabled;
    private ArrayList<Motd> maintenancen;
    private ArrayList<Motd> defaults;
    private ArrayList<Tablist> tablist;


    public DesignConfig() {
    }




}
