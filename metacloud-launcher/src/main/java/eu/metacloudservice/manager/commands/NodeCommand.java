package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfig;
import eu.metacloudservice.configuration.dummys.managerconfig.ManagerConfigNodes;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;


@CommandInfo(command = "node", DEdescription = "hier mit werden die Nodes verwaltet", ENdescription = "here with the nodes are administered", aliases = {"nodes", "cluster"})
public class NodeCommand extends CommandAdapter {

    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
        if (args.length == 0){
            sendHelp();
        }else if (args.length == 1){
            if (args[0].equalsIgnoreCase("list")){
                config.getNodes().forEach(managerConfigNodes -> {
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
                if (config.getNodes().stream().anyMatch(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node))){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                            "der angegebene Node '§f"+node+"§r' wurde erfolgreich gelöscht",
                            "the specified node '§f"+node+"§r' was successfully deleted");

                    config.getNodes().removeIf(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node));

                    new ConfigDriver("./service.json").save(config);
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Node '§f" + node + "§r' wurde nicht gefunden",
                            "the node '§f" + node + "§r' was not found");
                }
            }else {
                sendHelp();
            }
        }else if (args.length == 3){
            if (args[0].equalsIgnoreCase("create")){
                String node = args[1];
                String address = args[2];
                if (config.getNodes().stream().noneMatch(managerConfigNodes -> managerConfigNodes.getName().equalsIgnoreCase(node))){
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.SUCCESS,
                            "der angegebene Node '§f"+node+"§r' wurde erfolgreich erstellt",
                            "the specified node '§f"+node+"§r' was successfully createt");


                    ManagerConfigNodes nodes = new ManagerConfigNodes();
                    nodes.setName(node);
                    nodes.setAddress(address);

                    config.getNodes().add(nodes);

                    new ConfigDriver("./service.json").save(config);
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Der Node '§f" + node + "§r' wurde bereits gefunden",
                            "the node '§f" + node + "§r' was already found");
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
         commands.add("list");
        }else if (args.length == 1 & !args[0].equalsIgnoreCase("list")){
            ManagerConfig config = (ManagerConfig) new ConfigDriver("./service.json").read(ManagerConfig.class);
            config.getNodes().forEach(managerConfigNodes -> commands.add(managerConfigNodes.getName()));
        }


        return commands;
    }


    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fnode create <node> <address> §7~ erstelle einen neuen Node",
                " >> §fnode create <node> <address> §7~ create a new node");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fnode delete <node> §7~ bestehenden Node löschen",
                " >> §fnode delete <node> §7~ delete existing node");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fnode list §7~ zeigt alle Nodes an",
                " >> §fnode list §7~ shows all nodes");
    }
}
