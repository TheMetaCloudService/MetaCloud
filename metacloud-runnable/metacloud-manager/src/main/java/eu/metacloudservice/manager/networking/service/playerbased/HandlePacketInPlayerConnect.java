package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import eu.metacloudservice.webserver.entry.RouteEntry;
import io.netty.channel.Channel;

import java.util.Objects;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
public class HandlePacketInPlayerConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInPlayerConnect){
            if (!CloudManager.shutdown){
                String service = ((PacketInPlayerConnect) packet).getProxy();

                PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                general.getCloudplayers().removeIf(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName())));
                general.getCloudplayers().add(UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()));
                Driver.getInstance().getWebServer().updateRoute("/cloudplayer/genernal", new ConfigDriver().convert(general));
                CloudPlayerRestCache cache = new CloudPlayerRestCache(((PacketInPlayerConnect) packet).getName(), UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()));
                cache.handleConnect(service);
                cache.setCloudplayerservice("");
                Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()), (new RestDriver()).convert(cache)));

                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutPlayerConnect(((PacketInPlayerConnect) packet).getName(), service));
                Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudPlayerConnectedEvent(((PacketInPlayerConnect) packet).getName(), service, UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName())));
                if (CloudManager.config.isShowConnectingPlayers()){
                    Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-player-connect").replace("%player%", ((PacketInPlayerConnect) packet).getName())
                            .replace("%uuid%", Objects.requireNonNull(UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()))).replace("%proxy%", ((PacketInPlayerConnect) packet).getProxy()));

                }
                CloudManager.serviceDriver.getService(service).handelCloudPlayerConnection(true);
               }
        }
    }
}
