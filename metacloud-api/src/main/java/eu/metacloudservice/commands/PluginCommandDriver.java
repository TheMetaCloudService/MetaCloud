/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.commands;

import java.util.ArrayList;
import java.util.List;

public class PluginCommandDriver {
    private final List<PluginCommand> commands;


    public PluginCommandDriver() {
        commands = new ArrayList<>();
    }

    public void register(PluginCommand command){
        commands.add(command);
    }

    public List<PluginCommand> getCommands() {
        return commands;
    }

    public PluginCommand getCommand(String name){
        for (PluginCommand command : getCommands()){
            if(command.getCommand().equalsIgnoreCase(name)){
                return command;
            }
        }
        return null;
    }

}
