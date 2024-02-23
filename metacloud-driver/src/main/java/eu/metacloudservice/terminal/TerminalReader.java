/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.commands.CommandDriver;
import eu.metacloudservice.terminal.enums.Type;
import org.jline.reader.EndOfFileException;
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
                    consoleDriver.log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-is-not-found"));

                }
            } catch (UserInterruptException ignored) {
                System.exit(0);
            }catch (EndOfFileException end){
                if (consoleDriver.getSetupDriver().getSetup() != null){
                    consoleDriver.leaveSetup();
                }else {
                    commandDriver.executeCommand("stop");
                }
            }catch (NoSuchMethodError ignored){
                consoleDriver.log(Type.COMMAND, Driver.getInstance().getLanguageDriver().getLang().getMessage("command-is-not-work"));
            }
        }
    }

    private void handleSetupInput(String line) {
        if (line.equalsIgnoreCase("leave") ||line.equalsIgnoreCase("leave ")) {
            consoleDriver.leaveSetup();
        }else {
            Driver.getInstance().getTerminalDriver().getSetupDriver().getSetup().call(line.replace(" ", ""));
        }
    }
}
