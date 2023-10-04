package eu.metacloudservice.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.bungee.command.CloudCommand;
import eu.metacloudservice.bungee.listener.CloudConnectListener;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.subcommands.*;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.*;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {


    private static BungeeBootstrap instance;
    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        new PluginDriver();
                LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        CloudAPI.getInstance().setState(ServiceState.LOBBY, service.getService());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new CloudConnectListener());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand("cloud"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand("metacloud"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand("mc"));
        PluginDriver.getInstance().register(new ExitCommand());
        PluginDriver.getInstance().register(new VersionCommand());
        PluginDriver.getInstance().register(new ReloadCommand());
        PluginDriver.getInstance().register(new ServiceCommand());
        PluginDriver.getInstance().register(new GroupCommand());
        PluginDriver.getInstance().register(new PlayerCommand());

        new TimerBase().schedule(new TimerTask() {
            @Override
            public void run() {
                if (CloudAPI.getInstance().getGroupPool().getGroup(service.getGroup()).isMaintenance()){
                    ProxyServer.getInstance().getPlayers().forEach(player -> {
                       if ( !player.hasPermission("metacloud.connection.maintenance") && !CloudAPI.getInstance().getWhitelist().contains(player.getName())){
                           Messages messages = CloudAPI.getInstance().getMessages();
                           player.disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNetworkIsMaintenance()).replace("&", "§"));
                       }
                    });
                }
                    if (!NettyDriver.getInstance().nettyClient.getChannel().isActive()){
                        System.exit(0);
                    }
            }
        }, 2, 2, TimeUtil.SECONDS);
    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }

    public CloudService getLobby(ProxiedPlayer player){
        if (CloudAPI.getInstance().getServicePool().getServices().isEmpty()){
            return null;
        }else if (CloudAPI.getInstance().getServicePool().getServices().stream().noneMatch(service -> service.getGroup().getGroupType().equalsIgnoreCase("LOBBY")  && service.getState() == ServiceState.LOBBY)){
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

    public CloudService getLobby(ProxiedPlayer player, String kicked){
        if (CloudAPI.getInstance().getServicePool().getServices().isEmpty()){
            return null;
        }else if (CloudAPI.getInstance().getServicePool().getServices().stream().noneMatch(service -> service.getGroup().getGroupType().equals("LOBBY") && service.getState() == ServiceState.LOBBY)){
            return null;
        }
        List<CloudService> services = CloudAPI.getInstance().getServicePool().getServices().stream()
                .filter(service -> service.getGroup().getGroupType().equals("LOBBY"))
                .filter(service -> !service.getGroup().isMaintenance())
                .filter(service -> !service.getName().equals(kicked))
                .filter(service -> service.getState() == ServiceState.LOBBY)
                .filter(service -> service.getGroup().getPermission().equals("") || player.hasPermission(service.getGroup().getPermission())).collect(Collectors.toList());

        if (services.isEmpty()){
            return null;
        }
        List<Integer> priority = new ArrayList<>();
        services.forEach( service -> priority.add(service.getGroup().getPriority()));
        priority.sort(Collections.reverseOrder());
        int priorty = priority.get(0);
        List<CloudService> lobbys = services.stream().filter(service -> service.getGroup().getPriority() == priorty).collect(Collectors.toList());
        if (lobbys.size() == 0){
            return null;
        }
        return  lobbys.get(new Random().nextInt(lobbys.size()));
    }
}
