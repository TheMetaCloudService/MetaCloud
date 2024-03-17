/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.ap.SerializedPluginDescription;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.velocity.command.HubCommand;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Plugin(id = "metacloudhub", name = "metacloud-hub", version = "1.0.5-RELEASE", authors = "RauchigesEtwas", dependencies = {@Dependency(id = "metacloudapi"), @Dependency(id = "metacloudplugin")})
public class VelocityBootstrap {


    public static ProxyServer proxyServer;
    private final Logger logger;

    @Inject
    public VelocityBootstrap(ProxyServer proxyServer, Logger logger) {
        this.proxyServer = proxyServer;
        this.logger = logger;

        proxyServer.getCommandManager().register("hub", new HubCommand(), "lobby", "l", "leave", "quit");


    }

    @Subscribe
    public void handelInject(ProxyInitializeEvent event){
        new Driver();
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);

    }
    public static CloudService getLobby(Player player){
        if (CloudAPI.getInstance().getServicePool().getServices().stream().filter(service -> service.getGroup().getGroupType().equalsIgnoreCase("LOBBY")).collect(Collectors.toList()).isEmpty()){
            return null;
        }else {
            List<CloudService> cloudServices = CloudAPI.getInstance().getServicePool().getServices().stream()
                    .filter(service -> service.getGroup().getGroupType().equalsIgnoreCase("LOBBY"))
                    .filter(service -> !service.getGroup().isMaintenance())
                    .filter(service -> service.getState() == ServiceState.LOBBY).collect(Collectors.toList())
                    .stream().filter(service -> {
                        if (service.getGroup().getPermission().equalsIgnoreCase("")){
                            return true;
                        }else return player.hasPermission(service.getGroup().getPermission());
                    }).collect(Collectors.toList());


            if (cloudServices.isEmpty()){
                return null;
            }
            List<Integer> priority = new ArrayList<>();
            cloudServices.forEach( service -> priority.add(service.getGroup().getPriority()));
            priority.sort(Collections.reverseOrder());
            int priorty = priority.get(0);
            List<CloudService> lobbys = cloudServices.stream().filter(service -> service.getGroup().getPriority() == priorty).collect(Collectors.toList());
            return  lobbys.get(new Random().nextInt(lobbys.size()));
        }
    }
}
