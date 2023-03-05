package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.events.listeners.CloudPlayerConnectedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.entry.RouteEntry;
import io.netty.channel.Channel;

public class HandlePacketInPlayerConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInPlayerConnect){
            if (!CloudManager.shutdown){
                CloudPlayerRestCache cache = new CloudPlayerRestCache(((PacketInPlayerConnect) packet).getName(), "");
                cache.handleConnect(((PacketInPlayerConnect) packet).getProxy());
                cache.setCurrentService("");
                Driver.getInstance().getWebServer().addRoute(new RouteEntry("/cloudplayer/" + UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()), (new RestDriver()).convert(cache)));
                ManagerConfig config = (ManagerConfig)(new ConfigDriver("./service.json")).read(ManagerConfig.class);
                if (config.isShowConnectingPlayers()){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '"+ ((PacketInPlayerConnect) packet).getName() + "@" + UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()) +"§f' ist mit dem Proxy '"+ ((PacketInPlayerConnect) packet).getProxy() + "§r' verbunden",
                            "The player '"+ ((PacketInPlayerConnect) packet).getName()+ "@" + UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()) + "§f' is connected to the proxy '"+ ((PacketInPlayerConnect) packet).getProxy() + "§r'");
                }
                CloudManager.serviceDriver.getService(((PacketInPlayerConnect) packet).getProxy()).handelCloudPlayerConnection(true);
                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutPlayerConnect(((PacketInPlayerConnect) packet).getName()));
                CloudManager.eventDriver.executeEvent(new CloudPlayerConnectedEvent(((PacketInPlayerConnect) packet).getName(), ((PacketInPlayerConnect) packet).getProxy(), UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName())));
            }
        }
    }
}
