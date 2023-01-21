package eu.themetacloudservice;


import eu.themetacloudservice.groups.GroupDriver;
import eu.themetacloudservice.groups.TemplateDriver;
import eu.themetacloudservice.module.ModuleDriver;
import eu.themetacloudservice.storage.MessageStorage;
import eu.themetacloudservice.terminal.TerminalDriver;
import eu.themetacloudservice.webserver.WebServer;

public class Driver {

    private static  Driver instance;
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



    public ModuleDriver getModuleDriver() {
        return moduleDriver;
    }

    public TemplateDriver getTemplateDriver() {
        return templateDriver;
    }

    public GroupDriver getGroupDriver() {
        return groupDriver;
    }

    public void runWebServer(){
        this.webServer = new WebServer();
    }

    public WebServer getWebServer() {
        return webServer;
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
