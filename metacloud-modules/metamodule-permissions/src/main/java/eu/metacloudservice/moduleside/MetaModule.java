package eu.metacloudservice.moduleside;


import eu.metacloudservice.Driver;
import eu.metacloudservice.api.CloudPermissionAPI;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.module.extention.IModule;
import eu.metacloudservice.moduleside.commands.PermissionCommand;
import eu.metacloudservice.moduleside.config.Configuration;
import eu.metacloudservice.moduleside.config.IncludedAble;
import eu.metacloudservice.moduleside.config.PermissionAble;
import eu.metacloudservice.moduleside.config.PermissionGroup;
import eu.metacloudservice.moduleside.events.CloudEvents;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.io.File;
import java.util.ArrayList;

public class MetaModule implements IModule {


    public static MetaModule instance;

    @Override
    public void load() {
        instance = this;
        if (!new File("./modules/permissions/config.json").exists()){
            new File("./modules/permissions/").mkdirs();

            ArrayList<PermissionGroup> groups = new ArrayList<>();
            ArrayList<PermissionAble> permission = new ArrayList<>();
            ArrayList<IncludedAble> includedAbles = new ArrayList<>();

            permission.add(new PermissionAble("*", true, "LIFETIME"));
            includedAbles.add(new IncludedAble("member", "LIFETIME"));

            groups.add(new PermissionGroup("admin", false, 0,"§cAdmin §8| §7", "","", "", permission, includedAbles));
            groups.add(new PermissionGroup("member", true, 99,"§eMember §8| §7", "", "", "", new ArrayList<>(), new ArrayList<>()));

            Configuration config = new Configuration(groups,  new ArrayList<>());
            new ConfigDriver("./modules/permissions/config.json").save(config);
        }


        new CloudPermissionAPI();
        Driver.getInstance().getTerminalDriver().getCommandDriver().registerCommand(new PermissionCommand());
        Driver.getInstance().getMessageStorage().eventDriver.registerListener(new CloudEvents());

        Driver.getInstance().getWebServer().addRoute(new RouteEntry("/module/permission/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/permissions/config.json").read(Configuration.class)) ));

    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {
        Driver.getInstance().getWebServer().updateRoute("/module/permission/configuration", new ConfigDriver().convert(new ConfigDriver("./modules/permissions/config.json").read(Configuration.class)));
    }
}
