package eu.metacloudservice.node.cloudservices.entry;

public class QueueEntry {


    private boolean run;
    private String service;
    private String group;
    private boolean useProtocol;

    public QueueEntry(String service, String group, boolean useProtocol) {
        this.run = true;
        this.service = service;
        this.group = group;
        this.useProtocol = useProtocol;
    }

    public QueueEntry(String service) {
        run = false;
        this.service = service;
    }

    public boolean isRun() {
        return run;
    }

    public String getService() {
        return service;
    }

    public String getGroup() {
        return group;
    }

    public boolean isUseProtocol() {
        return useProtocol;
    }
}
