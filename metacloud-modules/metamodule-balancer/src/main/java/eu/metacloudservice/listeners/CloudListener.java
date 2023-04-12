package eu.metacloudservice.listeners;

import eu.metacloudservice.Driver;
import eu.metacloudservice.MetaModule;
import eu.metacloudservice.balance.subgates.SubGate;
import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.Subscribe;
import eu.metacloudservice.events.listeners.CloudProxyConnectedEvent;
import eu.metacloudservice.events.listeners.CloudProxyDisconnectedEvent;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.utils.ProxyData;

public class CloudListener implements ICloudListener {

    @Subscribe
    public void handle(CloudProxyConnectedEvent event){
        String service = event.getName();
        String host = event.getHost();
        Integer port = event.getPort();
        Group group = Driver.getInstance().getGroupDriver().load(event.getGroup());
        if (group.getGroupType().equalsIgnoreCase("PROXY")){
            MetaModule.getInstance().getProxyStorage().put(service, new ProxyData(0, service, host, port, new SubGate(service, host, port)));
        }
    }

    @Subscribe
    public void handle(CloudProxyDisconnectedEvent event){
        if (MetaModule.getInstance().getProxyStorage().containsKey(event.getName())){
            MetaModule.getInstance().getProxyStorage().remove(event.getName());
        }
    }
}
