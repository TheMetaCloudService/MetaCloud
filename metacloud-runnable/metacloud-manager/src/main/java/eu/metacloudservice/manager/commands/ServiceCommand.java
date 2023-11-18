package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.metacloudservice.manager.cloudservices.entry.TaskedService;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.process.ServiceState;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.dummys.WhiteList;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

@CommandInfo(command = "service", description = "command-service-description", aliases = {"serv", "task", "start"})
public class ServiceCommand extends CommandAdapter {


    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args.length==1){
            if (args[0].equalsIgnoreCase("list")){
                if ( CloudManager.serviceDriver.getServices().isEmpty()){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-no-services"));
                    return;
                }
               CloudManager.config.getNodes().stream().filter(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase("InternalNode") || NettyDriver.getInstance().nettyServer.isChannelFound(managerConfigNodes.getName())).forEach(managerConfigNodes -> {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, managerConfigNodes.getName()+": ");
                    CloudManager.serviceDriver.getServices().stream().filter(taskedService -> taskedService.getEntry().getNode().equalsIgnoreCase(managerConfigNodes.getName())).forEach(taskedService -> {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " > "+taskedService.getEntry().getServiceName() + "~" + taskedService.getEntry().getStatus().toString() + "-" + taskedService.getEntry().getUsedPort() +" §r(players: §f"+taskedService.getEntry().getCurrentPlayers()+"§r) ");
                    });
                });
            }else {
                sendHelp();
            }
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("whitelist")){
                CloudManager.config.getWhitelist().forEach(s -> Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " > " + s));
            }else if (args[0].equalsIgnoreCase("stopGroup")){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().find(group)){
                    CloudManager.serviceDriver.getServices(group).forEach(taskedService -> CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName()));
                    }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-group-not-found")
                            .replace("%group%", group));

                }
            }else  if (args[0].equalsIgnoreCase("stop")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                    CloudManager.serviceDriver.unregister(service);
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-not-found")
                            .replace("%servuce%", service));
                }
            }else  if (args[0].equalsIgnoreCase("restart")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                    CloudManager.serviceDriver.getService(service).handelRestart();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-not-found")
                            .replace("%servuce%", service));
                }
            }else  if (args[0].equalsIgnoreCase("joinscreen")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null && CloudManager.serviceDriver.getService(service).getEntry().getStatus() != ServiceState.QUEUED){
                    CloudManager.serviceDriver.getService(service).handelScreen();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-sync")
                            .replace("%servuce%", service));
                }
            }else  if (args[0].equalsIgnoreCase("sync")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-not-found")
                            .replace("%servuce%", service));
                    CloudManager.serviceDriver.getService(service).handelSync();
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-not-found")
                            .replace("%servuce%", service));
                }
            }else  if (args[0].equalsIgnoreCase("restartgroup")){
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().load(group) != null){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-group-restart")
                            .replace("%group%", group));

                    CloudManager.serviceDriver.getServices(group).forEach(TaskedService::handelRestart);
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-group-not-found")
                            .replace("%group%", group));
                }
            }else  if (args[0].equalsIgnoreCase("restartnode")){
                String node = args[1];
                if (((ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class)).getNodes().contains(node)){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-group-not-found")
                            .replace("%node%", node));
                    CloudManager.serviceDriver.getServicesFromNode(node).forEach(TaskedService::handelRestart);
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-node-restart")
                            .replace("%node%", node));
                }
            }else  if (args[0].equalsIgnoreCase("info")){
                String service = args[1];
                if (CloudManager.serviceDriver.getService(service) != null){
                   TaskedService taskedService = CloudManager.serviceDriver.getService(service);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service name: §f" + taskedService.getEntry().getServiceName());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service uuid: §f" + taskedService.getEntry().getUUID());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Node: §f" + taskedService.getEntry().getNode());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Service Gruppe: §f" + taskedService.getEntry().getGroupName(),
                            "Service group: §f" + taskedService.getEntry().getGroupName());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Port: §f" + taskedService.getEntry().getUsedPort());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Players: §f" + taskedService.getEntry().getCurrentPlayers());
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service Status: §f" + taskedService.getEntry().getStatus());
                    int time = Integer.parseInt(String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - taskedService.getEntry().getTime())));
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                            "Service IDLE: §f" + time+ " Second(s)");
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-not-found")
                            .replace("%servuce%", service));
                }
            }else {
                sendHelp();
            }
        }else {
            if (args[0].equalsIgnoreCase("run")) {
                String group = args[1];
                if (Driver.getInstance().getGroupDriver().find(group)) {
                    if (args[2].matches("[0-9]+")) {
                        Group gdata = Driver.getInstance().getGroupDriver().load(group);

                        if (gdata.getMaximalOnline() != -1){
                            if (gdata.getMaximalOnline() < (CloudManager.serviceDriver.getServices(group).size() + Integer.parseInt(args[2]))){
                                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-max-services"));
                                return;
                            }
                        }
                        for (int i = 0; i != Integer.parseInt(args[2]); i++) {
                            String id = "";
                            if (CloudManager.config.getUuid().equals("INT")){
                                id = String.valueOf(CloudManager.serviceDriver.getFreeUUID( group));
                            }else if (CloudManager.config.getUuid().equals("RANDOM")){
                                id = CloudManager.serviceDriver.getFreeUUID();
                            }
                                    CloudManager.serviceDriver.register(new TaskedEntry(
                                    CloudManager.serviceDriver.getFreePort(gdata.getGroupType().equalsIgnoreCase("PROXY")),
                                    gdata.getGroup(),
                                    gdata.getGroup() + CloudManager.config.getSplitter() + id,
                                    gdata.getStorage().getRunningNode(), CloudManager.config.getUseProtocol(), id));
                            Driver.getInstance().getMessageStorage().canUseMemory = Driver.getInstance().getMessageStorage().canUseMemory - gdata.getUsedMemory();
                        }
                    } else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-number"));
                    }
                } else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-group-not-found")
                            .replace("%group%", group));
                }
            } else if (args[0].equalsIgnoreCase("execute")) {
                StringBuilder msg = new StringBuilder();
                String service = args[1];
                for (int i = 2; i < args.length; i++) {
                    msg.append(args[i]).append(" ");
                }
                if (CloudManager.serviceDriver.getService(service) != null && NettyDriver.getInstance().nettyServer.isChannelFound(service) || service.equalsIgnoreCase("--all")) {
                  if ( service.equalsIgnoreCase("--all")){
                      Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-send-command-all"));

                      CloudManager.serviceDriver.getServices().forEach(taskedService -> taskedService.handelExecute(msg.toString()));
                  }else {
                      Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-service-not-found")
                              .replace("%servuce%", service));
                      CloudManager.serviceDriver.getService(service).handelExecute(msg.toString());
                  }
                } else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-send-command")
                            .replace("%servuce%", service));
                }
            } else if (args[0].equalsIgnoreCase("whitelist")) {
                String  option = args[1];
                String user = args[2];
                if (option.equalsIgnoreCase("add")){
                    if (!CloudManager.config.getWhitelist().contains(user)){
                        CloudManager.config.getWhitelist().add(user);
                        new ConfigDriver("./service.json").save(CloudManager.config);
                        WhiteList whitelistConfig = new WhiteList();
                        whitelistConfig.setWhitelist(CloudManager.config.getWhitelist());
                        Driver.getInstance().getWebServer().updateRoute("/default/whitelist", new ConfigDriver().convert(whitelistConfig));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-player-add-whitelist")
                                .replace("%player%", user));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-player-not-found")
                                .replace("%player%", user));
                    }
                }else if (option.equalsIgnoreCase("remove")){
                    if (CloudManager.config.getWhitelist().contains(user)){
                        CloudManager. config.getWhitelist().remove(user);
                        new ConfigDriver("./service.json").save(CloudManager.config);
                        WhiteList whitelistConfig = new WhiteList();
                         whitelistConfig.setWhitelist(CloudManager.config.getWhitelist());
                        Driver.getInstance().getWebServer().updateRoute("/default/whitelist", new ConfigDriver().convert(whitelistConfig));
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-player-remove-whitelist")
                                .replace("%player%", user));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-player-not-found")
                                .replace("%player%", user));
                    }
                }else {
                    sendHelp();
                }
            }else {
                sendHelp();
            }
            }
        }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> commands = new ArrayList<>();

        if (args.length == 0){
            commands.add("list");
            commands.add("run");
            commands.add("stopgroup");
            commands.add("joinscreen");
            commands.add("restart");
            commands.add("stop");
            commands.add("sync");
            commands.add("info");
            commands.add("execute");
            commands.add("restartgroup");
            commands.add("restartnode");
            commands.add("whitelist");
        }else if (args.length == 1 && !args[0].equalsIgnoreCase("list") && !args[0].equalsIgnoreCase("stopgroup") && ! args[0].equalsIgnoreCase("whitelist") && !args[0].equalsIgnoreCase("run") && !args[0].equalsIgnoreCase("restartgroup") && !args[0].equalsIgnoreCase("restartnode")){
            CloudManager.serviceDriver.getServices().forEach(taskedService -> commands.add(taskedService.getEntry().getServiceName()));
            if (args[0].equalsIgnoreCase("execute") || args[0].equalsIgnoreCase("sync")){
                commands.add("--all");
            }

        }else if (args.length == 1 && args[0].equalsIgnoreCase("whitelist")){
            commands.add("add");
            commands.add("remove");
        }else if (args.length== 1 && args[0].equalsIgnoreCase("restartnode")) {

            ((ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class)).getNodes().forEach(managerConfigNodes -> {
                commands.add(managerConfigNodes.getName());
            });
        }else if (args.length == 2 && args[0].equalsIgnoreCase("whitelist") && args[1].equalsIgnoreCase("remove")){
            commands.addAll(CloudManager.config.getWhitelist());
        }else if (args.length== 1 ){
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> commands.add(group.getGroup()));
        }


        return commands;
    }


    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-1"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-2"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-3"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-4"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-5"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-6"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-7"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-8"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-9"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-10"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-11"));
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-service-help-12"));
    }

}
