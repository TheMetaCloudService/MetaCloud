package eu.themetacloudservice.terminal.commands;

import eu.themetacloudservice.terminal.utils.TerminalStorageLine;
import lombok.var;

import java.util.ArrayList;
import java.util.Collections;

public abstract class CommandAdapter {

    private final String command;
    private final String[] aliases;
    private final String DEdescription;
    private final String ENdescription;


    public CommandAdapter(){
        final var annotation = getClass().getAnnotation(CommandInfo.class);

        this.command = annotation.command();
        this.aliases = annotation.aliases();
        this.ENdescription = annotation.ENdescription();
        this.DEdescription = annotation.DEdescription();
    }

    public abstract void performCommand(CommandAdapter command, String[] args);
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
        Collections.addAll(resuls, aliases);
        return resuls;
    }

}
