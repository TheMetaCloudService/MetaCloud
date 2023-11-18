package eu.metacloudservice.terminal.commands;

import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;
import java.util.Collections;

public abstract class CommandAdapter {

    private final String command;
    private final String[] aliases;
    private final String description;
    private final String permission;

    public CommandAdapter(){
        final var annotation = getClass().getAnnotation(CommandInfo.class);

        this.command = annotation.command();
        this.aliases = annotation.aliases();
        this.description = annotation.description();
        this.permission = annotation.permission();
    }

    public abstract void performCommand(CommandAdapter command, String[] args);
    public abstract ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args);
    public String getCommand() {
        return command;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getAliases() {
        ArrayList<String> resuls = new ArrayList<>();
        Collections.addAll(resuls, aliases);
        return resuls;
    }
    public String getPermission() {
        return permission;
    }

}
