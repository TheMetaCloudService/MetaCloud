package eu.metacloudservice.moduleside;

import eu.metacloudservice.config.*;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.commands.SyncProxyCommand;
import eu.metacloudservice.webserver.entry.RouteEntry;
import org.checkerframework.checker.units.qual.C;

import java.io.File;
import java.util.ArrayList;
import java.util.function.Consumer;

public class MetaModule implements IModule {
    @Override
    public void load() {
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new SyncProxyCommand());
        create();
        set();
    }

    @Override
    public void unload() {
        Driver.getInstance().getWebServer().removeRoute("/module/syncproxy/general");
        Driver.getInstance().getWebServer().removeRoute("/module/syncproxy/configuration");
    }

    @Override
    public void reload() {
        update();
    }


    private static void create(){
        try {
            if (!new File("./modules/syncproxy/config.json").exists()) {
                new File("./modules/syncproxy/").mkdirs();
                Configuration configuration = new Configuration();
                ArrayList<DesignConfig> configs = new ArrayList<>();
                DesignConfig config = new DesignConfig();
                config.setMotdEnabled(true);
                config.setTabEnabled(true);
                config.setTargetGroup("Proxy");
                ArrayList<Motd> maintenance = new ArrayList<>();

                Motd maintenanceLayout02 = new Motd();

                ArrayList<String> maintenancePlayerInfo02 = new ArrayList<>();
                maintenanceLayout02.setProtocol("§8▷ §cMaintenance");
                maintenanceLayout02.setFirstline("§8► §bMetaCloud  §8▷ §7Ready §ffor §7Future | §f1.16-1.19.4");
                maintenanceLayout02.setSecondline("§8➥ ✎ §7The network now in §fMAINTENANCE §8| §8◣ §b%proxy_name% §8◥ ");

                maintenancePlayerInfo02.add("");
                maintenancePlayerInfo02.add("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future");
                maintenancePlayerInfo02.add("");
                maintenancePlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
                maintenancePlayerInfo02.add("  powered by §8▷ §bInvis-CloudDE");
                maintenancePlayerInfo02.add("");
                maintenanceLayout02.setPlayerinfos(maintenancePlayerInfo02);

                maintenance.add(maintenanceLayout02);

                ArrayList<Motd> defaults = new ArrayList<>();

                Motd defaultsLayout02 = new Motd();

                ArrayList<String> defaultsPlayerInfo02 = new ArrayList<>();
                defaultsLayout02.setProtocol("§8▷ §b%online_players%§8/§b%max_players%");
                defaultsLayout02.setFirstline("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future | §f1.16-1.19.4");
                defaultsLayout02.setSecondline("§8➥ ✎ §7The network now §fONLINE §8| §8◣ §b%proxy_name% §8◥");
                defaultsPlayerInfo02.add("");
                defaultsPlayerInfo02.add("§8► §bMetaCloud §8▷ §7Ready §ffor §7Future");
                defaultsPlayerInfo02.add("");
                defaultsPlayerInfo02.add("  Our Discord §8▷ §b4kKEcaP9WC");
                defaultsPlayerInfo02.add("  powered by §8▷ §bInvis-CloudDE");
                defaultsPlayerInfo02.add("");
                defaultsLayout02.setPlayerinfos(defaultsPlayerInfo02);

                defaults.add(defaultsLayout02);

                ArrayList<Tablist> tablist = new ArrayList<>();

                Tablist tablayout01 = new Tablist();
                tablayout01.setHeader("\n      §8► §bMetaCloud §8▷ §7Ready §ffor §7Future §8◄      \n   §8► §7Current server §8▷ §b%service_name% §8◄   \n");

                tablayout01.setFooter("\n§8► §7Web §8▷ §bmetacloudservice.eu §8◄\n   §8► §7Powered by §8▷ §bInvis-CloudDE §8◄   \n");

                tablist.add(tablayout01);

                Tablist tablayout02 = new Tablist();
                tablayout02.setHeader("\n      §8► §bMetaCloud §8▷ §7Ready §ffor §7Future §8◄      \n    §8► §7Current players §8▷  §b%online_players% §r/ §b%max_players% §8◄   \n");
                tablayout02.setFooter("\n§8► §7Web §8▷ §bmetacloudservice.eu §8◄\n   §8► §7Powered by §8▷ §bInvis-CloudDE §8◄   \n");

                tablist.add(tablayout02);
                config.setMaintenancen(maintenance);
                config.setTablist(tablist);
                config.setDefaults(defaults);
                configs.add(config);
                configuration.setConfiguration(configs);


                new ConfigDriver("./modules/syncproxy/config.json").save(configuration);
            }
        }catch (Exception e){
            set();
        }
        set();


    }
    
    public static void set(){


        General general = new General("syncproxy", "1.0.0", "RauchigesEtwas");
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/syncproxy/general", new ConfigDriver().convert(general)));

        try {
            Configuration config = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);
            ArrayList<DesignConfig> designs = new ArrayList<>();
            config.getConfiguration().forEach(designConfig -> {

                ArrayList<Motd> maintenance = new ArrayList<>();
                ArrayList<Motd> defaults = new ArrayList<>();
                ArrayList<Tablist> tablist = new ArrayList<>();

                designConfig.getTablist().forEach(ct -> {
                    Tablist tab = new Tablist();
                    tab.setHeader(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getHeader()));
                    tab.setFooter(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getFooter()));
                    tablist.add(tab);
                });
                designConfig.getDefaults().forEach(cmotd -> {
                    ArrayList<String> playerlist = new ArrayList<>();
                    Motd motd = new Motd();
                    if (cmotd.getProtocol() == null){
                        motd.setProtocol(null);
                    }else {
                        motd.setProtocol(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getProtocol()));
                    }

                    motd.setFirstline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getFirstline()));
                    motd.setSecondline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getSecondline()));
                    cmotd.getPlayerinfos().forEach(s -> {
                        playerlist.add(Driver.getInstance().getMessageStorage().utf8ToUBase64(s));
                    });
                    motd.setPlayerinfos(playerlist);
                    defaults.add(motd);
                });
                designConfig.getMaintenancen().forEach(cmotd -> {
                    ArrayList<String> playerlist = new ArrayList<>();
                    Motd motd = new Motd();
                    if (cmotd.getProtocol() == null){
                        motd.setProtocol(null);
                    }else {
                        motd.setProtocol(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getProtocol()));
                    }

                    motd.setFirstline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getFirstline()));
                    motd.setSecondline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getSecondline()));
                    cmotd.getPlayerinfos().forEach(s -> {
                        playerlist.add(Driver.getInstance().getMessageStorage().utf8ToUBase64(s));
                    });
                    motd.setPlayerinfos(playerlist);
                    maintenance.add(motd);
                });

                DesignConfig moduleConfig = new DesignConfig();
                moduleConfig.setTargetGroup(designConfig.getTargetGroup());
                moduleConfig.setMotdEnabled(designConfig.isMotdEnabled());
                moduleConfig.setTabEnabled(designConfig.isTabEnabled());
                moduleConfig.setMaintenancen(maintenance);
                moduleConfig.setDefaults(defaults);
                moduleConfig.setTablist(tablist);
                designs.add(moduleConfig);
            });

            Configuration update = new Configuration();
            update.setConfiguration(designs);

            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/syncproxy/configuration", new ConfigDriver().convert(update)));
        }catch (Exception e){
            create();
            set();
        }
    }

    public static void update(){

        try {

            Configuration config = (Configuration) new ConfigDriver("./modules/syncproxy/config.json").read(Configuration.class);
            ArrayList<DesignConfig> designs = new ArrayList<>();
            config.getConfiguration().forEach(designConfig -> {

                ArrayList<Motd> maintenance = new ArrayList<>();
                ArrayList<Motd> defaults = new ArrayList<>();
                ArrayList<Tablist> tablist = new ArrayList<>();

                designConfig.getTablist().forEach(ct -> {
                    Tablist tab = new Tablist();
                    tab.setHeader(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getHeader()));
                    tab.setFooter(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getFooter()));
                    tablist.add(tab);
                });
                designConfig.getDefaults().forEach(cmotd -> {
                    ArrayList<String> playerlist = new ArrayList<>();
                    Motd motd = new Motd();
                    if (cmotd.getProtocol() == null){
                        motd.setProtocol(null);
                    }else {
                        motd.setProtocol(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getProtocol()));
                    }

                    motd.setFirstline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getFirstline()));
                    motd.setSecondline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getSecondline()));
                    cmotd.getPlayerinfos().forEach(s -> {
                        playerlist.add(Driver.getInstance().getMessageStorage().utf8ToUBase64(s));
                    });
                    motd.setPlayerinfos(playerlist);
                    defaults.add(motd);
                });
                designConfig.getMaintenancen().forEach(cmotd -> {
                    ArrayList<String> playerlist = new ArrayList<>();
                    Motd motd = new Motd();
                    if (cmotd.getProtocol() == null){
                        motd.setProtocol(null);
                    }else {
                        motd.setProtocol(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getProtocol()));
                    }

                    motd.setFirstline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getFirstline()));
                    motd.setSecondline(Driver.getInstance().getMessageStorage().utf8ToUBase64(cmotd.getSecondline()));
                    cmotd.getPlayerinfos().forEach(s -> {
                        playerlist.add(Driver.getInstance().getMessageStorage().utf8ToUBase64(s));
                    });
                    motd.setPlayerinfos(playerlist);
                    maintenance.add(motd);
                });

                DesignConfig moduleConfig = new DesignConfig();
                moduleConfig.setMaintenancen(maintenance);
                moduleConfig.setDefaults(defaults);
                moduleConfig.setTargetGroup(designConfig.getTargetGroup());
                moduleConfig.setMotdEnabled(designConfig.isMotdEnabled());
                moduleConfig.setTabEnabled(designConfig.isTabEnabled());
                moduleConfig.setTablist(tablist);
                designs.add(moduleConfig);
            });

            Configuration update = new Configuration();
            update.setConfiguration(designs);

            Driver.getInstance().getWebServer().updateRoute("/module/syncproxy/configuration", new ConfigDriver().convert(update));
        }catch (Exception e){
            set();
            update();
        }

    }
}
