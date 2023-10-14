package eu.metacloudservice.manager.networking.service;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.events.listeners.services.CloudProxyConnectedEvent;
import eu.metacloudservice.events.listeners.services.CloudServiceConnectedEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.service.PacketInServiceConnect;
import eu.metacloudservice.networking.out.service.PacketOutServiceConnected;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import io.netty.channel.Channel;

import java.util.TimerTask;

public class HandlePacketInServiceConnect implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {


        if (packet instanceof PacketInServiceConnect){
            if (CloudManager.serviceDriver.getService(((PacketInServiceConnect) packet).getService()) == null){

                channel.disconnect();
            }else if (NettyDriver.getInstance().nettyServer.isChannelFound(((PacketInServiceConnect) packet).getService())){

                channel.disconnect();
            }else {
                NettyDriver.getInstance().nettyServer.registerChannel(((PacketInServiceConnect) packet).getService(), channel);
                TaskedEntry entry = CloudManager.serviceDriver.getService(((PacketInServiceConnect) packet).getService()).getEntry();
                Group group = Driver.getInstance().getGroupDriver().load(entry.getGroupName());
                Driver.getInstance().getTerminalDriver().logSpeed(Type.NETWORK, "Der Service '§f"+((PacketInServiceConnect) packet).getService()+"§r' ist nun §ferfolgreich§r verbunden",
                        "The service '§f"+((PacketInServiceConnect) packet).getService()+"§r' is now §fsuccessfully§r connected");



                if (group.getGroupType().equals("PROXY")){
                    NettyDriver.getInstance().nettyServer.sendToAllAsynchronous(new PacketOutServiceConnected(((PacketInServiceConnect) packet).getService(), group.getGroup()));

                    new TimerBase().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            CloudManager.serviceDriver.getServices().forEach(taskedService -> {
                                if ( taskedService.getEntry().getStatus() != ServiceState.STARTED  && taskedService.getEntry().getStatus() != ServiceState.QUEUED) {
                                    channel.writeAndFlush(new PacketOutServiceConnected(taskedService.getEntry().getServiceName(),taskedService.getEntry().getGroupName()));
                                    NettyDriver.getInstance().nettyServer.sendToAllAsynchronous( new PacketOutServiceConnected(taskedService.getEntry().getServiceName(),taskedService.getEntry().getGroupName()));
                                }
                            });

                        }
                    }, 10, TimeUtil.SECONDS);
                    Driver.getInstance().getMessageStorage().eventDriver .executeEvent(new CloudProxyConnectedEvent(((PacketInServiceConnect) packet).getService(), entry.getNode(), entry.getUsedPort(), CloudManager.config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(entry.getNode())).toList().get(0).getAddress(), entry.getGroupName()));

                }else {
                    NettyDriver.getInstance().nettyServer.sendToAllSynchronized(new PacketOutServiceConnected(((PacketInServiceConnect) packet).getService(), group.getGroup()));
                    new TimerBase().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            CloudManager.serviceDriver.getServices().forEach(taskedService -> {
                                if ( taskedService.getEntry().getStatus() != ServiceState.STARTED  && taskedService.getEntry().getStatus() != ServiceState.QUEUED) {
                                    channel.writeAndFlush(new PacketOutServiceConnected(taskedService.getEntry().getServiceName(),taskedService.getEntry().getGroupName()));
                                    NettyDriver.getInstance().nettyServer.sendToAllSynchronized( new PacketOutServiceConnected(taskedService.getEntry().getServiceName(),taskedService.getEntry().getGroupName()));
                                }
                            });

                        }
                    },  10, TimeUtil.SECONDS);

                    Driver.getInstance().getMessageStorage().eventDriver.executeEvent(new CloudServiceConnectedEvent(((PacketInServiceConnect) packet).getService(), entry.getNode(), entry.getUsedPort(), CloudManager.config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equals(entry.getNode())).toList().get(0).getAddress(), entry.getGroupName()));


                }
            }
        }

    }
}
