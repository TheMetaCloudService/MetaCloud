/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.api;

import java.util.ArrayList;
import java.util.List;

public class PluginDriver {
    private final List<PluginCommand> commands;

    private static  PluginDriver instance;

    public PluginDriver() {
        instance = this;
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

    public static PluginDriver getInstance() {
        return instance;
    }
}
