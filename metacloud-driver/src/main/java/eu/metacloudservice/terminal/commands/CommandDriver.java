/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.commands;


import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;

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
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-is-not-found"));
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
