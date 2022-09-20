package eu.themetacloudservice;

import eu.themetacloudservice.terminal.TerminalDriver;
import eu.themetacloudservice.terminal.enums.Type;

public class CloudBoot {


    public static void main(String[] args) {
        new Driver();
        Driver.getInstance().setTerminalDriver(new TerminalDriver());
        Driver.getInstance().getTerminalDriver().log(Type.EMPTY, "");

    }
}
