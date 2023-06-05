package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.networking.in.node.PacketInNodeActionSuccess;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.webserver.dummys.liveservice.LiveServices;
import io.netty.channel.Channel;

public class HandlePacketInNodeActionSuccess implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInNodeActionSuccess){
            if (((PacketInNodeActionSuccess) packet).isLaunched()){

                TaskedEntry entry = CloudManager.serviceDriver.getService(((PacketInNodeActionSuccess) packet).getService()).getEntry();
                entry.setUsedPort(((PacketInNodeActionSuccess) packet).getPort());
                LiveServices liveServices = (LiveServices) new ConfigDriver().convert(CloudManager.restDriver.get("/services/" + entry.getServiceName().replace(CloudManager.config.getSplitter(), "~")), LiveServices.class);
                liveServices.setPort(((PacketInNodeActionSuccess) packet).getPort());
                Driver.getInstance().getWebServer().updateRoute("/services/" + entry.getServiceName().replace(CloudManager.config.getSplitter(), "~"), new ConfigDriver().convert(liveServices));

                Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+((PacketInNodeActionSuccess) packet).getService()+"§r' wird gestartet 'node: §f"+((PacketInNodeActionSuccess) packet).getNode()+"§r, port: §f"+((PacketInNodeActionSuccess) packet).getPort()+"§r'",
                        "The service '§f"+((PacketInNodeActionSuccess) packet).getService()+"§r' is starting 'node: §f"+((PacketInNodeActionSuccess) packet).getNode()+"§r, port: §f"+((PacketInNodeActionSuccess) packet).getPort()+"§r'");
                CloudManager.serviceDriver.getService(((PacketInNodeActionSuccess) packet).getService()).getEntry().setUsedPort(((PacketInNodeActionSuccess) packet).getPort());
            }else {
                CloudManager.serviceDriver.unregister(((PacketInNodeActionSuccess) packet).getService());
            }
        }
    }
}
