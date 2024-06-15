package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "modules", description = "command-modules-description", aliases = {"module", "md"})
public class ModulesCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "This command is currently only available in english. German translation will be added soon.");
        if (args.length == 0) {
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Currently loaded modules:");
            Driver.getInstance().getModuleDriver().getLoadedModules().forEach((module) -> {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, module + " - " + module.getJarName());
            });
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " ");
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Available modules:");
            Driver.getInstance().getModuleDriver().getAvailableModuleNames().forEach((module) -> {
                Driver.getInstance().getTerminalDriver().log(Type.COMMAND, module);
            });
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, " ");
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Use 'modules download <module>' to download a module.");
            return;
        } else if (args.length == 2 && args[0].equalsIgnoreCase("download")) {
            Driver.getInstance().getModuleDriver().downloadModule(args[1]);
            return;
        } else {
            Driver.getInstance().getTerminalDriver().log(Type.COMMAND, "Invalid arguments. Use 'modules' to list all modules.");
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
