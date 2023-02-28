package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.module.loader.ModuleClassLoader;
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
            if ( Driver.getInstance().getModuleDriver().loadedModules.isEmpty()){
                Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                        "Es konnten keine module gefunden werden",
                        "No modules could be found");
                return;
            }

            Driver.getInstance().getModuleDriver().loadedModules.forEach(loadedModule -> {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND,  "§7> Name: §b" + loadedModule.readProperties().MODULE_NAME
                        +"§7~Author(s):§b " + loadedModule.readProperties().MODULE_AUTHOR +"§7-Version:§b " +
                        loadedModule.readProperties().MODULE_VERSION);
            });
        }else if (args[0].equalsIgnoreCase("reload")){
            if (args.length == 2) {
                String module = args[1];

                boolean exists = false;
                ModuleClassLoader selecedModule = null;

                for (int i = 0; i != Driver.getInstance().getModuleDriver().loadedModules.size(); i++) {
                    ModuleClassLoader module1 = Driver.getInstance().getModuleDriver().loadedModules.get(i);
                    if (module1.readProperties().MODULE_NAME.equalsIgnoreCase(module)) {
                        selecedModule = module1;
                        exists = true;
                    }
                }
                if (exists) {
                    selecedModule.disableModule();
                    selecedModule.enableModule();
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Das Modul '§f"+module+"§r' wurde mit Erfolg neu geladen",
                            "The module '§f"+module+"§r' was reloaded with success");

                } else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                            "Das Module wurde nicht gefunden",
                            "The module was not found");
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
            commands.add("list");
            commands.add("reload");
        }if (args.length == 1){
            if (args[0].equalsIgnoreCase("reload")){
                Driver.getInstance().getModuleDriver().loadedModules.forEach(moduleClassLoader -> {
                    commands.add(moduleClassLoader.modulename);
                });
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
