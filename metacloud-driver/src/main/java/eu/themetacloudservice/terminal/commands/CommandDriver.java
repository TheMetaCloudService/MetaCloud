package eu.themetacloudservice.terminal.commands;


import eu.themetacloudservice.Driver;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

public class CommandDriver {

    private Set<CommandAdapter> commands;

    public CommandDriver() {
        this.commands = new HashSet<>();
    }

    public Set<CommandAdapter> getCommands() {
        return commands;
    }

    public void registerCommand(CommandAdapter command){
        this.commands.add(command);
    }

    public void unregisterCommand(CommandAdapter command){
        this.commands.remove(command);
    }


    @SneakyThrows
    public void executeCommand(String line){

        CommandAdapter command = getCommand(line.split(" ")[0]);
        String[] args = Driver.getInstance().getMessageStorage().dropFirstString(line.split(" "));
        if(command != null){
            command.performCommand(command, args);
        }else {

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
