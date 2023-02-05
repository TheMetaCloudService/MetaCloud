package eu.themetacloudservice.bungeecord.command;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.bungeecord.CloudPlugin;
import eu.themetacloudservice.bungeecord.utils.LobbyEntry;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.message.Messages;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.function.Consumer;

public class LobbyCommand extends Command {
    public LobbyCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {


        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            Messages messages = (Messages) new ConfigDriver().convert(CloudPlugin.getInstance().getRestDriver().get("/messages"), Messages.class);
            if (CloudPlugin.getInstance().getLobbyDriver().getLobby().stream().anyMatch(lobbyEntry -> lobbyEntry.getName().equalsIgnoreCase(player.getServer().getInfo().getName()))){
                player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getAlreadyOnFallback()).replace("%PREFIX%", Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix())));
            }else {

                if (CloudPlugin.getInstance().getLobbyDriver().getLobby().isEmpty()){
                    player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getNoFallbackServer()).replace("%PREFIX%", Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix())));

                }else {

                    LobbyEntry fallback = CloudPlugin.getInstance().getLobbyDriver().findMatchingLobby(ProxyServer.getInstance().getPlayer(player.getUniqueId()));
                    player.connect(CloudPlugin.getInstance().getServerDriver().getServerInfo(fallback.getName()));
                    player.sendMessage(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getSuccessfullyConnected()).replace("%PREFIX%", Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getPrefix())).replace("%SERVICE", fallback.getName()));

                }

            }

        }
    }
}
