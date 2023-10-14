package eu.metacloudservice.velocity.command;

import com.velocitypowered.api.command.SimpleCommand;
import com.velocitypowered.api.proxy.Player;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.velocity.VelocityBootstrap;
import net.kyori.adventure.text.Component;

public class HubCommand implements SimpleCommand {
    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player){
            Player player = (Player) invocation.source();
            Messages messages = CloudAPI.getInstance().getMessages();
            if (CloudAPI.getInstance().getPlayerPool().getPlayer(player.getUsername()).getServer().getGroup().getGroupType().equalsIgnoreCase("LOBBY")){
                player.sendMessage(Component.text(messages.getMessages().get("alreadyOnFallback").replace("%PREFIX%", messages.getMessages().get("prefix"))));
            }else {
                if (VelocityBootstrap.getLobby(player) == null){
                    player.sendMessage(Component.text((messages.getMessages().get("noFallbackServer").replace("%PREFIX%", messages.getMessages().get("prefix")))));
                  }else {
                    CloudService fallback = VelocityBootstrap.getLobby(player);
                    player.createConnectionRequest(VelocityBootstrap.proxyServer.getServer(fallback.getName()).get());
                    player.sendMessage(Component.text(messages.getMessages().get("successfullyConnected").replace("%PREFIX%", messages.getMessages().get("prefix"))
                            .replace("%service_name%", fallback.getName())));
                }
            }
        }
    }
}
