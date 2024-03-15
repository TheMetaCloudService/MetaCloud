package eu.metacloudservice.velocity;


import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.subcommands.*;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.velocity.command.CloudCommand;
import eu.metacloudservice.velocity.listeners.CloudConnectListener;

import net.kyori.adventure.text.Component;
import org.slf4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

@Plugin(id = "metacloudplugin", name = "metacloud-plugin", version = "1.0.5-RELEASE", authors = "RauchigesEtwas")
public class VelocityBootstrap {

    public static ProxyServer proxyServer;
    private final Logger logger;

    @Inject
    public VelocityBootstrap(ProxyServer proxyServer, Logger logger) {
        VelocityBootstrap.proxyServer = proxyServer;
        this.logger = logger;


    }

    @Subscribe
    public void handelInject(ProxyInitializeEvent event){
        new Driver();
        new PluginDriver();
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        CloudAPI.getInstance().setState(ServiceState.LOBBY, service.getService());
        proxyServer.getCommandManager().register("cloud", new CloudCommand(), "metacloud", "mc");
        PluginDriver.getInstance().register(new ExitCommand());
        PluginDriver.getInstance().register(new VersionCommand());
        PluginDriver.getInstance().register(new ReloadCommand());
        PluginDriver.getInstance().register(new ServiceCommand());
        PluginDriver.getInstance().register(new GroupCommand());
        PluginDriver.getInstance().register(new PlayerCommand());
        proxyServer.getEventManager().register(this, new CloudConnectListener(proxyServer));


        new TimerBase().schedule(new TimerTask() {
            @Override
            public void run() {

                if (CloudAPI.getInstance().getGroupPool().getGroup(service.getGroup()).isMaintenance()){
                    proxyServer.getAllPlayers().forEach(player -> {
                        if ( !player.hasPermission("metacloud.connection.maintenance") && !CloudAPI.getInstance().getWhitelist().contains(player.getUsername())){
                            Messages messages = CloudAPI.getInstance().getMessages();
                            player.disconnect(Component.text(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getMessages().get("kickNetworkIsMaintenance")).replace("&", "§")));
                        }
                    });
                }

                if (!NettyDriver.getInstance().nettyClient.getChannel().isActive()){
                    System.exit(0);
                }
            }
        }, 10, 10, TimeUtil.SECONDS);
    }

    public static CloudService getLobby(Player player){
        if (CloudAPI.getInstance().getServicePool().getServices().stream().noneMatch(service -> service.getGroup().getGroupType().equalsIgnoreCase("LOBBY"))){
            return null;
        }else {
            List<CloudService> cloudServices = CloudAPI.getInstance().getServicePool().getServices().stream()
                    .filter(service -> service.getGroup().getGroupType().equalsIgnoreCase("LOBBY"))
                    .filter(service -> !service.getGroup().isMaintenance())
                    .filter(service -> service.getState() == ServiceState.LOBBY).toList()
                    .stream().filter(service -> {
                        if (service.getGroup().getPermission().equalsIgnoreCase("")) {
                            return true;
                        } else return player.hasPermission(service.getGroup().getPermission());
                    }).toList();


            if (cloudServices.isEmpty()){
                return null;
            }
            List<Integer> priority = new ArrayList<>();
            cloudServices.forEach( service -> priority.add(service.getGroup().getPriority()));
            priority.sort(Collections.reverseOrder());
            int priorty = priority.get(0);
            List<CloudService> lobbys = cloudServices.stream().filter(service -> service.getGroup().getPriority() == priorty).toList();
            return  lobbys.get(new Random().nextInt(lobbys.size()));
        }
    }

    public static CloudService getLobby(Player player, String kicked){
        if (CloudAPI.getInstance().getServicePool().getServices().stream().noneMatch(service -> service.getGroup().getGroupType().equals("LOBBY"))){
            return null;
        }
        List<CloudService> services = CloudAPI.getInstance().getServicePool().getServices().stream()
                .filter(service -> service.getGroup().getGroupType().equals("LOBBY"))
                .filter(service -> !service.getGroup().isMaintenance())
                .filter(service -> !service.getName().equals(kicked))
                .filter(service -> service.getState() == ServiceState.LOBBY)
                .filter(service -> service.getGroup().getPermission().equals("") || player.hasPermission(service.getGroup().getPermission())).toList();

        if (services.isEmpty()){
            return null;
        }
        List<Integer> priority = new ArrayList<>();
        services.forEach( service -> priority.add(service.getGroup().getPriority()));
        priority.sort(Collections.reverseOrder());
        int priorty = priority.get(0);
        List<CloudService> lobbys = services.stream().filter(service -> service.getGroup().getPriority() == priorty).toList();
        if (lobbys.size() == 0){
            return null;
        }
        return  lobbys.get(new Random().nextInt(lobbys.size()));
    }
}
