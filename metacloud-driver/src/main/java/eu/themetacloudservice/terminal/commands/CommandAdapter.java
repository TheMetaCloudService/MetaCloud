package eu.themetacloudservice.terminal.commands;

import eu.themetacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

public abstract class CommandAdapter {

    private String command;
    private String[] aliases;
    private String description;


    public abstract boolean performCommand(CommandAdapter command, String[] args);
    public abstract ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args);


    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAliases() {
        ArrayList<String> resuls = new ArrayList<>();
        for (String al : aliases){
            resuls.add(al);
        }
        return resuls;
    }

}
