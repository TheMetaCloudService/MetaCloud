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

        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }
}
