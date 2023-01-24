package eu.themetacloudservice.process;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.themetacloudservice.groups.dummy.Group;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ServiceProcess {

    private final Group group;
    private final String service;
    private final int port;
    private Process process;
    private final boolean useProtocol;




    public ServiceProcess(Group group, String service, int port, boolean useProtocol) {
        this.group = group;
        this.service = service;
        this.port = port;
        this.useProtocol = useProtocol;
    }

    @SneakyThrows
    public void sync(){
        if (process == null || this.port == 0 || this.service == null || this.group == null){
            return;
        }
        if (!group.isRunStatic()){
            if (            new File("./local/templates/" + group.getStorage().getTemplate() +"/").exists()){
                new File("./local/templates/" + group.getStorage().getTemplate() +"/").delete();
            }
            new File("./local/templates/" + group.getStorage().getTemplate() +"/").delete();
            new File("./local/templates/" + group.getStorage().getTemplate()+"/").mkdirs();
            try {
                FileUtils.copyDirectory(new File("./live/" + group.getGroup() + "/" + service + "/"), new File("./local/templates/" + group.getStorage().getTemplate()+"/"));
            } catch (IOException ignored) {}
        }else {
            if ( new File("./local/templates/" + group.getStorage().getTemplate() +"/" + service + "/").exists()){
                new File("./local/templates/" + group.getStorage().getTemplate() +"/" + service + "/").delete();
            }
            new File("./local/templates/" + group.getStorage().getTemplate()+"/" + service + "/").mkdirs();
            try {
                FileUtils.copyDirectory(new File("./live/" + group.getGroup() + "/" + service + "/"), new File("./local/templates/" + group.getStorage().getTemplate()+"/" + service +"/"));
            } catch (IOException ignored) {}
        };
    }

    public void handelLaunch() {

        if (process != null || this.port == 0 || this.service == null || this.group == null ){
            return;
        }

        if (!Driver.getInstance().getTemplateDriver().get().contains(group.getStorage().getTemplate())) {
            Driver.getInstance().getTemplateDriver().create(group.getStorage().getTemplate(), group.getGroupType().equalsIgnoreCase("PROXY"), group.isRunStatic());
        }
        if (!new File("./live/").exists()) {
            new File("./live/").mkdirs();
        }
        new File("./live/" + group.getGroup() + "/" + service + "/plugins/").mkdirs();

        if (!group.isRunStatic()) {
            Driver.getInstance().getTemplateDriver().copy(group.getStorage().getTemplate(), "./live/" + group.getGroup() + "/" + service + "/");

        } else {
          if (new File("./local/templates/" + group.getGroup() + "/" + service + "/").exists()){
              try {
                  FileUtils.copyDirectory( new File("./local/templates/" + group.getGroup() + "/" +  service +"/"),
                          new File("./live/" + group.getGroup() + "/" + service +"/"));
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }else {
              try {
                  FileUtils.copyDirectory( new File("./local/templates/" + group.getGroup() + "/" + "default/"),
                          new File("./live/" + group.getGroup() + "/" + service +"/"));
              } catch (IOException e) {
                  e.printStackTrace();
              }
          }
        }

        Driver.getInstance().getModuleDriver().getAllProperties().forEach((s, properties) -> {
                if (properties.getProperty("module_copy").equalsIgnoreCase("ALL")){
                    try {
                        FileUtils.copyFile(new File("./modules/" + s + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/"));
                    } catch (IOException ignored) {}
                } else if (properties.getProperty("module_copy").equalsIgnoreCase("LOBBY")){
                    if (group.getGroupType().equalsIgnoreCase("LOBBY")){
                        try {
                            FileUtils.copyFile(new File("./modules/" + s + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/"));
                        } catch (IOException ignored) {}
                    }
            }else if (properties.getProperty("module_copy").equalsIgnoreCase("SERVER")){
                    if (!group.getGroupType().equalsIgnoreCase("PROXY")){
                        try {
                            FileUtils.copyFile(new File("./modules/" + s + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/"));
                        } catch (IOException ignored) {}
                    }
                }else if (properties.getProperty("module_copy").equalsIgnoreCase("PROXY")){
                    if (group.getGroupType().equalsIgnoreCase("PROXY")){
                        try {
                            FileUtils.copyFile(new File("./modules/" + s + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/"));
                        } catch (IOException ignored) {}
                    }
                }
        });

        LiveService liveService = new LiveService();
        liveService.setService(service);

        liveService.setGroup(group.getGroup());
        liveService.setPort(port);
        if (new File("./service.json").exists()){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            liveService.setManagerAddress(config.getManagerAddress());
            liveService.setRunningNode("InternalNode");
            liveService.setRestPort(config.getRestApiCommunication());
            liveService.setNetworkPort(config.getNetworkingCommunication());
        }else {
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            liveService.setManagerAddress(config.getManagerAddress());
            liveService.setRestPort(config.getRestApiCommunication());
            liveService.setRunningNode(config.getNodeName());
            liveService.setNetworkPort(config.getNetworkingCommunication());
        }
        new ConfigDriver("./live/" + group.getGroup() + "/" + service + "/CLOUDSERVICE.json").save(liveService);


        try {
            FileUtils.copyDirectory( new File("./local/GLOBAL/"),
                    new File("./live/" + group.getGroup() + "/" + service +"/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.copyFile(new File("./local/server-icon.png"), new File("./live/" + group.getGroup() + "/" + service+ "/server-icon.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            FileUtils.copyFile(new File("./connection.key"), new File("./live/" + group.getGroup() + "/" + service+ "/connection.key"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File("./live/" + group.getGroup() + "/" + service + "/"));
        if (group.getGroupType().equals("PROXY")) {
            String[] command = new String[]{
                    "java",
                    "-XX:+UseG1GC",
                    "-XX:MaxGCPauseMillis=50",
                    "-XX:-UseAdaptiveSizePolicy",
                    "-XX:CompileThreshold=100",
                    "-Dcom.mojang.eula.agree=true",
                    "-Dio.netty.recycler.maxCapacity=0",
                    "-Dio.netty.recycler.maxCapacity.default=0",
                    "-Djline.terminal=jline.UnsupportedTerminal",
                    "-Xmx" + group.getUsedMemory() + "M",
                    "-jar",
                    "server.jar"
            };
            File configFile = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" + service + "/", "config.yml");
            final FileWriter fileWriter;
            try {
                fileWriter = new FileWriter(configFile);
                fileWriter.write(Driver.getInstance().getMessageStorage().getBungeeCordConfiguration(port, group.getMaxPlayers(), useProtocol));
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


            processBuilder.command(command);
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] command = new String[]{
                    "java",
                    "-XX:+UseG1GC",
                    "-XX:MaxGCPauseMillis=50",
                    "-XX:-UseAdaptiveSizePolicy",
                    "-XX:CompileThreshold=100",
                    "-Dcom.mojang.eula.agree=true",
                    "-Dio.netty.recycler.maxCapacity=0",
                    "-Dio.netty.recycler.maxCapacity.default=0",
                    "-Djline.terminal=jline.UnsupportedTerminal",
                    "-Xmx" + group.getUsedMemory() + "M",
                    "-jar",
                    "server.jar",
                    "--nogui"

            };
            File configFile = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" +service + "/", "server.properties");
            try {
                final FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write(Driver.getInstance().getMessageStorage().getSpigotProperty(port));
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            File configFile2 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/", "bukkit.yml");
            try {
                final FileWriter fileWriter2 = new FileWriter(configFile2);
                fileWriter2.write(Driver.getInstance().getMessageStorage().getSpigotConfiguration());
                fileWriter2.flush();
                fileWriter2.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            processBuilder.command(command);
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @SneakyThrows
    public void handelShutdown(){

        if (process != null && process.isAlive()){
            process.destroy();
            process.destroyForcibly().destroy();
        }

        Thread.sleep(200);

        try {
            if (!group.isRunStatic()){
                FileUtils.deleteDirectory(new File("./live/" +group.getGroup() + "/" + service + "/"));

                new File("./live/" +group.getGroup() + "/" + service + "/").deleteOnExit();
                File file = new File("./live/"+group.getGroup() + "/");
                if (Objects.requireNonNull(file.list()).length == 0) {
                    file.delete();
                }
            }else {
                new File("./local/templates/" + group.getGroup() + "/" +  service +"/").delete();
                new File("./local/templates/" + group.getGroup() + "/" +  service +"/").mkdirs();
                FileUtils.copyDirectory(new File("./live/" +group.getGroup()+"/" + service + "/"), new File("./local/templates/" + group.getGroup() + "/" +  service +"/"));
                Thread.sleep(200);
                FileUtils.deleteDirectory(new File("./live/" +group.getGroup()+"/" + service + "/"));

                File file = new File("./live/"+ group.getGroup() + "/");
                if (Objects.requireNonNull(file.list()).length == 0) {
                    file.delete();
                }
            }
            File file = new File("./live/");
            if (Objects.requireNonNull(file.list()).length == 0) {
                file.delete();
            }
        }catch (IOException | InterruptedException e){}
    }

    public Group getGroup() {
        return group;
    }

    public String getService() {
        return service;
    }

    public int getPort() {
        return port;
    }

    public Process getProcess() {
        return process;
    }

    public boolean isUseProtocol() {
        return useProtocol;
    }
}
