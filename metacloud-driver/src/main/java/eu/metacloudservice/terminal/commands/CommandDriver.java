package eu.metacloudservice.terminal.commands;


import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CommandDriver {

    private final List<CommandAdapter> commands;
    public CommandDriver() {
        this.commands = new ArrayList<>();
    }
    public List<CommandAdapter> getCommands() {
        return commands;
    }
    public void registerCommand(CommandAdapter command){
        this.commands.add(command);
    }

    @SneakyThrows
    public void executeCommand(String line){

        CommandAdapter command = getCommand(line.split(" ")[0]);
        String[] args = Driver.getInstance().getMessageStorage().dropFirstString(line.split(" "));
        if(command != null){
            command.performCommand(command, args);
        }else {
            if (Driver.getInstance().getMessageStorage().language.equalsIgnoreCase("DE")){
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "der eingegebene Befehl wurde nicht gefunden bitte tippe '§fhelp§r'");
            }else {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "the entered command was not found please type '§fhelp§r'");
            }
        }
    }

    public CommandAdapter getCommand(String name){
        for (CommandAdapter command : getCommands()){
            if(command.getCommand().equalsIgnoreCase(name)){
                return command;
            }
            if (command.getAliases().contains(name)){
                return command;
            }
        }
        return null;
    }
}
