package eu.metacloudservice.bungee.command;


import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.pool.service.entrys.CloudService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.stream.Collectors;

public class LobbyCommand extends Command {

    public LobbyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            Messages messages = CloudAPI.getInstance().getMessages();
            if (CloudAPI.getInstance().getPlayerPool().getPlayer(player.getName()).getServer().getGroup().getGroupType().equalsIgnoreCase("LOBBY")){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getAlreadyOnFallback()).replace("%PREFIX%", Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix())));
            }else {

                if (BungeeBootstrap.getInstance().getLobby(player) == null){
                    player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getNoFallbackServer()).replace("%PREFIX%", Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix())));

                }else {
                    CloudService fallback = BungeeBootstrap.getInstance().getLobby(player);
                    player.connect(ProxyServer.getInstance().getServerInfo(fallback.getName()));
                    player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getSuccessfullyConnected()).replace("%PREFIX%", Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix())).replace("%SERVICE%", fallback.getName()));

                }

            }

        }
    }
}
