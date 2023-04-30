package eu.metacloudservice.terminal;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.enums.Type;
import eu.metacloudservice.terminal.screens.GroupSetup;
import eu.metacloudservice.terminal.screens.MainSetup;

public final class TerminalReader extends Thread{


    private final TerminalDriver consoleDriver;

    public TerminalReader(TerminalDriver consoleDriver) {
        this.consoleDriver = consoleDriver;
    }


    @Override
    public void run() {
        while (!this.isInterrupted()) {
            try {
                String prompt = "§bmetacloud§f@"+System.getProperty("user.name") +" §7=> ";

                final var line = Driver.getInstance().getTerminalDriver().getLineReader().readLine(Driver.getInstance().getTerminalDriver().getColoredString(prompt));
                if (line != null && !line.isEmpty()) {
                    final var input = this.consoleDriver.getInputs().poll();

                    if (Driver.getInstance().getMessageStorage().openServiceScreen){
                        Driver.getInstance().getMessageStorage().consoleInput.add(line);
                    }else if(Driver.getInstance().getTerminalDriver().isInSetup()){
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
            }catch (Exception ignored){
                ignored.printStackTrace();
            }
        }
    }
}
