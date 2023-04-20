package eu.metacloudservice.moduleside;

import eu.metacloudservice.Driver;
import eu.metacloudservice.config.General;
import eu.metacloudservice.config.groups.GroupConfiguration;
import eu.metacloudservice.config.groups.entry.GroupEntry;
import eu.metacloudservice.config.player.PlayerConfiguration;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.commands.PermissionsCommand;
import eu.metacloudservice.moduleside.drivers.PermissionDriver;
import eu.metacloudservice.moduleside.handlers.CloudPlayerHandler;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class MetaModule implements IModule {

    public static PermissionDriver permissionDriver;

    @Override
    public void load() {
        permissionDriver = new PermissionDriver();
        createConfigurations();
        set();
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new CloudPlayerHandler());
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new PermissionsCommand());
    }

    @Override
    public void unload() {
        Driver.getInstance().getWebServer().removeRoute("/module/permissions/general");
        Driver.getInstance().getWebServer().removeRoute("/module/permissions/configuration");
        permissionDriver.getPlayers().forEach(configuration -> {
            Driver.getInstance().getWebServer().removeRoute("/module/permissions/" + configuration.getUUID());
        });
    }

    @Override
    public void reload() {
        update();
    }

    private void createConfigurations(){
        new File("./modules/permissions/cloudplayers/").mkdirs();
        if (!new File("./modules/permissions/config.json").exists()){

         GroupConfiguration configuration = new GroupConfiguration();
         ArrayList<GroupEntry> groups = new ArrayList<>();

            GroupEntry admin = new GroupEntry();
            HashMap<String, String> includes = new HashMap<>();
            HashMap<String, Boolean> perms = new HashMap<>();
            perms.put("*", true);
            includes.put("member", "LIFETIME");
            admin.setName("admin");
            admin.setPrefix("§bAdmin §8| §7");
            admin.setSuffix("");
            admin.setInclude(includes);
            admin.setTagPower(0);
            admin.setPermissions(perms);
            admin.setDefaultGroup(false);

         GroupEntry defaultGroup = new GroupEntry();
         defaultGroup.setName("member");
         defaultGroup.setPrefix("§7Member §8| §7");
         defaultGroup.setSuffix("");
         defaultGroup.setInclude(new HashMap<>());
         defaultGroup.setTagPower(10);
         defaultGroup.setPermissions(new HashMap<>());
         defaultGroup.setDefaultGroup(true);
            groups.add(admin);
            groups.add(defaultGroup);

            configuration.setGroups(groups);
            new ConfigDriver("./modules/permissions/config.json").save(configuration);
        }
    }


    public static void set(){
        General general = new General("permissions", "1.0.0", "RauchigesEtwas");
        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/permissions/general", new ConfigDriver().convert(general)));
        permissionDriver.getPlayers().forEach(configuration -> {
            Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/permissions/" + configuration.getUUID(), new ConfigDriver().convert(configuration)));
        });

        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/permissions/configuration", new ConfigDriver().convert(translate())));

    }

    public static void update(){
        permissionDriver.getPlayers().forEach(configuration -> {
            if (Driver.getInstance().getWebServer().getRoutes("/module/permissions/" + configuration.getUUID()) == null){
                Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/permissions/" + configuration.getUUID(), new ConfigDriver().convert(configuration)));
            }else {
                Driver.getInstance().getWebServer().updateRoute("/module/permissions/" + configuration.getUUID(), new ConfigDriver().convert(configuration));
            }
        });

        Driver.getInstance().getWebServer().updateRoute("/module/permissions/configuration", new ConfigDriver().convert(translate()));

    }

    private static GroupConfiguration translate(){
        GroupConfiguration configuration = new GroupConfiguration();
        GroupConfiguration raw = (GroupConfiguration) new ConfigDriver("./modules/permissions/config.json").read(GroupConfiguration.class);
        ArrayList<GroupEntry> groups = new ArrayList<>();
        raw.getGroups().forEach(groupEntry -> {
            GroupEntry entry = new GroupEntry();
            entry.setTagPower(groupEntry.getTagPower());
            entry.setPermissions(groupEntry.getPermissions());
            entry.setName(groupEntry.getName());
            entry.setDefaultGroup(groupEntry.isDefaultGroup());
            entry.setInclude(groupEntry.getInclude());
            entry.setPrefix(Driver.getInstance().getMessageStorage().utf8ToUBase64(groupEntry.getPrefix()));
            entry.setSuffix(Driver.getInstance().getMessageStorage().utf8ToUBase64(groupEntry.getSuffix()));
            groups.add(entry);
        });
        configuration.setGroups(groups);
        return configuration;
    }

}
