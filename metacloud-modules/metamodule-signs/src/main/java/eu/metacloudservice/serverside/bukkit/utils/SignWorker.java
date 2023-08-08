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
import java.util.TimerTask;

public class SignWorker extends Thread{


    private SignLayout onlineLayout, fullLayout, maintenanceLayout, searchingLayout;
    private Integer online, full, maintenance, searching;

    public SignWorker() {
        online = 0;
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

               online = online >= configuration.getOnline().size()-1 ? 0 : online + 1;
               full = full >= configuration.getFull().size()-1 ? 0 : full + 1;
               maintenance = maintenance >= configuration.getMaintenance().size()-1 ? 0 : maintenance + 1;
               searching = searching >= configuration.getSearching().size()-1 ? 0 : searching + 1;

               onlineLayout = configuration.getOnline().get(online);
               fullLayout = configuration.getFull().get(full);
               maintenanceLayout = configuration.getMaintenance().get(maintenance);
               searchingLayout = configuration.getSearching().get(searching);

               CloudAPI.getInstance().getServicePool().getServices().parallelStream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY).forEach(cloudService -> {
                   if (!BukkitBootstrap.getInstance().getSignDriver().isOnSign(cloudService.getName())){
                       CloudSign cloudSign = BukkitBootstrap.getInstance().getSignDriver().findFreeSign(cloudService.getGroup().getGroup());
                       if (cloudSign != null){
                           BukkitBootstrap.getInstance().getSignDriver().updateSign(cloudSign.getUuid(), cloudService.getName());
                       }

                   }
               });

               CloudAPI.getInstance().getServicePool().getServices().parallelStream().filter(cloudService -> cloudService.getState() == ServiceState.IN_GAME).forEach(cloudService -> {
                   if (BukkitBootstrap.getInstance().getSignDriver().isOnSign(cloudService.getName())){
                       CloudSign cloudSign = BukkitBootstrap.getInstance().getSignDriver().getSign(cloudService.getName());
                       if (cloudSign != null)
                           BukkitBootstrap.getInstance().getSignDriver().updateSign(cloudSign.getUuid(), "");
                   }
               });

               if (configuration.isHideFull()){
                   CloudAPI.getInstance().getServicePool().getServices().parallelStream().filter(cloudService -> cloudService.getState() == ServiceState.LOBBY && cloudService.getPlayercount() >= cloudService.getGroup().getMaxPlayers()).forEach(cloudService -> {
                       if (BukkitBootstrap.getInstance().getSignDriver().isOnSign(cloudService.getName())){
                           CloudSign cloudSign = BukkitBootstrap.getInstance().getSignDriver().getSign(cloudService.getName());
                           if (cloudSign != null)
                               BukkitBootstrap.getInstance().getSignDriver().updateSign(cloudSign.getUuid(), "");
                       }
                   });

               }



               Bukkit.getScheduler().runTask(BukkitBootstrap.getInstance(), () -> {
                   for (CloudSign cloudSign : BukkitBootstrap.getInstance().getSignDriver().getCloudSigns()){

                       if (cloudSign == null)
                           continue;
                       Group group = CloudAPI.getInstance().getGroups().stream().filter(group1 -> group1.getGroup().equalsIgnoreCase(cloudSign.getGroup())).findFirst().orElse(null);
                       if (group == null)
                           continue;

                       if (group.isMaintenance()){
                           cloudSign.setLayout(maintenanceLayout);
                       }else if (cloudSign.getServer().isEmpty()){
                           cloudSign.setLayout(searchingLayout);
                       }else {
                           CloudService cloudService = CloudAPI.getInstance().getServicePool().getService(cloudSign.getServer());
                           if (cloudService == null){
                               cloudSign.setLayout(searchingLayout);
                               cloudSign.setServer("");
                           }
                           if (cloudService.getPlayercount() >= group.getMaxPlayers()){
                               cloudSign.setLayout(fullLayout);
                           }else {
                               cloudSign.setLayout(onlineLayout);
                           }
                       }
                   }
               });
           }
       },1 ,1 , TimeUtil.SECONDS);
    }
}
