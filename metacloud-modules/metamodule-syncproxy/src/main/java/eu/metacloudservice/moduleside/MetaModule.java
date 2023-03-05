package eu.metacloud.moduleside;

import eu.metacloud.config.Configuration;
import eu.metacloud.config.Motd;
import eu.metacloud.config.Tablist;
import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;

public class MetaModule implements IModule {
    @Override
    public void load() {
        if (!new File("./modules/metacloud-syncproxy/config.json").exists()) {
            new File("./modules/metacloud-syncproxy/").mkdirs();
            Configuration config = new Configuration();

            ArrayList<Motd> maintenance = new ArrayList<>();
            Motd maintenanceLayout01 = new Motd();

            ArrayList<String> maintenancePlayerInfo01 = new ArrayList<>();
            maintenanceLayout01.setProtocol("§cMaintenance");
            maintenanceLayout01.setFirstline("  §b§lMetaCloud§r§8〖 §7Intelligent Minecraft cloudsystem §8〗");
            maintenanceLayout01.setSecondline("§8✎ §7The network is in §cmaintenance §8◣ §b%proxy_name% §8◥ ");

            maintenancePlayerInfo01.add(" §b§lMetaCloud〖 §7Intelligent Minecraft cloudsystem §8〗");
            maintenancePlayerInfo01.add("");
            maintenancePlayerInfo01.add("§7Our Discord: §bhttps://discord.gg/4kKEcaP9WC");
            maintenancePlayerInfo01.add("§7Our Twitter: §b@MetaCloudService");
            maintenancePlayerInfo01.add("§7Our Partner: §bttps://invis-cloud.de/");
            maintenancePlayerInfo01.add("");
            maintenanceLayout01.setPlayerinfos(maintenancePlayerInfo01);

            maintenance.add(maintenanceLayout01);


            Motd maintenanceLayout02 = new Motd();

            ArrayList<String> maintenancePlayerInfo02 = new ArrayList<>();
            maintenanceLayout02.setProtocol("§cMaintenance");
            maintenanceLayout02.setFirstline("  §b§lMetaCloud§r§8〖 §7Intelligent Minecraft cloudsystem §8〗");
            maintenanceLayout02.setSecondline("§8✎ §7visit our partner | https://invis-cloud.de/ ");

            maintenancePlayerInfo02.add(" §b§lMetaCloud〖 §7Intelligent Minecraft cloudsystem §8〗");
            maintenancePlayerInfo02.add("");
            maintenancePlayerInfo02.add("§7Our Discord: §bhttps://discord.gg/4kKEcaP9WC");
            maintenancePlayerInfo02.add("§7Our Twitter: §b@MetaCloudService");
            maintenancePlayerInfo02.add("§7Our Partner: §bttps://invis-cloud.de/");
            maintenancePlayerInfo02.add("");
            maintenanceLayout02.setPlayerinfos(maintenancePlayerInfo02);

            maintenance.add(maintenanceLayout02);

            ArrayList<Motd> defaults = new ArrayList<>();
            Motd defaultsLayout01 = new Motd();

            ArrayList<String> defaultsPlayerInfo01 = new ArrayList<>();
            defaultsLayout01.setProtocol(null);
            defaultsLayout01.setFirstline("  §b§lMetaCloud§r§8〖 §7Intelligent Minecraft cloudsystem§8〗");
            defaultsLayout01.setSecondline("§8✎ §7The network now §bOnline §8◣ §b%proxy_name% §8◥ ");

            defaultsPlayerInfo01.add("  §b§lMetaCloud〖 §7Intelligent Minecraft cloudsystem §8〗");
            defaultsPlayerInfo01.add("");
            defaultsPlayerInfo01.add("§7Our Discord: §bhttps://discord.gg/4kKEcaP9WC");
            defaultsPlayerInfo01.add("§7Our Twitter: §b@MetaCloudService");
            defaultsPlayerInfo01.add("§7Our Partner: §bttps://invis-cloud.de/");
            defaultsPlayerInfo01.add("");
            defaultsLayout01.setPlayerinfos(defaultsPlayerInfo01);

            defaults.add(defaultsLayout01);


            Motd defaultsLayout02 = new Motd();

            ArrayList<String> defaultsPlayerInfo02 = new ArrayList<>();
            defaultsLayout02.setProtocol(null);
            defaultsLayout02.setFirstline("  §b§lMetaCloud§r§8〖 §7Next generation of CloudSystem §8〗");
            defaultsLayout02.setSecondline("§8✎ §7visit our partner | https://invis-cloud.de/ ");

            defaultsPlayerInfo02.add("  §b§lMetaCloud〖 §7Next generation of CloudSystem §8〗");
            defaultsPlayerInfo02.add("");
            defaultsPlayerInfo02.add("§7Our Discord: §bhttps://discord.gg/4kKEcaP9WC");
            defaultsPlayerInfo02.add("§7Our Twitter: §b@MetaCloudService");
            defaultsPlayerInfo02.add("§7Our partner: §bttps://invis-cloud.de/");
            defaultsPlayerInfo02.add("");
            defaultsLayout02.setPlayerinfos(defaultsPlayerInfo02);

            defaults.add(defaultsLayout02);

            ArrayList<Tablist> tablist = new ArrayList<>();

            Tablist tablayout01 = new Tablist();
            tablayout01.setHeader("\n§8◣ §bMetaCloud §8• §7Next generation of CloudSystem §8◥\n§8► §7Current server §8• §b%service_name% §8◄\n");
            tablayout01.setFooter("\n§8► §7Developer §8• §bRauchigesEtwas §8◄\n§8► §7visit our partner §8• §b§bttps://invis-cloud.de §8◄\n");

            tablist.add(tablayout01);

            Tablist tablayout02 = new Tablist();
            tablayout02.setHeader("\n§8◣ §bMetaCloud §8• §7Next generation of CloudSystem §8◥\n§8► §7Current server §8• §b%proxy_name% §8◄\n");
            tablayout02.setFooter("\n§8► §7Developer §8• §bRauchigesEtwas §8◄\n§8► §7visit our website §8• §bhttps://metacloudservice.eu §8◄\n");


            Tablist tablayout03 = new Tablist();
            tablayout03.setHeader("\n§8◣ §bMetaCloud §8• §7Next generation of CloudSystem §8◥\n§8► §7Current players §8• §b%online_players% §7/§b %max_players% §8◄\n");
            tablayout03.setFooter("\n§8► §7Developer §8• §bRauchigesEtwas §8◄\n§8► §7visit our partner §8• §b§bttps://invis-cloud.de §8◄\n");

            tablist.add(tablayout03);


            tablist.add(tablayout02);
            config.setMaintenancen(maintenance);
            config.setDefaults(defaults);
            config.setTablist(tablist);

            new ConfigDriver("./modules/metacloud-syncproxy/config.json").save(config);
        }

        set();
        
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {
        unload();
    }
    
    
    public void set(){
        Configuration config = (Configuration) new ConfigDriver("./modules/metacloud-syncproxy/config.json").read(Configuration.class);

        ArrayList<Motd> maintenance = new ArrayList<>();
        ArrayList<Motd> defaults = new ArrayList<>();
        ArrayList<Tablist> tablist = new ArrayList<>();


        config.getTablist().forEach(ct -> {
            Tablist tab = new Tablist();
            tab.setHeader(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getHeader()));
            tab.setFooter(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getFooter()));
            tablist.add(tab);
        });

