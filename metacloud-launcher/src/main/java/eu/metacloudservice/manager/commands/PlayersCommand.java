package eu.metacloudservice.manager.commands;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandAdapter;
import eu.metacloudservice.terminal.commands.CommandInfo;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.utils.TerminalStorageLine;

import java.util.ArrayList;


@CommandInfo(command = "cloudplayers", DEdescription = "", ENdescription = "", aliases = {"players", "cp"})
public class PlayersCommand extends CommandAdapter {
    @Override
    public void performCommand(CommandAdapter command, String[] args) {

    }

    @Override
    public ArrayList<String> tabComplete(TerminalStorageLine consoleInput, String[] args) {
        return null;
    }


    private void sendHelp() {
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers list §7~ zeigt dir alle Spieler an ",
                " >> §fcloudplayers list §7~ shows you all players");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers <name> §7~ zeigt dir alle Spieler an ",
                " >> §fcloudplayers <name> §7~ shows you all players");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers <name> kick <grund> §7~ zeigt dir alle Spieler an ",
                " >> §fcloudplayers <name> kick <reason> §7~ shows you all players");

        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers <name> sendMessage <message> §7~ zeigt dir alle Spieler an ",
                " >> §fcloudplayers <name> sendMessage <message> §7~ shows you all players");
        Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND,
                " >> §fcloudplayers <name> connect <service> §7~ zeigt dir alle Spieler an ",
                " >> §fcloudplayers <name> connect <service> §7~ shows you all players");
    }
}
