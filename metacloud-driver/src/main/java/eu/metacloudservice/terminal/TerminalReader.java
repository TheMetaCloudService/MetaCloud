package eu.metacloudservice.terminal;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandDriver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.screens.GroupSetup;
import eu.metacloudservice.terminal.screens.MainSetup;
import org.jline.reader.UserInterruptException;

public final class TerminalReader extends Thread {
    private final TerminalDriver consoleDriver;
    private final CommandDriver commandDriver;

    public TerminalReader(TerminalDriver consoleDriver) {
        this.consoleDriver = consoleDriver;
        this.commandDriver = this.consoleDriver.getCommandDriver();
    }

    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                String prompt = String.format("§bmetacloud§f@%s §7=> ", System.getProperty("user.name"));

                final var line = consoleDriver.getLineReader().readLine(consoleDriver.getColoredString(prompt));
                if (line != null && !line.trim().isEmpty()) {
                    final var input = consoleDriver.getInputs().poll();

                    if (Driver.getInstance().getMessageStorage().openServiceScreen) {
                        Driver.getInstance().getMessageStorage().consoleInput.add(line);
                    } else if (consoleDriver.isInSetup()) {
                        handleSetupInput(line);
                    } else if (input != null) {
                        input.inputs().accept(line);
                    } else {
                        commandDriver.executeCommand(line);
                    }
                } else {
                    consoleDriver.logSpeed(Type.COMMAND, "Der eingegebene Befehl wurde nicht gefunden bitte tippe '§fhelp§r'",
                            "The entered command was not found please type '§fhelp§r'");

                }
            } catch (UserInterruptException ignored) {
                commandDriver.executeCommand("stop");
            }
        }
    }

    private void handleSetupInput(String line) {
        if (line.equalsIgnoreCase("leave") ||line.equalsIgnoreCase("leave ")) {
            consoleDriver.leaveSetup();
        } else if (Driver.getInstance().getMessageStorage().setupType.equalsIgnoreCase("MAINSETUP")) {
            new MainSetup(line);
        } else if (Driver.getInstance().getMessageStorage().setupType.equalsIgnoreCase("GROUP")) {
            new GroupSetup(line);
        }
    }
}
