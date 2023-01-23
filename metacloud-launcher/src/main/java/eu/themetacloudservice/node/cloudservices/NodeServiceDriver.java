package eu.themetacloudservice.node.cloudservices;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.node.cloudservices.interfaces.INodeServiceDriver;
import eu.themetacloudservice.process.ServiceProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class NodeServiceDriver implements INodeServiceDriver {

    private final ArrayList<ServiceProcess> services;
    private final NodeConfig config;

    public NodeServiceDriver() {
        services = new ArrayList<>();
         config= (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
    }

    @Override
    public ServiceProcess handelLaunch(String service, boolean usedProtocol) {
        if (services.stream().noneMatch(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service))){

            ServiceProcess process =new ServiceProcess(null, service, getFreePort(true), usedProtocol);
            process.handelLaunch();
            services.add(process);
            return process;
        }else {
            return null;
        }
    }


    @Override
    public void handelQuit(String service) {
        if (services.stream().anyMatch(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service))){
            ServiceProcess process = getService(service);
            process.handelShutdown();
            services.removeIf(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service));
        }
    }

    @Override
    public ServiceProcess getService(String service) {
        return services.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get();
    }

    @Override
    public void handelSync(String service) {
        getService(service).sync();
    }


    @Override
    public Integer getFreePort(boolean proxy) {
        List<Integer> ports = services.stream().map(ServiceProcess::getPort).collect(Collectors.toList());
        return IntStream.range(proxy ? this.config.getBungeecordPort() : this.config.getSpigotPort(), Integer.MAX_VALUE)
                .filter(p -> !ports.contains(p))
                .findFirst()
                .orElse(0);
    }

    @Override
    public List<ServiceProcess> getServices(String Group) {
        return services.stream().filter(serviceProcess -> serviceProcess.getGroup().getGroup().equalsIgnoreCase(Group)).collect(Collectors.toList());
    }

    @Override
    public List<ServiceProcess> getServices() {
        return services;
    }

    @Override
    public void handelQuit(List<String> services) {
        services.forEach(this::handelQuit);
    }
}
