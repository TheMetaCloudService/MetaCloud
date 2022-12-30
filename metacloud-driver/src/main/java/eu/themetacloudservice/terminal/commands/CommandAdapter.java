package eu.themetacloudservice.terminal.commands;

import eu.themetacloudservice.terminal.utils.TerminalStorageLine;
import lombok.var;

import java.util.ArrayList;

public abstract class CommandAdapter {

    private String command;
    private String[] aliases;
    private String DEdescription;
    private String ENdescription;


    public CommandAdapter(){
        final var annotation = getClass().getAnnotation(CommandInfo.class);

        this.command = annotation.command();
        this.aliases = annotation.aliases();
        this.ENdescription = annotation.ENdescription();
        this.DEdescription = annotation.DEdescription();
    }

    public abstract boolean performCommand(CommandAdapter command, String[] args);
    public abstract ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args);


    public String getCommand() {
        return command;
    }

    public String getDEdescription() {
        return DEdescription;
    }

    public String getENdescription() {
        return ENdescription;
    }

    public ArrayList<String> getAliases() {
        ArrayList<String> resuls = new ArrayList<>();
        for (String al : aliases){
            resuls.add(al);
        }
        return resuls;
    }

}
