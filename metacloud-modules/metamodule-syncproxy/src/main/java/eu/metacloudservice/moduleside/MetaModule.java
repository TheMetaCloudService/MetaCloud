package eu.metacloudservice.moduleside;

import eu.metacloudservice.config.*;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.commands.SyncProxyCommand;
import eu.metacloudservice.moduleside.events.SyncEvents;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MetaModule implements IModule {
    @Override
    public void load() {


        create();
        set();
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new SyncProxyCommand());
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new SyncEvents());
    }

    @Override
    public void unload() {
        Driver.getInstance().getWebServer().removeRoute("/module/syncproxy/general");
        Driver.getInstance().getWebServer().removeRoute("/module/syncproxy/configuration");
    }

    @Override
    public void reload() {
        create();
        update();
    }


    private static void create(){

            if (!new File("./modules/syncproxy/config.json").exists()) {
                new File("./modules/syncproxy/").mkdirs();
                Configuration configuration = new Configuration();
                ArrayList<DesignConfig> configs = new ArrayList<>();
                Driver.getInstance().getGroupDriver().getAll().stream().filter(group -> group.getGroupType().equalsIgnoreCase("PROXY")).forEach(group -> {
                    DesignConfig config = new DesignConfig();
                    config.setMotdEnabled(true);
                    config.setTabEnabled(true);
                    config.setTargetGroup(group.getGroup());
                    ArrayList<Motd> maintenance = new ArrayList<>();

                    Motd maintenanceLayout = new Motd();
                    ArrayList<String> maintenancePlayerInfo = new ArrayList<>();
                    maintenanceLayout.setProtocol("§8▷ §c§lMaintenance");
                    maintenanceLayout.setFirstline("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future §8• §f§l1.16-1.20.✘");
                    maintenanceLayout.setSecondline("§8➥ ✎ §7We are in §f§lMAINTENANCE §8| §8◣ §b§l%proxy_name% §8◥");

                    maintenancePlayerInfo.add("");
                    maintenancePlayerInfo.add("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future");
                    maintenancePlayerInfo.add("");
                    maintenancePlayerInfo.add("  Our Discord §8▷ §b4kKEcaP9WC");
                    maintenancePlayerInfo.add("  Developed by §8▷ §b§lRauchigesEtwas");
                    maintenancePlayerInfo.add("");
                    maintenanceLayout.setPlayerinfos(maintenancePlayerInfo);

                    Motd maintenanceLayout02 = new Motd();

                    ArrayList<String> maintenancePlayerInfo02 = new ArrayList<>();
                    maintenanceLayout02.setProtocol("§8▷ §c§lMaintenance");
                    maintenanceLayout02.setFirstline("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future §8• §f§l1.16-1.20.✘");
                    maintenanceLayout02.setSecondline("§8➥ ✂ §7Download this §fCloudsystem §7on §b§lSPIGOT§3§lM");

                    maintenancePlayerInfo02.add("");
                    maintenancePlayerInfo02.add("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future");
                    maintenancePlayerInfo02.add("");
                    maintenancePlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
                    maintenancePlayerInfo02.add("  Developed by §8▷ §b§lRauchigesEtwas");
                    maintenancePlayerInfo02.add("");
                    maintenanceLayout02.setPlayerinfos(maintenancePlayerInfo02);

                    maintenance.add(maintenanceLayout02);

                    ArrayList<Motd> defaults = new ArrayList<>();

                    Motd defaultsLayout = new Motd();
                    ArrayList<String> defaultsPlayerInfo = new ArrayList<>();
                    defaultsLayout.setProtocol("§8▷ §b§l%online_players%§8/§b§l%max_players%");
                    defaultsLayout.setFirstline("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future §8• §f§l1.16-1.20.✘");
                    defaultsLayout.setSecondline("§8➥ ✂ §7Download this §fCloudsystem §7on §b§lSPIGOT§3§lMC");
                    defaultsPlayerInfo.add("");
                    defaultsPlayerInfo.add("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future");
                    defaultsPlayerInfo.add("");
                    defaultsPlayerInfo.add("  Our Discord §8▷ §b§l4kKEcaP9WC");
                    defaultsPlayerInfo.add("  Developed by §8▷ §b§lRauchigesEtwas");
                    defaultsPlayerInfo.add("");
                    defaultsLayout.setPlayerinfos(defaultsPlayerInfo);
                    Motd defaultsLayout02 = new Motd();

                    ArrayList<String> defaultsPlayerInfo02 = new ArrayList<>();
                    defaultsLayout02.setProtocol("§8▷ §b§l%online_players%§8/§b§l%max_players%");
                    defaultsLayout02.setFirstline("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future §8• §f§l1.16-1.20.✘");
                    defaultsLayout02.setSecondline("§8➥ ✎ §7The network now §f§lONLINE §8| §8◣ §b§l%proxy_name% §8◥");
                    defaultsPlayerInfo02.add("");
                    defaultsPlayerInfo02.add("§8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Futuree");
                    defaultsPlayerInfo02.add("");
                    defaultsPlayerInfo02.add("  Our Discord §8▷ §b§l4kKEcaP9WC");
                    defaultsPlayerInfo02.add("  Developed by §8▷ §b§lRauchigesEtwas");
                    defaultsPlayerInfo02.add("");
                    defaultsLayout02.setPlayerinfos(defaultsPlayerInfo02);

                    defaults.add(defaultsLayout);
                    defaults.add(defaultsLayout02);

                    ArrayList<Tablist> tablist = new ArrayList<>();

                    Tablist tablayout01 = new Tablist();
                    tablayout01.setHeader("\n      §8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future §8• §b§l%online_players% §7/ §3§l%max_players% §8◄      \n   §8► §7Your current server §8▷ §b§l%service_name% §8◄   \n");

                    tablayout01.setFooter("\n        §8► §7Discord §8▷ §b§ldiscord.gg/§3§l4kKEcaP9WC §8◄       \n§8► §7Developer §8▷ §b§lReauchiges§3§lEtwas §8◄\n");

                    tablist.add(tablayout01);

                    Tablist tablayout02 = new Tablist();
                    tablayout02.setHeader("\n      §8► §b§lMeta§3§lCloud §8▷ §7Ready §ffor §7Future §8• §b§l%online_players% §7/ §3§l%max_players% §8◄      \n   §8► §7Your current proxy §8▷ §b§l%proxy_name% §8◄   \n");
                    tablayout02.setFooter("\n        §8► §7Discord §8▷ §b§ldiscord.gg/§3§l4kKEcaP9WC §8◄       \n§8► §7Website §8▷ §b§lmetacloud§3§lservice.eu §8◄\n");

                    tablist.add(tablayout02);
                    config.setMaintenancen(maintenance);
                    config.setTablist(tablist);
                    config.setDefaults(defaults);
                    configs.add(config);
                });
                configuration.setConfiguration(configs);


                new ConfigDriver("./modules/syncproxy/config.json").save(configuration);
            }

        new TimerBase().scheduleAsync(new TimerTask() {
            @Override
            public void run() {
                set();
                update();
            }
        }, 5, TimeUtil.SECONDS);


    }
    
    public static void set(){


        Configuration config = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/syncproxy/configuration", new ConfigDriver().convert(config)));
    }

    public static void update(){

        try {

            Configuration config = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);

            Driver.getInstance().getWebServer().updateRoute("/module/syncproxy/configuration", new ConfigDriver().convert(config));
        }catch (Exception e){
            set();
            update();
        }

    }
}
