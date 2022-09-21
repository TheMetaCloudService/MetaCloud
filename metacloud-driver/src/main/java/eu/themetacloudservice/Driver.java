package eu.themetacloudservice;


import eu.themetacloudservice.storage.MessageStorage;
import eu.themetacloudservice.terminal.TerminalDriver;

public class Driver {

    private static  Driver instance;
    private TerminalDriver terminalDriver;
    private MessageStorage messageStorage;

    public Driver(){


        /*
         * @FUNCTION: Load all for the Driver.class
         * @Coder: RauchigesEtwas (Robin B.)
         * */

        instance = this;
        this.messageStorage = new MessageStorage();
    }

    public TerminalDriver getTerminalDriver() {
        return terminalDriver;
    }

    public MessageStorage getMessageStorage() {
        return messageStorage;
    }

    public void setTerminalDriver(TerminalDriver terminalDriver) {
        this.terminalDriver = terminalDriver;
    }

    public static Driver getInstance() {
        return instance;
    }
}
