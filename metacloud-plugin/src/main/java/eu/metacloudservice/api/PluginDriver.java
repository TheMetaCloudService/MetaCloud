/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.api;

import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.enums.Type;
import lombok.SneakyThrows;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

public class PluginDriver {
    private final List<ProxyCommand> commands;

    private static  PluginDriver instance;

    public PluginDriver() {
        instance = this;
        commands = new ArrayList<>();
    }

    public void register(ProxyCommand command){
        commands.add(command);
    }

    public List<ProxyCommand> getCommands() {
        return commands;
    }

    @SneakyThrows
    public void executeCommand(String command, ProxiedPlayer proxiedPlayer, Player player, String[] args){

        getCommand(command).performCommand(getCommand(command), proxiedPlayer, player, args);
    }

    public ProxyCommand getCommand(String name){
        for (ProxyCommand command : getCommands()){
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
