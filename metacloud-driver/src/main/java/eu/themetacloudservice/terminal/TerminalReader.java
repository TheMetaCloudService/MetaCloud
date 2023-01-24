package eu.themetacloudservice.terminal;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;
import eu.themetacloudservice.terminal.screens.GroupSetup;
import eu.themetacloudservice.terminal.screens.MainSetup;
import lombok.var;

public final class TerminalReader extends Thread{


    private final TerminalDriver consoleDriver;

    public TerminalReader(TerminalDriver consoleDriver) {
        this.consoleDriver = consoleDriver;
    }


    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                final var line = Driver.getInstance().getTerminalDriver().getLineReader().readLine(Driver.getInstance().getTerminalDriver().getColoredString("§bmetacloud§f@"+System.getProperty("user.name") +" §7=> "));
                if (line != null && !line.isEmpty()) {
                    final var input = this.consoleDriver.getInputs().poll();

                    if(Driver.getInstance().getTerminalDriver().isInSetup()){
                        if (line.equalsIgnoreCase("leave")){
                            Driver.getInstance().getTerminalDriver().leaveSetup();
                        }else if (Driver.getInstance().getMessageStorage().setuptype.equalsIgnoreCase("MAINSETUP")){
                            new MainSetup(line);
                        }else if (Driver.getInstance().getMessageStorage().setuptype.equalsIgnoreCase("GROUP")){
                            new GroupSetup(line);
                        }
                    } else if (input != null) {
                        input.inputs().accept(line);
                    } else {
                        this.consoleDriver.getCommandDriver().executeCommand(line);
                    }
                }else {
                    Driver.getInstance().getTerminalDriver().logSpeed(Type.COMMAND, "der eingegebene Befehl wurde nicht gefunden bitte tippe '§fhelp§r'", "the entered command was not found please type '§fhelp§r");
                }
            }catch (Exception ignored){}
        }
    }
}
