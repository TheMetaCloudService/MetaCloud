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

public class HubCommand extends Command {

    public HubCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (commandSender instanceof ProxiedPlayer){
            ProxiedPlayer player = (ProxiedPlayer) commandSender;
            Messages messages = CloudAPI.getInstance().getMessages();
            if (CloudAPI.getInstance().getPlayerPool().getPlayer(player.getName()).getServer().getGroup().getGroupType().equalsIgnoreCase("LOBBY")){
                player.sendMessage(messages.getMessages().get("alreadyOnFallback").replace("%PREFIX%", messages.getMessages().get("prefix")));
            }else {

                if (BungeeBootstrap.getInstance().getLobby(player) == null){
                    player.sendMessage(messages.getMessages().get("noFallbackServer").replace("%PREFIX%", messages.getMessages().get("prefix")));

                }else {
                    CloudService fallback = BungeeBootstrap.getInstance().getLobby(player);
                    player.connect(ProxyServer.getInstance().getServerInfo(fallback.getName()));
                    player.sendMessage(messages.getMessages().get("successfullyConnected").replace("%PREFIX%", messages.getMessages().get("prefix")).replace("%service_name%", fallback.getName()));

                }

            }

        }
    }
}
