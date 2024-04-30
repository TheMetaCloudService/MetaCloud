/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bungee;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.Driver;
import eu.metacloudservice.bungee.command.HubCommand;
import eu.metacloudservice.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BungeeBootstrap extends Plugin {



    private static BungeeBootstrap instance;
    @Override
    public void onEnable() {
        instance = this;
        new Driver();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("lobby"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("hub"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("l"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("leave"));
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new HubCommand("quit"));

    }

    public CloudService getLobby(ProxiedPlayer player){
        if (CloudAPI.getInstance().getServicePool().getServices().isEmpty()){
            return null;
        }else if (CloudAPI.getInstance().getServicePool().getServices().stream().filter(service -> service.getGroup().getGroupType().equalsIgnoreCase("LOBBY")).collect(Collectors.toList()).isEmpty()){
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

    public static BungeeBootstrap getInstance() {
        return instance;
    }
}
