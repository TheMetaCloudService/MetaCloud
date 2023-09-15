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


    public SignLayout onlineLayout, fullLayout, maintenanceLayout, searchingLayout, emptyLayout;
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
        new TimerBase().scheduleAsync(new TimerTask() {
            @Override
            public void run() {
                Configuration configuration = BukkitBootstrap.getInstance().signsAPI.getConfig();

                empty = empty >= configuration.getEmpty().size() - 1 ? 0 : empty + 1;
                online = online >= configuration.getOnline().size() - 1 ? 0 : online + 1;
                full = full >= configuration.getFull().size() - 1 ? 0 : full + 1;
                maintenance = maintenance >= configuration.getMaintenance().size() - 1 ? 0 : maintenance + 1;
                searching = searching >= configuration.getSearching().size() - 1 ? 0 : searching + 1;

                emptyLayout = configuration.getEmpty().get(empty);
                onlineLayout = configuration.getOnline().get(online);
                fullLayout = configuration.getFull().get(full);
                maintenanceLayout = configuration.getMaintenance().get(maintenance);
                searchingLayout = configuration.getSearching().get(searching);
            }
        }, 1, 2 , TimeUtil.SECONDS);
    }
}
