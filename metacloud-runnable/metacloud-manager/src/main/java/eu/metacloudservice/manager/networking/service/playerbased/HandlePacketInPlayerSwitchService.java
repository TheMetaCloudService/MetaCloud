package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.events.listeners.player.CloudPlayerSwitchEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerSwitchService;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerSwitchService;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import io.netty.channel.Channel;

import java.util.Objects;

public class HandlePacketInPlayerSwitchService implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInPlayerSwitchService){
            if (!CloudManager.shutdown){
                CloudPlayerRestCache restCech = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerSwitchService) packet).getName())), CloudPlayerRestCache.class);

                if (CloudManager.config.isShowConnectingPlayers()){
                    Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-player-switch-server").replace("%player%", ((PacketInPlayerSwitchService) packet).getName())
                            .replace("%uuid%", Objects.requireNonNull(UUIDDriver.getUUID(((PacketInPlayerSwitchService) packet).getName()))).replace("%service%", ((PacketInPlayerSwitchService) packet).getServer()));
                }

                if (!restCech.getCloudplayerservice().equalsIgnoreCase("") && restCech.getCloudplayerservice() != null){

                    if (  CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()) != null){
                        if (CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()).getEntry().getStatus() != ServiceState.QUEUED)
                         CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()).handelCloudPlayerConnection(false);
                    }
                }

                String from = restCech.getCloudplayerservice();

                NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutPlayerSwitchService(((PacketInPlayerSwitchService) packet).getName(), ((PacketInPlayerSwitchService) packet).getServer(), from));

                Driver.getInstance().getMessageStorage().eventDriver .executeEvent(new CloudPlayerSwitchEvent(((PacketInPlayerSwitchService) packet).getName(), UUIDDriver.getUUID(((PacketInPlayerSwitchService) packet).getName()), restCech.getCloudplayerservice(), ((PacketInPlayerSwitchService) packet).getServer()));
                CloudManager.serviceDriver.getService(((PacketInPlayerSwitchService) packet).getServer()).handelCloudPlayerConnection(true);
                restCech.setCloudplayerservice(((PacketInPlayerSwitchService) packet).getServer());
                Driver.getInstance().getWebServer().updateRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerSwitchService) packet).getName()), (new RestDriver()).convert(restCech));

            }
        }
    }
}
