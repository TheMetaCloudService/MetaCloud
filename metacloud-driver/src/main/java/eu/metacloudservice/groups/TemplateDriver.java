package eu.metacloudservice.groups;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.groups.interfaces.ITemplateDriver;
import eu.metacloudservice.terminal.animation.AnimationDriver;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class TemplateDriver implements ITemplateDriver {

    private final String TEMPLATES_DIR = "./local/templates/";
    private final String DEFAULT_DIR = "/default/";


    @Override
    public void create(String template, boolean bungee, boolean isstatic) {
        if (isstatic){
            if (!new File(TEMPLATES_DIR+ template+ DEFAULT_DIR).exists()){
                new File(TEMPLATES_DIR+ template+ DEFAULT_DIR).mkdirs();
                if (new File("./service.json").exists()){
                    ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                    if (bungee){
                        Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template+ "/default");
                        new AnimationDriver().play();

                    }else {
                        Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion(), template+ "/default");
                        new AnimationDriver().play();
                    }
                }else {
                    NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
                    if (bungee){
                        Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template + "/default");
                        new AnimationDriver().play();
                    }else {
                        Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion(), template+ "/default");
                        new AnimationDriver().play();
                    }
                }
            }
        }else {
            if (!new File(TEMPLATES_DIR+ template+ "/").exists()){
                new File(TEMPLATES_DIR+ template+ "/").mkdirs();
                if (new File("./service.json").exists()){
                    ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
                    if (bungee){
                        Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template);
                        new AnimationDriver().play();

                    }else {
                        Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion(), template);
                        new AnimationDriver().play();
                    }
                }else {
                    NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
                    if (bungee){
                        Driver.getInstance().getMessageStorage().packetLoader.loadBungee(config.getBungeecordVersion(), template);
                        new AnimationDriver().play();
                    }else {
                        Driver.getInstance().getMessageStorage().packetLoader.loadSpigot(config.getSpigotVersion(), template);
                        new AnimationDriver().play();
                    }
                }
            }
        }
    }

    @SneakyThrows
    @Override
    public void copy(String template, String directory) {
       if (new File(TEMPLATES_DIR+ template+ "/").exists()){
           FileUtils.copyDirectory(new File(TEMPLATES_DIR+ template+ "/"), new File(directory));
       }
    }

    @Override
    public void delete(String template) {
        if (new File(TEMPLATES_DIR+ template+"/").exists()){
            new File(TEMPLATES_DIR+ template+"/").delete();
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
        return  new File(TEMPLATES_DIR + template + "/server.jar").exists();
    }

    @Override
    public ArrayList<String> get() {

        File file = new File(TEMPLATES_DIR);
        File[] files = file.listFiles();
        ArrayList<String> templates = new ArrayList<>();
        for (int i = 0; i != (files != null ? files.length : 0); i++) {
            String template = files[i].getName();
            templates.add(template);
        }
        return templates;
    }
}
