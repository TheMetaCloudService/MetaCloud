package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "module", DEdescription = "hier kannst du deine Module verwalten", ENdescription = "here you can manage your modules", aliases = {"plugins", "addons", "modules"})
public class ModuleCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {

        if (args.length == 0){
            sendHelp();
        }else if (args[0].equalsIgnoreCase("list")){
            if ( Driver.getInstance().getModuleDriver().getLoadedModules().isEmpty()){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        "Es konnten keine module gefunden werden",
                        "No modules could be found");
                return;
            }

            Driver.getInstance().getModuleDriver().getLoadedModules().forEach(loadedModule -> {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND,  "§7> Name: §b" + loadedModule.configuration.getName()
                        +"§7~Author(s):§b " + loadedModule.configuration.getAuthor()+"§7-Version:§b " +
                        loadedModule.configuration.getVersion());
            });
        }else if (args[0].equalsIgnoreCase("reload")){
            if (args.length == 2) {
                String module = args[1];


                Driver.getInstance().getModuleDriver().getLoadedModules().stream().filter(moduleLoader -> moduleLoader.configuration.getName().equalsIgnoreCase(module)).findFirst().get().reload();

                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Das Modul '§f"+module+"§r' wurde mit Erfolg neu geladen",
                            "The module '§f"+module+"§r' was reloaded with success");

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
            commands.add("list");
            commands.add("reload");
        }if (args.length == 1){
            if (args[0].equalsIgnoreCase("reload")){

            }
        }
        return commands;
    }

    private void sendHelp(){
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fmodule list §7~ zeigt dir alle laufenden Service",
                " >> §fmodule list §7~ shows you all running service");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fmodule reload <module> §7~ um eine ganze Gruppe stoppen",
                " >> §fmodule reload <module> §7~ to stop an entire group");
    }
}
