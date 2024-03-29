/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;
import eu.metacloudservice.webserver.entry.RouteEntry;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;


@CommandInfo(command = "node", description = "command-node-description", aliases = {"nodes", "cluster", "wrapper"})
public class NodeCommand extends CommandAdapter {

    @Override
    public void performCommand(CommandAdapter command, String[] args) {

        if (args.length == 0){
            sendHelp();
        }else if (args.length == 1){
            if (args[0].equalsIgnoreCase("list")){
                CloudManager.config.getNodes().forEach(managerConfigNodes -> {
                    if (NettyDriver.getInstance().nettyServer.isChannelFound(managerConfigNodes.getName()) || managerConfigNodes.getName().equalsIgnoreCase("InternalNode")){
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"§f" + managerConfigNodes.getName() + "~" + managerConfigNodes.getAddress()+ "-Connected");
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,"§f" + managerConfigNodes.getName() + "~" + managerConfigNodes.getAddress() + "-Offline");
                    }
                });
            }else {
                sendHelp();
            }
        }else if (args.length == 2){
            if (args[0].equalsIgnoreCase("delete")){
                String node = args[1];
                if (CloudManager.config.getNodes().stream().anyMatch(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node))){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-delete")
                            .replace("%node%", node));
                    CloudManager.config.getNodes().removeIf(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node));
                    new ConfigDriver("./service.json").save(CloudManager.config);
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-not-found")
                            .replace("%node%", node));

                }
            }else    if (args[0].equalsIgnoreCase("services")){
                String node = args[1];
                if (CloudManager.config.getNodes().stream().anyMatch(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node))){
                    CloudManager.serviceDriver.getServicesFromNode(node).forEach(taskedService -> {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, taskedService.getEntry().getServiceName() + "~" + taskedService.getEntry().getCurrentPlayers());
                    });
                    CloudManager.config.getNodes().removeIf(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node));
                    new ConfigDriver("./service.json").save(CloudManager.config);
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-not-found")
                            .replace("%node%", node));
                }
            }else{
                sendHelp();
            }
        }else if (args.length == 3){
            if (args[0].equalsIgnoreCase("create")){
                String node = args[1];
                String address = args[2];
                if (CloudManager.config.getNodes().stream().noneMatch(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node))){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-create")
                            .replace("%node%", node));

                    ManagerConfigNodes nodes = new ManagerConfigNodes();
                    nodes.setName(node);
                    nodes.setAddress(address);
                    CloudManager.config.getNodes().add(nodes);

                    String link = "http://" + CloudManager.config.getManagerAddress() + ":" + CloudManager.config.getRestApiCommunication() + "/setup/" + node;
                    NodeConfig config = new NodeConfig();
                    config.setLanguage(CloudManager.config.getLanguage());
                    config.setManagerAddress(CloudManager.config.getManagerAddress());
                    config.setCanUsedMemory(1024);
                    config.setBungeecordVersion(CloudManager.config.getBungeecordVersion());
                    config.setSpigotVersion(CloudManager.config.getSpigotVersion());
                    config.setNetworkingCommunication(CloudManager.config.getNetworkingCommunication());
                    config.setRestApiCommunication(CloudManager.config.getRestApiCommunication());
                    config.setCopyLogs(CloudManager.config.isCopyLogs());
                    config.setBungeecordPort(CloudManager.config.getBungeecordPort());
                    config.setProcessorUsage(CloudManager.config.getProcessorUsage());
                    config.setAutoUpdate(CloudManager.config.isAutoUpdate());
                    config.setSpigotPort(CloudManager.config.getSpigotPort());
                    config.setNodeAddress(address);
                    config.setNodeName(node);
                    Driver.getInstance().getWebServer().addRoute(new RouteEntry("/setup/" + node, new ConfigDriver().convert(config)));
                    new ConfigDriver("./service.json").save(CloudManager.config);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-link")
                            .replace("%link%", link));
                }else {
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-exists")
                            .replace("%node%", node));
                }
            }else {
                sendHelp();
            }
        }else {
            sendHelp();
        }

    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        ArrayList<String> commands = new ArrayList<>();
        if (args.length == 0){
         commands.add("create");
         commands.add("delete");
         commands.add("services");
         commands.add("list");
        }else if (args.length == 1 & !args[0].equalsIgnoreCase("list")){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            config.getNodes().forEach(managerConfigNodes -> commands.add(managerConfigNodes.getName()));
        }
        return commands;
    }


    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-help-1"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-help-2"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-help-3"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-node-help-4")
                );
    }
}
