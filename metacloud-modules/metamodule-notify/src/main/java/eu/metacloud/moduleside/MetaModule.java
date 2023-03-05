package eu.metacloud.moduleside;

import eu.metacloud.config.Configuration;
import eu.metacloudservice.module.extention.IModule;

import java.io.File;

public class MetaModule implements IModule {
    @Override
    public void load() {
        if (!new File("./modules/notify/config.json").exists()){
            Configuration configuration = new Configuration();
            configuration.setProxiedServiceConnected("");
            configuration.setProxiedServiceDiconnected("");
            configuration.setProxiedServicePrepared("");
            configuration.setServiceConnected("");
            configuration.setServiceDiconnected("");
            configuration.setServicePrepared("");
        }
    }

    @Override
    public void unload() {

    }

    @Override
    public void reload() {

    }
}
