/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.networking.command;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.packet.packets.in.service.command.PacketInCommandWhitelist;
import eu.metacloudservice.networking.packet.NettyAdaptor;
import eu.metacloudservice.networking.packet.Packet;
import eu.metacloudservice.webserver.dummys.WhiteList;
import io.netty.channel.Channel;

public class HandlePacketInCommandWhitelist implements NettyAdaptor {
    @Override
    public void handle(Channel channel, Packet packet) {
        if (packet instanceof PacketInCommandWhitelist){
            if (!CloudManager.config.getWhitelist().contains(((PacketInCommandWhitelist) packet).getName())){
                CloudManager.config.getWhitelist().add(((PacketInCommandWhitelist) packet).getName());
                new ConfigDriver("./service.json").save(CloudManager.config);
                WhiteList whitelistConfig = new WhiteList();
                whitelistConfig.setWhitelist(CloudManager.config.getWhitelist());
                Driver.getInstance().getWebServer().updateRoute("/default/whitelist", new ConfigDriver().convert(whitelistConfig));
            }   else {
                CloudManager.config.getWhitelist().remove(((PacketInCommandWhitelist) packet).getName());
                new ConfigDriver("./service.json").save(CloudManager.config);
                WhiteList whitelistConfig = new WhiteList();
                whitelistConfig.setWhitelist(CloudManager.config.getWhitelist());
                Driver.getInstance().getWebServer().updateRoute("/default/whitelist", new ConfigDriver().convert(whitelistConfig));
            }
        }
    }

}
