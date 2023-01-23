package eu.themetacloudservice.node.cloudservices.interfaces;

import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.process.ServiceProcess;

import java.util.List;

public interface INodeServiceDriver {

    ServiceProcess handelLaunch(String service, boolean usedProtocol);
    void handelQuit(String service);
    ServiceProcess getService(String service);
    void handelSync(String service);
    Integer getFreePort(boolean proxy);
    List<ServiceProcess> getServices(String Group);
    List<ServiceProcess> getServices();
    void handelQuit(List<String> services);

}
