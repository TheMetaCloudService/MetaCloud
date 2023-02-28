package eu.metacloudservice.manager.cloudservices.entry;


import eu.metacloudservice.process.ServiceState;

public class TaskedEntry {

    private int current_players;
    private int check_interval;
    private int check_interval_players;
    private int used_port;
    private final String group_name;
    private final String service_name;
    private final String task_node;
    private ServiceState status;
    private final boolean use_protocol;
    private final  long time;
    private String UUID;

    public TaskedEntry(int used_port, String group_name, String service_name, String task_node, boolean use_protocol) {
        this.current_players = 0;
        this.check_interval = 0;
        this.check_interval_players = 0;
        this.time = System.currentTimeMillis();
        this.used_port = used_port;
        this.group_name = group_name;
        this.service_name = service_name;
        this.task_node = task_node;
        this.status = ServiceState.QUEUED;
        this.use_protocol = use_protocol;
        this.UUID = java.util.UUID.randomUUID().toString();
    }


    public String getUUID() {
        return UUID;
    }

    public void setUsedPort(int used_port) {
        this.used_port = used_port;
    }

    public boolean isUseProtocol() {
        return use_protocol;
    }

    public int getCheckInterval() {
        return check_interval;
    }

    public void setCheckInterval(int check_interval) {
        this.check_interval = check_interval;
    }

    public void setCurrentPlayers(int current_players) {
        this.current_players = current_players;
    }

    public int getCurrentPlayers() {
        return current_players;
    }

    public String getGroupName() {
        return group_name;
    }

    public String getServiceName() {
        return service_name;
    }

    public String getNode() {
        return task_node;
    }

    public ServiceState getStatus() {
        return status;
    }

    public void setStatus(ServiceState status) {
        this.status = status;
    }

    public int getUsedPort() {
        return used_port;
    }

    public int getCheckIntervalPlayers() {
        return check_interval_players;
    }

    public void setCheckIntervalPlayers(int check_interval_players) {
        this.check_interval_players = check_interval_players;
    }

    public long getTime() {
        return time;
    }
}
