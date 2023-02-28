package eu.metacloudservice.manager.networking.node;

import eu.metacloudservice.Driver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.networking.in.node.PacketInNodeActionSuccess;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.terminal.enums.Type;
import io.netty.channel.Channel;

public class HandlePacketInNodeActionSuccess implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInNodeActionSuccess){
            if (((PacketInNodeActionSuccess) packet).isLaunched()){
                TaskedEntry entry = CloudManager.serviceDriver.getService(((PacketInNodeActionSuccess) packet).getService()).getEntry();
                Driver.getInstance().getTerminalDriver().logSpeed(Type.INFO, "Der Service '§f"+((PacketInNodeActionSuccess) packet).getService()+"/"+entry.getUUID()+"§r' wird gestartet 'node: §f"+((PacketInNodeActionSuccess) packet).getNode()+"§r, port: §f"+((PacketInNodeActionSuccess) packet).getPort()+"§r'",
                        "The service '§f"+((PacketInNodeActionSuccess) packet).getService()+"/"+entry.getUUID()+"§r' is starting 'node: §f"+((PacketInNodeActionSuccess) packet).getNode()+"§r, port: §f"+((PacketInNodeActionSuccess) packet).getPort()+"§r'");
                CloudManager.serviceDriver.getService(((PacketInNodeActionSuccess) packet).getService()).getEntry().setUsedPort(((PacketInNodeActionSuccess) packet).getPort());
            }else {
                CloudManager.serviceDriver.unregister(((PacketInNodeActionSuccess) packet).getService());
            }
        }
    }
}