        config.getDefaults().forEach(cmotd -> {
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

        config.getMaintenancen().forEach(cmotd -> {
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

        Configuration moduleConfig = new Configuration();
        moduleConfig.setMaintenancen(maintenance);
        moduleConfig.setDefaults(defaults);
        moduleConfig.setTablist(tablist);

        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/modules/syncproxy", new ConfigDriver().convert(moduleConfig)));
    }

    public void update(){
        Configuration config = (Configuration) new ConfigDriver("./modules/metacloud-syncproxy/config.json").read(Configuration.class);

        ArrayList<Motd> maintenance = new ArrayList<>();
        ArrayList<Motd> defaults = new ArrayList<>();
        ArrayList<Tablist> tablist = new ArrayList<>();


        config.getTablist().forEach(ct -> {
            Tablist tab = new Tablist();
            tab.setHeader(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getHeader()));
            tab.setFooter(Driver.getInstance().getMessageStorage().utf8ToUBase64(ct.getFooter()));
            tablist.add(tab);
        });

        config.getDefaults().forEach(cmotd -> {
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

        config.getMaintenancen().forEach(cmotd -> {
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

        Configuration moduleConfig = new Configuration();
        moduleConfig.setMaintenancen(maintenance);
        moduleConfig.setDefaults(defaults);
        moduleConfig.setTablist(tablist);
        Driver.getInstance().getWebServer().updateRoute("/modules/syncproxy", new ConfigDriver().convert(moduleConfig));
    }
}
