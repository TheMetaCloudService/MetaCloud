package eu.metacloudservice.process;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.configuration.dummys.serviceconfig.LiveService;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.node.PacketInSendConsole;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class ServiceProcess {

    private final Group group;
    private final String service;
    private final int port;
    private Process process;
    private final boolean useProtocol;
    private boolean useVelocity;
    public boolean useConsole;
    private   BufferedReader reader;
    public final LinkedList<String> consoelStorage;


    public ServiceProcess(Group group, String service, int port, boolean useProtocol) {
        this.group = group;
        this.service = service;
        this.port = port;
        this.useProtocol = useProtocol;
        this.useVelocity = false;
        consoelStorage = new LinkedList<>();
        useConsole = false;


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


    public void handelConsole(){
        if (useConsole){
            useConsole = false;
        }else {
            useConsole = true;
            new Thread(() -> {
                String line;

                consoelStorage.forEach(s -> {
                    if (new File("./service.json").exists()){
                        Driver.getInstance().getTerminalDriver().log(getService(), s);
                    }else {
                        NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInSendConsole(service, s));
                    }
                });
                    try {
                        while ((line = reader.readLine()) != null && useConsole){
                            consoelStorage.add(line);
                            if (new File("./service.json").exists()){
                                Driver.getInstance().getTerminalDriver().log(getService(), line);
                            }else {
                                NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInSendConsole(service, line));
                            }
                        }
                    }catch (Exception e){
                        Driver.getInstance().getMessageStorage().openServiceScreen = false;
                    }

            }).start();
        }
    }

    public void handelLaunch() {

        if (process != null || this.port == 0 || this.service == null || this.group == null ){
            return;
        }

        if (!Driver.getInstance().getTemplateDriver().get().contains(group.getStorage().getTemplate())) {
            Driver.getInstance().getTemplateDriver().create(group.getStorage().getTemplate(), group.getGroupType().equalsIgnoreCase("PROXY"), group.isRunStatic());
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

        if (!Driver.getInstance().getModuleDriver().getLoadedModules().isEmpty()){
            Driver.getInstance().getModuleDriver().getLoadedModules().forEach(moduleLoader -> {
                if (moduleLoader.configuration.getCopy().equalsIgnoreCase("ALL")){
                    try {
                        FileUtils.copyFile(new File("./modules/" + moduleLoader.getJarName() + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/" + moduleLoader.getJarName() + ".jar"));
                    } catch (IOException ignored) {}
                }else if (moduleLoader.configuration.getCopy().equalsIgnoreCase("LOBBY")){
                    if (group.getGroupType().equalsIgnoreCase("LOBBY")){
                        try {
                            FileUtils.copyFile(new File("./modules/" + moduleLoader.getJarName() + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/" + moduleLoader.getJarName() + ".jar"));
                        } catch (IOException ignored) {}
                    }
                }else if (moduleLoader.configuration.getCopy().equalsIgnoreCase("PROXY")){
                    if (group.getGroupType().equalsIgnoreCase("PROXY")){
                        try {
                            FileUtils.copyFile(new File("./modules/" + moduleLoader.getJarName() + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/" + moduleLoader.getJarName() + ".jar"));
                        } catch (IOException ignored) {}

                    }
                }else if (moduleLoader.configuration.getCopy().equalsIgnoreCase("SERVER")){
                    if (!group.getGroupType().equalsIgnoreCase("PROXY")){
                        try {
                            FileUtils.copyFile(new File("./modules/" + moduleLoader.getJarName() + ".jar"), new File("./live/" + group.getGroup() + "/" + service + "/plugins/" + moduleLoader.getJarName() + ".jar"));
                        } catch (IOException ignored) {}

                    }
                }
            });
        }


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
            useVelocity = config.getBungeecordVersion().equalsIgnoreCase("VELOCITY");
        }else {
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            liveService.setManagerAddress(config.getManagerAddress());
            liveService.setRestPort(config.getRestApiCommunication());
            liveService.setRunningNode(config.getNodeName());
            liveService.setNetworkPort(config.getNetworkingCommunication());
            useVelocity = config.getBungeecordVersion().equalsIgnoreCase("VELOCITY");
        }
        new ConfigDriver("./live/" + group.getGroup() + "/" + service + "/CLOUDSERVICE.json").save(liveService);


        try {
            FileUtils.copyDirectory( new File("./local/GLOBAL/EVERY/"),
                    new File("./live/" + group.getGroup() + "/" + service +"/"));
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
        if (!group.getStorage().getJavaEnvironment().isEmpty()){
            processBuilder.environment().put("JAVA_HOME",group.getStorage().getJavaEnvironment());
        }

        if (group.getGroupType().equals("PROXY")) {

            if (!new File("./live/" + group.getGroup() + "/" + service+ "/server-icon.png").exists()){
                try {
                    FileUtils.copyFile(new File("./local/server-icon.png"), new File("./live/" + group.getGroup() + "/" + service+ "/server-icon.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileUtils.copyDirectory( new File("./local/GLOBAL/EVERY_PROXY/"),
                        new File("./live/" + group.getGroup() + "/" + service +"/"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (useVelocity){
                String[] command = new String[]{
                        "java",
                        "-XX:+UseG1GC",
                        "-XX:MaxGCPauseMillis=50",
                        "-XX:-UseAdaptiveSizePolicy",
                        "-XX:CompileThreshold=100",
                        "-Dio.netty.recycler.maxCapacity=0",
                        "-Dio.netty.recycler.maxCapacity.default=0",
                        "-Djline.terminal=jline.UnsupportedTerminal",
                        "-Xmx" + group.getUsedMemory() + "M",
                        "-jar",
                        "server.jar"
                };
                File configFile = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" + service + "/", "velocity.toml");
                final FileWriter fileWriter;
                try {
                    fileWriter = new FileWriter(configFile);
                    fileWriter.write(Driver.getInstance().getMessageStorage().getVelocityToml(port, getGroup().getMaxPlayers(), useProtocol));
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();

                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                File configFile2 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" + service + "/", "forwarding.secret");
                final FileWriter fileWriter2;
                try {
                    fileWriter2 = new FileWriter(configFile2);
                    fileWriter2.write(generatedString);
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
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            }else {
                String[] command = new String[]{
                        "java",
                        "-XX:+UseG1GC",
                        "-XX:MaxGCPauseMillis=50",
                        "-XX:-UseAdaptiveSizePolicy",
                        "-XX:CompileThreshold=100",
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

            }
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } else {

            try {
                FileUtils.copyDirectory( new File("./local/GLOBAL/EVERY_SERVER/"),
                        new File("./live/" + group.getGroup() + "/" + service +"/"));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                    "--nogui",
                    "--nojline"
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

                if (!new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service +"/bukkit.yml").exists()){
                    File configFile2 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/", "bukkit.yml");
                    try {
                        final FileWriter fileWriter2 = new FileWriter(configFile2);
                        fileWriter2.write(Driver.getInstance().getMessageStorage().getSpigotConfiguration());
                        fileWriter2.flush();
                        fileWriter2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            if (!new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service +"/spigot.yml").exists()){
                File configFile3 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" +service + "/", "spigot.yml");
                try {
                    final FileWriter fileWriter3 = new FileWriter(configFile3);
                    fileWriter3.write(Driver.getInstance().getMessageStorage().getSoigotYML(useVelocity));
                    fileWriter3.flush();
                    fileWriter3.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                processBuilder.command(command);
                try {
                    process = processBuilder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        }
    }

    public void handleRestart(){
        if (process != null && process.isAlive()){
            process.destroy();
            process.destroyForcibly().destroy();
        }


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
            useVelocity = config.getBungeecordVersion().equalsIgnoreCase("VELOCITY");
        }else {
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            liveService.setManagerAddress(config.getManagerAddress());
            liveService.setRestPort(config.getRestApiCommunication());
            liveService.setRunningNode(config.getNodeName());
            liveService.setNetworkPort(config.getNetworkingCommunication());
            useVelocity = config.getBungeecordVersion().equalsIgnoreCase("VELOCITY");
        }
        new ConfigDriver("./live/" + group.getGroup() + "/" + service + "/CLOUDSERVICE.json").save(liveService);


        try {
            FileUtils.copyDirectory( new File("./local/GLOBAL/EVERY/"),
                    new File("./live/" + group.getGroup() + "/" + service +"/"));
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
        if (!group.getStorage().getJavaEnvironment().isEmpty()){
            processBuilder.environment().put("JAVA_HOME",group.getStorage().getJavaEnvironment());
        }

        if (group.getGroupType().equals("PROXY")) {

            if (!new File("./live/" + group.getGroup() + "/" + service+ "/server-icon.png").exists()){
                try {
                    FileUtils.copyFile(new File("./local/server-icon.png"), new File("./live/" + group.getGroup() + "/" + service+ "/server-icon.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileUtils.copyDirectory( new File("./local/GLOBAL/EVERY_PROXY/"),
                        new File("./live/" + group.getGroup() + "/" + service +"/"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (useVelocity){
                String[] command = new String[]{
                        "java",
                        "-XX:+UseG1GC",
                        "-XX:MaxGCPauseMillis=50",
                        "-XX:-UseAdaptiveSizePolicy",
                        "-XX:CompileThreshold=100",
                        "-Dio.netty.recycler.maxCapacity=0",
                        "-Dio.netty.recycler.maxCapacity.default=0",
                        "-Djline.terminal=jline.UnsupportedTerminal",
                        "-Xmx" + group.getUsedMemory() + "M",
                        "-jar",
                        "server.jar"
                };
                File configFile = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" + service + "/", "velocity.toml");
                final FileWriter fileWriter;
                try {
                    fileWriter = new FileWriter(configFile);
                    fileWriter.write(Driver.getInstance().getMessageStorage().getVelocityToml(port, getGroup().getMaxPlayers(), useProtocol));
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int leftLimit = 97; // letter 'a'
                int rightLimit = 122; // letter 'z'
                int targetStringLength = 10;
                Random random = new Random();

                String generatedString = random.ints(leftLimit, rightLimit + 1)
                        .limit(targetStringLength)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
                File configFile2 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup()+ "/" + service + "/", "forwarding.secret");
                final FileWriter fileWriter2;
                try {
                    fileWriter2 = new FileWriter(configFile2);
                    fileWriter2.write(generatedString);
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
                reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            }else {
                String[] command = new String[]{
                        "java",
                        "-XX:+UseG1GC",
                        "-XX:MaxGCPauseMillis=50",
                        "-XX:-UseAdaptiveSizePolicy",
                        "-XX:CompileThreshold=100",
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

            }
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        } else {

            try {
                FileUtils.copyDirectory(new File("./local/GLOBAL/EVERY_SERVER/"),
                        new File("./live/" + group.getGroup() + "/" + service + "/"));
            } catch (IOException e) {
                e.printStackTrace();
            }

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
                    "--nogui",
                    "--nojline"
            };
            File configFile = new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/", "server.properties");
            try {
                final FileWriter fileWriter = new FileWriter(configFile);
                fileWriter.write(Driver.getInstance().getMessageStorage().getSpigotProperty(port));
                fileWriter.flush();
                fileWriter.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (!new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/bukkit.yml").exists()) {
                File configFile2 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/", "bukkit.yml");
                try {
                    final FileWriter fileWriter2 = new FileWriter(configFile2);
                    fileWriter2.write(Driver.getInstance().getMessageStorage().getSpigotConfiguration());
                    fileWriter2.flush();
                    fileWriter2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if (!new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/spigot.yml").exists()) {
                File configFile3 = new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/", "spigot.yml");
                try {
                    final FileWriter fileWriter3 = new FileWriter(configFile3);
                    fileWriter3.write(Driver.getInstance().getMessageStorage().getSoigotYML(useVelocity));
                    fileWriter3.flush();
                    fileWriter3.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            processBuilder.command(command);
            try {
                process = processBuilder.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
         }
        }

    @SneakyThrows
    public void handelShutdown(){

        if (process != null && process.isAlive()){
            process.destroy();
            process.destroyForcibly().destroy();
        }
        Thread.sleep(200);

        if (new File("./service.json").exists()){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            if (config.isCopyLogs()){
                if (new File("./local/logs/services/"+group.getGroup()+"/" + service + ".json").exists()){
                    new File("./local/logs/services/" +group.getGroup()+"/"+ service + ".json").delete();
                }
                new File("./local/logs/services/"+group.getGroup()+"/").mkdirs();
                FileUtils.copyFile(new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/logs/latest.log"), new File("./local/logs/services/"+group.getGroup()+"/" + service + ".json"));
            }
        }else {
            NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
            if (config.isCopyLogs()){
                if (new File("./local/logs/services/"+group.getGroup()+"/" + service + ".json").exists()){
                    new File("./local/logs/services/" +group.getGroup()+"/"+ service + ".json").delete();
                }
                new File("./local/logs/services/"+group.getGroup()+"/").mkdirs();
                FileUtils.copyFile(new File(System.getProperty("user.dir") + "/live/" + group.getGroup() + "/" + service + "/logs/latest.log"), new File("./local/logs/services/"+group.getGroup()+"/" + service + ".json"));
            }
        }

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
