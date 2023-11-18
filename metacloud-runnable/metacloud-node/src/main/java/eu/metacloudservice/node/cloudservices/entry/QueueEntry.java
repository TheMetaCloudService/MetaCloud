package eu.metacloudservice.node.cloudservices.entry;

public class QueueEntry {


    @lombok.Getter
    private final boolean run;
    @lombok.Getter
    private final String service;
    @lombok.Getter
    private String group;
    @lombok.Getter
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

}
