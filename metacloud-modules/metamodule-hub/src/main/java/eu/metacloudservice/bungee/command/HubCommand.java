package eu.metacloudservice.bungee.command;


import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.bungee.BungeeBootstrap;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.service.entrys.CloudService;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class HubCommand extends Command {

    public HubCommand(String name) {
        super(name);
    }

    @Override
    public void execute(CommandSender commandSender, String[] args) {

        if (!(commandSender instanceof ProxiedPlayer proxiedPlayer)) return;
        final Messages messages = CloudAPI.getInstance().getMessages();
        if (CloudAPI.getInstance().getPlayerPool().getPlayer(proxiedPlayer.getName()).getServer().getGroup().getGroupType().equalsIgnoreCase("LOBBY")) {
            proxiedPlayer.sendMessage(TextComponent.fromLegacyText(messages.getMessages().get("alreadyOnFallback").replace("%PREFIX%", messages.getMessages().get("prefix"))));
            return;
        }

        final CloudService fallbackService = BungeeBootstrap.getInstance().getLobby(proxiedPlayer);
        if (fallbackService == null) {
            proxiedPlayer.sendMessage(TextComponent.fromLegacyText(messages.getMessages().get("noFallbackServer").replace("%PREFIX%", messages.getMessages().get("prefix"))));
            return;
        }

        proxiedPlayer.connect(ProxyServer.getInstance().getServerInfo(fallbackService.getName()));
        proxiedPlayer.sendMessage(TextComponent.fromLegacyText(messages.getMessages().get("successfullyConnected").replace("%PREFIX%", messages.getMessages().get("prefix")).replace("%service_name%", fallbackService.getName())));

    }
}
