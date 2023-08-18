package eu.metacloudservice;


import eu.metacloudservice.groups.GroupDriver;
import eu.metacloudservice.groups.TemplateDriver;
import eu.metacloudservice.module.ModuleDriver;
import eu.metacloudservice.storage.MessageStorage;
import eu.metacloudservice.terminal.TerminalDriver;
import eu.metacloudservice.webserver.WebServer;
import lombok.Getter;

@Getter
public final class Driver {

    private static Driver instance;
    private TerminalDriver terminalDriver;
    private final MessageStorage messageStorage;
    private final GroupDriver groupDriver;
    private final TemplateDriver templateDriver;
    private final ModuleDriver moduleDriver;
    private WebServer webServer;

    public Driver(){
        /*
         * @FUNCTION: Load all for the Driver.class
         * @Coder: RauchigesEtwas (Robin B.)
         * */
        instance = this;
        groupDriver = new GroupDriver();
        this.moduleDriver = new ModuleDriver();
        this.messageStorage = new MessageStorage();
        this.templateDriver = new TemplateDriver();

    }

    public void runWebServer(){
        this.webServer = new WebServer();
    }

    public void setTerminalDriver(TerminalDriver terminalDriver) {
        this.terminalDriver = terminalDriver;
    }

    public static Driver getInstance() {
        return instance;
    }
}
