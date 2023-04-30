package eu.metacloudservice.moduleside.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.moduleside.MetaModule;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;

@CommandInfo(command = "syncproxy", aliases = {"sp"}, DEdescription = "Sehe alle berfehle des SyncProxyModules", ENdescription = "See all commands of the SyncProxyModule")
public class SyncProxyCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {
        if (args.length == 0){
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                    " >> §fsyncproxy reload  §7~ das gesamte Syncproxy-Modul neu laden",
                    " >> §fsyncproxy reload §7~ reload the entire Syncproxy module");
        }else if (args[0].equalsIgnoreCase("reload")){
            MetaModule.update();
        }else {
            Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                    " >> §fsyncproxy reload  §7~ das gesamte Syncproxy-Modul neu laden",
                    " >> §fsyncproxy reload §7~ reload the entire Syncproxy module");
        }
    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {

        ArrayList<String> commands = new ArrayList<>();
        commands.add("reload");
        return commands;
    }
}
