package eu.metacloudservice.webserver.dummys.liveservice;

import eu.metacloudservice.configuration.interfaces.IConfigAdapter;
import eu.metacloudservice.process.ServiceState;
import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class LiveServices implements IConfigAdapter {

    private String name;
    private int uuid;
    private String group;
    private int players;
    private String host;
    private String node;
    private int port;
    private ServiceState state;
    private long lastReaction;

    public LiveServices() {}


}
