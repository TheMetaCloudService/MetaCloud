package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.CloudManager;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.setup.setups.group.GroupSetup;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "group", aliases = {"g", "template", "temp"}, description = "command-group-description")
public class GroupCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            sendHelp();
        }else if (args.length == 1){
            if (args[0].equalsIgnoreCase("create")){
                Driver.getInstance().getTerminalDriver().getSetupDriver().setSetup(new GroupSetup());
                Driver.getInstance().getTerminalDriver().joinSetup();
            }else if (args[0].equalsIgnoreCase("list")){
                if ( Driver.getInstance().getGroupDriver().getAll().isEmpty()){
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found" ));
                    return;
                }
              ArrayList<Group> groups =   Driver.getInstance().getGroupDriver().getAll();
                for (int i = 0; i != groups.size() ; i++) {
                    Group group =  groups.get(i);
                    Driver.getInstance().getTerminalDriver().log(Type.COMMAND, group.getGroup() +"~" + group.getGroupType() + " | "+ group.getStorage().getRunningNode());
                }
            }else {
                sendHelp();
            }
        }else {
            if (args.length== 2){
                if (args[1].equalsIgnoreCase("delete")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Driver.getInstance().getGroupDriver().delete(group);
                        CloudManager.serviceDriver.delete.add(group);
                        CloudManager.serviceDriver.getServices(group).forEach(taskedService -> CloudManager.serviceDriver.unregister(taskedService.getEntry().getServiceName()));

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-delete")
                                .replace("%group%", group));

                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }


                }else  if (args[1].equalsIgnoreCase("info")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        Driver.getInstance().getTerminalDriver().log(Type.SUCCESS, new ConfigDriver().convert(raw));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else {
                    sendHelp();
                }
            }else  if (args.length== 3) {
                if (args[1].equalsIgnoreCase("setmaintenance")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        raw.setMaintenance(args[2].equalsIgnoreCase("true"));
                        Driver.getInstance().getGroupDriver().update(group, raw);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-change-maintenance" ));

                        Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                    }else if (group.equals("--all")) {
                        Driver.getInstance().getGroupDriver().getAll().forEach(group1 -> {
                            group1.setMaintenance(args[2].equalsIgnoreCase("true"));
                            Driver.getInstance().getGroupDriver().update(group, group1);
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + group, new ConfigDriver().convert(group1));
                        });
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-change-maintenance" ));

                    }else{
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else  if (args[1].equalsIgnoreCase("settemplate")){
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if (Driver.getInstance().getTemplateDriver().get().contains(args[2].replace(" ", ""))){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.getStorage().setTemplate(args[2].replace(" ", ""));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-template-change" ));
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-no-template" ));
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else if (args[1].equalsIgnoreCase("setminamount")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setMinimalOnline(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-min-count"));

                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-only-number"));

                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else  if (args[1].equalsIgnoreCase("setpriority")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setPriority(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-priority"));

                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-only-number"));
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else  if (args[1].equalsIgnoreCase("setJavaEnvironment")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        Group raw = Driver.getInstance().getGroupDriver().load(group);
                        raw.getStorage().setJavaEnvironment(args[2]);
                        Driver.getInstance().getGroupDriver().update(group, raw);
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-environment")
                                .replace("%path%", args[2]));

                        Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else  if (args[1].equalsIgnoreCase("setmaxplayers")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setMaxPlayers(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-max-players"));

                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-only-number"));

                        }
                    }else {

                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else  if (args[1].equalsIgnoreCase("setstartnewpercen")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setStartNewPercent(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-start-procent"));
                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-only-number"));

                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else if (args[1].equalsIgnoreCase("setpermission")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].contains(".") ){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setPermission(args[2]);
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-permission" ));


                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-need-dot" ));
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
                    }
                }else  if (args[1].equalsIgnoreCase("setmaxamount")) {
                    String group = args[0];
                    if (Driver.getInstance().getGroupDriver().find(group)){
                        if(args[2].matches("[0-9]+") || args[2].equalsIgnoreCase("-1")){
                            Group raw = Driver.getInstance().getGroupDriver().load(group);
                            raw.setMinimalOnline(Integer.valueOf(args[2]));
                            Driver.getInstance().getGroupDriver().update(group, raw);
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-max-count"));

                            Driver.getInstance().getWebServer().updateRoute("/cloudgroup/" + raw.getGroup(), new ConfigDriver().convert(raw));
                        }else {
                            Driver.getInstance().getTerminalDriver().log(Type.COMMAND,Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-only-number"));
                        }
                    }else {
                        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-not-found")
                                .replace("%group%", group));
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
            commands.add("create");
            commands.add("list");
            Driver.getInstance().getGroupDriver().getAll().forEach(group -> commands.add(group.getGroup()));
            commands.add("--all");
        }
        if (args.length == 1 && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("list")){
            commands.add("delete");
            commands.add("info");
            commands.add("setmaintenance");
            commands.add("setmaxplayers");
            commands.add("settemplate");
            commands.add("setminamount");
            commands.add("setjavaenvironment");
            commands.add("setpriority");
            commands.add("setstartnewpercen");
            commands.add("setpermission");
            commands.add("setmaxamount");
        }
        if (args.length == 2){
            if (args[1].equalsIgnoreCase("setmaintenance") && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("list")) {
                commands.add("true");
                commands.add("false");
            }  if (args[1].equalsIgnoreCase("settemplate") && !args[0].equalsIgnoreCase("create") && !args[0].equalsIgnoreCase("list")) {
                ArrayList<String> rawtemplates = Driver.getInstance().getTemplateDriver().get();
                commands.addAll(rawtemplates);
            }
        }

        return commands;
    }

    private void sendHelp(){

        Driver.getInstance().getTerminalDriver().log(Type.COMMAND,

                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-1"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-2"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-3"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-4"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-5"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-6"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-7"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-8"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-9"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-10"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-11"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-12"),
                Driver.getInstance().getLanguageDriver().getLang().getMessage("command-group-help-13")
                );

    }
}
