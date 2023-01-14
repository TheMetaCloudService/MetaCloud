package eu.themetacloudservice;


import eu.themetacloudservice.groups.GroupDriver;
import eu.themetacloudservice.groups.TemplateDriver;
import eu.themetacloudservice.module.ModuleDriver;
import eu.themetacloudservice.queue.QueueDriver;
import eu.themetacloudservice.queue.dummy.QueueTask;
import eu.themetacloudservice.storage.MessageStorage;
import eu.themetacloudservice.terminal.TerminalDriver;

public class Driver {

    private static  Driver instance;
    private TerminalDriver terminalDriver;
    private MessageStorage messageStorage;
    private GroupDriver groupDriver;
    private TemplateDriver templateDriver;
    private ModuleDriver moduleDriver;
    private QueueDriver queueDriver;

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


    public void createQueue(){
        queueDriver = new QueueDriver();
    }
    public QueueDriver getQueueDriver() {
        return queueDriver;
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
