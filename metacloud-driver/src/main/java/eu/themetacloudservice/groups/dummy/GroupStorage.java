package eu.themetacloudservice.groups.dummy;

public class GroupStorage {

    private String template;
    private String runningNode;


    public GroupStorage() {
    }

    public GroupStorage(String template, String runningNode) {
        this.template = template;
        this.runningNode = runningNode;
    }

    public String getTemplate() {
        return template;
    }

    public String getRunningNode() {
        return runningNode;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setRunningNode(String runningNode) {
        this.runningNode = runningNode;
    }
}
