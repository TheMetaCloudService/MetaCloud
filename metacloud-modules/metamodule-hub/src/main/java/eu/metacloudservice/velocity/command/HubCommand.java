package eu.metacloudservice.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.velocity.VelocityBootstrap;
import net.kyori.adventure.text.Component;

public class HubCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (!(invocation.source() instanceof Player player)) return;
        final Messages messages = CloudAPI.getInstance().getMessages();
        if (CloudAPI.getInstance().getPlayerPool().getPlayer(player.getUsername()).getServer().getGroup().getGroupType().equalsIgnoreCase("LOBBY")) {
            player.sendMessage(Component.text(messages.getMessages().get("alreadyOnFallback").replace("%PREFIX%", messages.getMessages().get("prefix"))));
            return;
        }

        final CloudService fallBackService = VelocityBootstrap.getLobby(player);
        if (fallBackService == null) {
            player.sendMessage(Component.text((messages.getMessages().get("noFallbackServer").replace("%PREFIX%", messages.getMessages().get("prefix")))));
            return;
        }

        player.createConnectionRequest(VelocityBootstrap.proxyServer.getServer(fallBackService.getName()).get()).connect();
        player.sendMessage(Component.text(messages.getMessages().get("successfullyConnected").replace("%PREFIX%", messages.getMessages().get("prefix")).replace("%service_name%", fallBackService.getName())));
    }
}
