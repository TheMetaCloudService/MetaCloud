package eu.themetacloudservice.process;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.process.interfaces.ICloudProcess;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CloudProcess implements ICloudProcess {


    private Integer port;
    private String  Name;
    private String  template;
    private String livePath;
    private Group group;
    private Process service;
    private Thread serviceThread;

    public CloudProcess(Integer port, String name, String template, Group group) {
        this.port = port;
        Name = name;
        this.template = template;
        this.livePath = "/live/"+group.getGroup() + "/" + name+"/";
        this.group = group;
    }

    @SneakyThrows
    @Override
    public CloudProcess build() {

        if (!new File("./live/").exists()){
            new File("./live/").mkdirs();
        }

        if (group.isRunStatic()){
            if (!new File("." + livePath + "plugins/").exists()){
                new File("." + livePath + "plugins/").mkdirs();
                FileUtils.copyDirectory(new File("./local/templates/" + template+ "/"), new File("."+ livePath));
            }
        }else {
            new File("." + livePath + "plugins/").mkdirs();
            FileUtils.copyDirectory(new File("./local/templates/" + template+ "/"), new File("."+ livePath));
        }


        FileUtils.copyDirectory(new File("./local/GLOBAL/"), new File("."+ livePath));
        FileUtils.copyFile(new File("./local/server-icon.png"), new File("."+livePath+"server-icon.png"));
        Driver.getInstance().getModuleDriver().getAllProperties().forEach((s, properties) -> {

            if (properties.getProperty("usetype").equalsIgnoreCase("ALL")){
                try {
                    FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("."+livePath+ "/plugins/"+ s +".jar"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (properties.getProperty("usetype").equalsIgnoreCase("LOBBY")){
                if (group.getGroupType().equalsIgnoreCase("LOBBY")){
                    try {
                        FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("."+livePath+ "/plugins/"+ s +".jar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if (properties.getProperty("usetype").equalsIgnoreCase("PROXY")){
                if (group.getGroupType().equalsIgnoreCase("PROXY")){
                    try {
                        FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("."+livePath+ "/plugins/"+ s +".jar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }else if (properties.getProperty("usetype").equalsIgnoreCase("SERVER")){
                if (!group.getGroupType().equalsIgnoreCase("PROXY")){
                    try {
                        FileUtils.copyFile(new File("./modules/"+s+".jar"), new File("."+livePath+ "/plugins/"+ s +".jar"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        if (!group.getGroupType().equalsIgnoreCase("PROXY")){
            File configFile1 = new File(System.getProperty("user.dir") +livePath, "server.properties");
            final FileWriter fileWriter1 = new FileWriter(configFile1);
            fileWriter1.write(Driver.getInstance().getMessageStorage().getSpigotProperty());
            fileWriter1.flush();
            fileWriter1.close();
            File configFile2 = new File(System.getProperty("user.dir") + livePath, "bukkit.yml");
            final FileWriter fileWriter2 = new FileWriter(configFile2);
            fileWriter2.write(Driver.getInstance().getMessageStorage().getSpigotConfiguration());
            fileWriter2.flush();
            fileWriter2.close();
        }else {
            File configFile2 = new File(System.getProperty("user.dir") + livePath, "config.yml");
            final FileWriter fileWriter2 = new FileWriter(configFile2);
            fileWriter2.write(Driver.getInstance().getMessageStorage().getBungeeCordConfiguration(port, group.getMaxPlayers()));
            fileWriter2.flush();
            fileWriter2.close();
        }


        if (new File("./service.json").exists()){

        }else {

        }

        return this;
    }

    @SneakyThrows
    @Override
    public void run() {
        if (group.getGroupType().equalsIgnoreCase("PROXY")){
            this.service = Runtime.getRuntime().exec("java -Xmx" + group.getUsedMemory() + "M -jar server.jar", null , new File(System.getProperty("user.dir") + livePath));
        }else {
            this.service = Runtime.getRuntime().exec("java -Xmx" + group.getUsedMemory() + "M -Dcom.mojang.eula.agree=true  -jar server.jar  -org.spigotmc.netty.disabled=true --port " + port + " --max-players " + group.getMaxPlayers(), null , new File(System.getProperty("user.dir") + livePath));
        }


    }

    @SneakyThrows
    @Override
    public void shutdown() {
        this.service.destroy();
        this.serviceThread.stop();
        this.service.destroyForcibly();

        Thread.sleep(200);

        if (group.isRunStatic()){
            new File("./local/templates/" + template+ "/" + Name+"/").delete();
            new File("./local/templates/" + template+ "/" + Name+"/").mkdirs();
            FileUtils.copyDirectory(new File("." + livePath), new File("./local/templates/" + template+ "/" + Name+"/"));
            Thread.sleep(200);
            FileUtils.deleteDirectory(new File("."+livePath));
            File file = new File("./live/" + group.getGroup());
            if (file.list().length == 0) {
                file.delete();
            }
        }else {
            FileUtils.deleteDirectory(new File("."+livePath));
            File file = new File("./live/" + group.getGroup());
            if (file.list().length == 0) {
                file.delete();
            }
        }

        File file = new File("./live/");
        if (file.list().length == 0) {
            file.delete();
        }

    }
}
