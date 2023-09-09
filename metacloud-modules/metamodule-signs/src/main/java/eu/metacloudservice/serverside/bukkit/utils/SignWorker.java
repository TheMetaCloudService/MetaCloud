/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.serverside.bukkit.utils;

import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.SignsAPI;
import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.SignLayout;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.serverside.bukkit.BukkitBootstrap;
import eu.metacloudservice.serverside.bukkit.signs.CloudSign;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;

import java.util.Arrays;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;

public class SignWorker extends Thread{


    private SignLayout onlineLayout, fullLayout, maintenanceLayout, searchingLayout, emptyLayout;
    private Integer online, full, maintenance, searching, empty;

    public SignWorker() {
        online = 0;
        empty = 0;
        full = 0;
        maintenance = 0;
        searching = 0;
    }

    @Override
    public void run() {
        new TimerBase().schedule(new TimerTask() {
            @Override
            public void run() {
                Configuration configuration = BukkitBootstrap.getInstance().signsAPI.getConfig();

                empty = empty >= configuration.getEmpty().size()-1 ? 0 : empty + 1;
                online = online >= configuration.getOnline().size()-1 ? 0 : online + 1;
                full = full >= configuration.getFull().size()-1 ? 0 : full + 1;
                maintenance = maintenance >= configuration.getMaintenance().size()-1 ? 0 : maintenance + 1;
                searching = searching >= configuration.getSearching().size()-1 ? 0 : searching + 1;

                emptyLayout = configuration.getEmpty().get(empty);
                onlineLayout = configuration.getOnline().get(online);
                fullLayout = configuration.getFull().get(full);
                maintenanceLayout = configuration.getMaintenance().get(maintenance);
                searchingLayout = configuration.getSearching().get(searching);
                SignsAPI api = BukkitBootstrap.getInstance().signsAPI;
                BukkitBootstrap.getInstance().getSignDriver().getCloudSigns().forEach(cloudSign -> {

                    if (CloudAPI.getInstance().getGroupsName().parallelStream().noneMatch(s -> s.equalsIgnoreCase(cloudSign.getGroup()))){
                        //DELETE SIGN IF GROUP NOT LONER EXISTS
                        BukkitBootstrap.getInstance().getSignDriver().handleSignRemove(cloudSign.getUuid());
                    }else if (CloudAPI.getInstance().getGroups().parallelStream().filter(group -> group.getGroup().equalsIgnoreCase(cloudSign.getGroup())).findFirst().get().isMaintenance()){
                        //IF GROUP IS IN MAINTENANCE, CHANGE TO EMPTY_SERVER / MAINTENANCE_SIGN
                        cloudSign.setLayout(maintenanceLayout);
                        cloudSign.setServer("");
                    }else if (cloudSign.getServer().equalsIgnoreCase("")){
                        CloudService nextAbleService = CloudAPI.getInstance().getServicePool().getServicesByGroup(cloudSign.getGroup()).parallelStream()
                                .filter(cloudService -> cloudService.getState() == ServiceState.LOBBY)
                                .filter(cloudService -> cloudService.getPlayercount() < cloudService.getGroup().getMaxPlayers())
                                .filter(cloudService -> !BukkitBootstrap.getInstance().getSignDriver().isOnSign(cloudService.getName()))
                                .findFirst().orElse(null);
                        if (nextAbleService == null) {
                            //IF SERVICE IS NOT FOUND, CHANGE TO EMPTY_SERVER / SEARCHING_SIGN
                            cloudSign.setLayout(searchingLayout);
                            cloudSign.setServer("");
                        }else {
                            //IF ALL OVER THIS IS NOT TRUE, CHANGE TO ONLINE_SIGN

                            if (nextAbleService.getPlayers().isEmpty()){
                                cloudSign.setLayout(emptyLayout);
                            }else {
                                cloudSign.setLayout(onlineLayout);
                            }
                            cloudSign.setServer(nextAbleService.getName());
                        }
                    }else if (!CloudAPI.getInstance().getServicePool().serviceNotNull(cloudSign.getServer())){
                        //IF SERVICE IS NOT FOUND, CHANGE TO EMPTY_SERVER / SEARCHING_SIGN
                        cloudSign.setLayout(searchingLayout);
                        cloudSign.setServer("");
                    }else if (CloudAPI.getInstance().getServicePool().getService(cloudSign.getServer()).getState() != ServiceState.LOBBY){
                        //IF SERVICE CHANGE TO INGAME, CHANGE TO EMPTY_SERVER / SEARCHING_SIGN
                        cloudSign.setLayout(searchingLayout);
                        cloudSign.setServer("");
                    }else if (CloudAPI.getInstance().getServicePool().getService(cloudSign.getServer()).getPlayercount() >= CloudAPI.getInstance().getServicePool().getService(cloudSign.getServer()).getGroup().getMaxPlayers()){
                        //IF SERVICE IS FULL, CHANGE TO SEARCHING_SIGN AND SERVER EMPTY OR FULL_SIGN
                        if (api.getConfig().isHideFull()){
                            cloudSign.setLayout(searchingLayout);
                            cloudSign.setServer("");
                        }else {
                            cloudSign.setLayout(fullLayout);
                        }
                    }else {
                        //IF ALL OVER THIS IS NOT TRUE, CHANGE TO ONLINE_SIGN

                        if (CloudAPI.getInstance().getServicePool().getService(cloudSign.getServer()).getPlayers().isEmpty()){
                            cloudSign.setLayout(emptyLayout);
                        }else {
                            cloudSign.setLayout(onlineLayout);
                        }
                        cloudSign.setLayout(onlineLayout);
                    }
                });
            }
        }, 1, 1, TimeUtil.SECONDS);
    }
}
