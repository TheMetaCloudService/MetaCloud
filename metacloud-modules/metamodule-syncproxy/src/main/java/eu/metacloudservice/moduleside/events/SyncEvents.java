/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.moduleside.events;

import eu.metacloudservice.config.Configuration;
import eu.metacloudservice.config.DesignConfig;
import eu.metacloudservice.config.Motd;
import eu.metacloudservice.config.Tablist;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.events.EventProcess;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.IEventAdapter;
import eu.metacloudservice.events.entrys.Priority;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.group.CloudGroupCreateEvent;
import eu.metacloudservice.events.listeners.group.CloudGroupDeleteEvent;

import java.util.ArrayList;

public class SyncEvents implements ICloudListener {


    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudGroupCreateEvent event){

        Configuration configuration = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);
        DesignConfig config = new DesignConfig();
        config.setMotdEnabled(true);
        config.setTabEnabled(true);
        config.setTargetGroup(event.getGroupname());
        ArrayList<Motd> maintenance = new ArrayList<>();

        Motd maintenanceLayout02 = new Motd();

        ArrayList<String> maintenancePlayerInfo02 = new ArrayList<>();
        maintenanceLayout02.setProtocol("§8▷ §cMaintenance");
        maintenanceLayout02.setFirstline("§8► §bMetaCloud  §8▷ §7Ready §ffor §7Future | §f1.16-1.20.1");
        maintenanceLayout02.setSecondline("§8➥ ✎ §7The network now in §fMAINTENANCE §8| §8◣ §b%proxy_name% §8◥ ");

        maintenancePlayerInfo02.add("");
        maintenancePlayerInfo02.add("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future");
        maintenancePlayerInfo02.add("");
        maintenancePlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
        maintenancePlayerInfo02.add("  powered by §8▷ §bVortexHostDE");
        maintenancePlayerInfo02.add("");
        maintenanceLayout02.setPlayerinfos(maintenancePlayerInfo02);

        maintenance.add(maintenanceLayout02);

        ArrayList<Motd> defaults = new ArrayList<>();

        Motd defaultsLayout02 = new Motd();

        ArrayList<String> defaultsPlayerInfo02 = new ArrayList<>();
        defaultsLayout02.setProtocol("§8▷ §b%online_players%§8/§b%max_players%");
        defaultsLayout02.setFirstline("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future | §f1.16-1.20.1");
        defaultsLayout02.setSecondline("§8➥ ✎ §7The network now §fONLINE §8| §8◣ §b%proxy_name% §8◥");
        defaultsPlayerInfo02.add("");
        defaultsPlayerInfo02.add("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future");
        defaultsPlayerInfo02.add("");
        defaultsPlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
        defaultsPlayerInfo02.add("  powered by §8▷ §bVortexHostDE");
        defaultsPlayerInfo02.add("");
        defaultsLayout02.setPlayerinfos(defaultsPlayerInfo02);

        defaults.add(defaultsLayout02);

        ArrayList<Tablist> tablist = new ArrayList<>();

        Tablist tablayout01 = new Tablist();
        tablayout01.setHeader("\n      §8► §bMetaCloud §8▷ §7Ready §ffor §7Future §8◄      \n   §8► §7Current server §8▷ §b%service_name% §8◄   \n");

        tablayout01.setFooter("\n§8► §7Web §8▷ §bmetacloudservice.eu §8◄\n   §8► §7Powered by §8▷ §bVortexHostDE §8◄   \n");

        tablist.add(tablayout01);

        Tablist tablayout02 = new Tablist();
        tablayout02.setHeader("\n      §8► §bMetaCloud §8▷ §7Ready §ffor §7Future §8◄      \n    §8► §7Current players §8▷  §b%online_players% §r/ §b%max_players% §8◄   \n");
        tablayout02.setFooter("\n§8► §7Web §8▷ §bmetacloudservice.eu §8◄\n   §8► §7Powered by §8▷ §bVortexHostDE §8◄   \n");

        tablist.add(tablayout02);
        config.setMaintenancen(maintenance);
        config.setTablist(tablist);
        config.setDefaults(defaults);
        configuration.getConfiguration().add(config);
    }


    @Subscribe(priority = Priority.HIGHEST)
    public void handle(CloudGroupDeleteEvent event){
        Configuration configuration = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);
        configuration.getConfiguration().removeIf(designConfig -> designConfig.getTargetGroup().equals(event.getGroupname()));
    }

}
