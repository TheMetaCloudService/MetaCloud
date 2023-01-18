package eu.themetacloudservice.groups;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.groups.interfaces.ITemplateDriver;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class TemplateDriver implements ITemplateDriver {


    @Override
    public void create(String template, boolean bungee) {
        if (!new File("./local/templates/"+ template+ "/").exists()){
            new File("./local/templates/"+ template+ "/").mkdirs();
            if (new File("./service.json").exists()){
                ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                if (bungee){
                    Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template);

                }else {
                    Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion().replace("-", "").replace(".", ""), template);
                }
            }else {
                NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
                if (bungee){
                    Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template);
                }else {
                    Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion().replace("-", "").replace(".", ""), template);
                }
            }
        }

    }

    @SneakyThrows
    @Override
    public void copy(String template, String directory) {
       if (new File("./local/templates/"+ template+ "/").exists()){
           FileUtils.copyDirectory(new File("./local/templates/"+ template+ "/"), new File(directory));
       }
    }

    @Override
    public void delete(String template) {
        if (new File("./local/templates/"+ template+"/").exists()){
            new File("./local/templates/"+ template+"/").delete();
        }
    }

    @Override
    public void install(String template, boolean bungee) {
        if (new File("./service.json").exists()){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            if (bungee){
                Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template);

            }else {
                Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion().replace("-", "").replace(".", ""), template);
            }
        }else {
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            if (bungee){
                Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template);
            }else {
                Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion().replace("-", "").replace(".", ""), template);
            }
        }
    }


    public void installAll(){
        get().forEach(s -> {
            if (!isInstalled(s)){
                install(s, Driver.getInstance().getGroupDriver().getVersionByTemplate(s).equalsIgnoreCase("PROXY"));
            }
        });
    }


    private boolean isInstalled(String template){
        return  new File("./local/templates/" + template + "/server.jar").exists();
    }

    @Override
    public ArrayList<String> get() {

        File file = new File("./local/templates/");
        File[] files = file.listFiles();
        ArrayList<String> templates = new ArrayList<>();
        for (int i = 0; i != (files != null ? files.length : 0); i++) {
            String template = files[i].getName();
            templates.add(template);
        }
        return templates;
    }
}
