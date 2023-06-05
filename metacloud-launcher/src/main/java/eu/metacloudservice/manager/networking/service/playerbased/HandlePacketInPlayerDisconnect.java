package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.events.listeners.player.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerDisconnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import io.netty.channel.Channel;


public class HandlePacketInPlayerDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
         if (packet instanceof PacketInPlayerDisconnect){
             if (!CloudManager.shutdown){
                 NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutPlayerDisconnect(((PacketInPlayerDisconnect) packet).getName()));
                 CloudPlayerRestCache restCech = (CloudPlayerRestCache)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())), CloudPlayerRestCache.class);

                 if (CloudManager.serviceDriver.getService(restCech.getCloudplayerproxy()) != null){
                     CloudManager.serviceDriver.getService(restCech.getCloudplayerproxy()).handelCloudPlayerConnection(false);
                 }
                 if (!restCech.getCloudplayerservice().equalsIgnoreCase("")){

                     if (CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()) != null){
                         CloudManager.serviceDriver.getService(restCech.getCloudplayerservice()).handelCloudPlayerConnection(false);
                     }
                 }

                try {
                    PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                    general.getCloudplayers().removeIf(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));
                    Driver.getInstance().getWebServer().updateRoute("/cloudplayer/genernal", new ConfigDriver().convert(general));
                }catch (Exception e){
                    try {
                        PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                        general.getCloudplayers().removeIf(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));
                        Driver.getInstance().getWebServer().updateRoute("/cloudplayer/genernal", new ConfigDriver().convert(general));
                    }catch (Exception exception){
                        PlayerGeneral general = (PlayerGeneral) new ConfigDriver().convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/genernal"), PlayerGeneral.class);
                        general.getCloudplayers().removeIf(s -> s.equalsIgnoreCase(UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));
                        Driver.getInstance().getWebServer().updateRoute("/cloudplayer/genernal", new ConfigDriver().convert(general));
                    }
                }

                 Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudPlayerDisconnectedEvent(((PacketInPlayerDisconnect) packet).getName(), UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));

                 Driver.getInstance().getWebServer().removeRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()));
                 if (CloudManager.config.isShowConnectingPlayers()){
                     Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '"+ ((PacketInPlayerDisconnect) packet).getName() + "@" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()) + "§r' hat das Netzwerk verlassen",
                             "The player '"+ ((PacketInPlayerDisconnect) packet).getName()  + "@" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()) + "§r' has left the network");
                 }

             }
         }
    }
}
