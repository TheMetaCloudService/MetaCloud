package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCache;
import eu.metacloudservice.cloudplayer.offlineplayer.ceched.OfflinePlayerCacheConfiguration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.listeners.player.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.packet.packets.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.packet.packets.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import io.netty.channel.Channel;

import java.util.Objects;

public class HandlePacketInPlayerDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
         if (packet instanceof PacketInPlayerDisconnect){
             if (!CloudManager.shutdown){
                 NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutPlayerDisconnect(((PacketInPlayerDisconnect) packet).getName()));
                 CloudPlayerRestCache restCech = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())), CloudPlayerRestCache.class);

                 if (CloudManager.serviceDriver.getService(restCech.getCloudplayerproxy()) != null){
                     if (CloudManager.serviceDriver.getService(restCech.getCloudplayerproxy()).getEntry().getStatus() != ServiceState.QUEUED)
                       CloudManager.serviceDriver.getService(restCech.getCloudplayerproxy()).handelCloudPlayerConnection(false);
                 }

                 if (!restCech.getCloudplayerservice().equalsIgnoreCase("") && restCech.getCloudplayerservice() != null){
                     if (CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()) != null){
                         if (CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()).getEntry().getStatus() != ServiceState.QUEUED)
                            CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()).handelCloudPlayerConnection(false);
                     }
                 }

                 PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                 general.getCloudplayers().removeIf(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));
                 Driver.getInstance().getWebServer().updateRoute("/cloudplayer/genernal", new ConfigDriver().convert(general));

                 Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudPlayerDisconnectedEvent(((PacketInPlayerDisconnect) packet).getName(), UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));


                 Driver.getInstance().getWebServer().removeRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()));
                 Driver.getInstance().getWebServer().removeRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()));
                 if (CloudManager.config.isShowConnectingPlayers()){
                     Driver.getInstance().getTerminalDriver().log(Type.NETWORK, Driver.getInstance().getLanguageDriver().getLang().getMessage("network-player-disconnect").replace("%player%", ((PacketInPlayerDisconnect) packet).getName())
                             .replace("%uuid%", Objects.requireNonNull(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()))));
                 }

                 if (Driver.getInstance().getOfflinePlayerCacheDriver().readConfig().getPlayerCaches().stream().anyMatch(cp -> cp.getUniqueID().equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())))){
                     OfflinePlayerCacheConfiguration config = Driver.getInstance().getOfflinePlayerCacheDriver().readConfig();
                     OfflinePlayerCache offlinePlayerCache = config.getPlayerCaches().stream().filter(cache1 -> cache1.getUniqueID().equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()))).findFirst().get();
                     offlinePlayerCache.setLastConnected(String.valueOf(System.currentTimeMillis()));
                     config.getPlayerCaches().removeIf(c -> c.getUniqueID().equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));
                     config.getPlayerCaches().add(offlinePlayerCache);
                     Driver.getInstance().getOfflinePlayerCacheDriver().saveConfig(config);
                 }


             }
         }
    }
}
