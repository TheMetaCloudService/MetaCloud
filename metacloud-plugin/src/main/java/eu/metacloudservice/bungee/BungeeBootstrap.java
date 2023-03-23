package eu.metacloudservice.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.command.CloudCommand;
import eu.metacloudservice.bungee.command.LobbyCommand;
import eu.metacloudservice.bungee.listener.CloudConnectListener;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.message.Messages;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import net.md_5.bungee.PacketConstants;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.protocol.packet.Respawn;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {


    private static BungeeBootstrap instance;
    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        LiveService service = (LiveService) new ConfigDriver("./CLOUDSERVICE.json").read(LiveService.class);
        CloudAPI.getInstance().setState(ServiceState.LOBBY, service.getService());
        ProxyServer.getInstance().getPluginManager().registerListener(this, new CloudConnectListener());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCommand("lobby"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCommand("hub"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCommand("l"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new LobbyCommand("leave"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand("cloud"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand("metacloud"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new CloudCommand("mc"));

        new TimerBase().schedule(new TimerTask() {
            @Override
            public void run() {

                if (
                        CloudAPI.getInstance().getGroups().stream().filter(group -> group.getGroup().equalsIgnoreCase(service.getGroup())).findFirst().get().isMaintenance()){
                    ProxyServer.getInstance().getPlayers().forEach(player -> {
                       if ( !player.hasPermission("metacloud.connection.maintenance") && !CloudAPI.getInstance().getWhitelist().contains(player.getName())){
                           Messages messages = CloudAPI.getInstance().getMessages();
                           player.disconnect(Driver.getInstance().getMessageStorage().base64ToUTF8(messages.getKickNetworkIsMaintenance()).replace("&", "§"));
                       }
                    });
                }


            }
        }, 2, 2, TimeUtil.SECONDS);

    }

    public static BungeeBootstrap getInstance() {
        return instance;
    }


    public CloudService getLobby(ProxiedPlayer player){
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

    public CloudService getLobby(ProxiedPlayer player, String kicked){
        if ( CloudAPI.getInstance().getServicePool().getServices().stream()
                .filter(service -> service.getGroup().getGroupType().equals("LOBBY")).collect(Collectors.toList()).isEmpty()){
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
