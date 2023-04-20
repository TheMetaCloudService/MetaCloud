package eu.metacloudservice.manager.networking.service.playerbased;

import eu.metacloudservice.Driver;
import eu.metacloudservice.cloudplayer.CloudPlayerRestCache;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.events.listeners.player.CloudPlayerConnectedEvent;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.playerbased.PacketInPlayerConnect;
import eu.metacloudservice.networking.out.service.playerbased.PacketOutPlayerConnect;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.storage.UUIDDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.RestDriver;
import eu.metacloudservice.webserver.dummys.PlayerGeneral;
import eu.metacloudservice.webserver.entry.RouteEntry;
import io.netty.channel.Channel;

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
                ManagerConfig config = (ManagerConfig)(new ConfigDriver("./service.json")).read(ManagerConfig.class);
                NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutPlayerConnect(((PacketInPlayerConnect) packet).getName()));
                Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudPlayerConnectedEvent(((PacketInPlayerConnect) packet).getName(), service, UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName())));
                if (config.isShowConnectingPlayers()){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Spieler '"+ ((PacketInPlayerConnect) packet).getName() + "@" + UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()) +"§f' ist mit dem Proxy '"+ service + "§r' verbunden",
                            "The player '"+ ((PacketInPlayerConnect) packet).getName()+ "@" + UUIDDriver.getUUID(((PacketInPlayerConnect) packet).getName()) + "§f' is connected to the proxy '"+ service+ "§r'");
                }
                CloudManager.serviceDriver.getService(service).handelCloudPlayerConnection(true);
               }
        }
    }
}
