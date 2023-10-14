/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.bukkit;

import dev.sergiferry.playernpc.api.NPCLib;
import eu.metacloudservice.CloudAPI;
import eu.metacloudservice.api.NpcAPI;
import eu.metacloudservice.api.PluginDriver;
import eu.metacloudservice.async.AsyncCloudAPI;
import eu.metacloudservice.bukkit.commands.NPCCommand;
import eu.metacloudservice.bukkit.events.CloudEvents;
import eu.metacloudservice.bukkit.events.Events;
import eu.metacloudservice.pool.service.entrys.CloudService;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

public class NPCBootstrap extends JavaPlugin {

    private static NPCBootstrap instance;
    public NpcAPI npcAPI;

    @Override
    public void onEnable(){
        instance = this;
        npcAPI = new NpcAPI();
        NPCLib.getInstance().registerPlugin(instance);
        Bukkit.getPluginManager().registerEvents(new Events(), this);
        CloudAPI.getInstance().getEventDriver().registerListener(new CloudEvents());
        PluginDriver.getInstance().register(new NPCCommand());
        WorkerTask workerTask = new WorkerTask();
        workerTask.runTaskTimer(this, 0, 40L);
    }


    public static NPCBootstrap getInstance() {
        return instance;
    }
}
