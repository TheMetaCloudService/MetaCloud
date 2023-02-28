package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCech;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.events.listeners.CloudPlayerDisconnectedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerDisconnect;
import eu.metacloudservice.networking.out.service.PacketOutServiceDisconnected;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import io.netty.channel.Channel;


public class HandlePacketInPlayerDisconnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
         if (packet instanceof PacketInPlayerDisconnect){
             if (!CloudManager.shutdown){
                     CloudPlayerRestCech restCech = (CloudPlayerRestCech)(new RestDriver()).convert(Driver.getInstance().getWebServer().getRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())), CloudPlayerRestCech.class);
                 ManagerConfig config = (ManagerConfig)(new ConfigDriver("./service.json")).read(ManagerConfig.class);
                     CloudManager.serviceDriver.getService(restCech.getCurrentProxy()).handelCloudPlayerConnection(false);
                 if (!restCech.getCurrentService().equalsIgnoreCase("")){
                     CloudManager.serviceDriver.getService(restCech.getCurrentService()).handelCloudPlayerConnection(false);
                 }
                 Driver.getInstance().getWebServer().removeRoute("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()));
                 if (config.isShowConnectingPlayers()){
                     Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '"+ ((PacketInPlayerDisconnect) packet).getName() + "@" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()) + "§r' hat das Netzwerk verlassen",
                             "The player '"+ ((PacketInPlayerDisconnect) packet).getName()  + "@" + UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName()) + "§r' has left the network");
                 }
                 NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServiceDisconnected(((PacketInPlayerDisconnect) packet).getName(), true));
                 CloudManager.eventDriver.executeEvent(new CloudPlayerDisconnectedEvent(((PacketInPlayerDisconnect) packet).getName(), UUIDDriver.getUUID(((PacketInPlayerDisconnect) packet).getName())));

             }
         }
    }
}
