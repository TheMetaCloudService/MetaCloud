package eu.metacloudservice.async.pool.service;

import eu.metacloudservice.async.pool.service.entrys.CloudService;
import eu.metacloudservice.process.ServiceState;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ServicePool {


    private final ArrayList<CloudService> connectedServices;

    public ServicePool() {
        connectedServices = new ArrayList<>();
    }

    public List<CloudService> getServices(){
        return connectedServices;
    }

    public CloudService getService(@NonNull String name){
        return connectedServices.stream().filter(cloudService -> cloudService.getName().equalsIgnoreCase(name)).findFirst().get();
    }

    public List<CloudService> getServicesByGroup(@NonNull String group){
        return connectedServices.stream().filter(cloudService -> cloudService.getGroup().getGroup().equals(group)).collect(Collectors.toList());
    }
    public List<CloudService> getServicesByState(@NonNull  ServiceState state){
        return connectedServices.stream().filter(cloudService -> cloudService.getState() == state).collect(Collectors.toList());
    }

    public boolean registerService(CloudService service){
        if (connectedServices.stream().noneMatch(service1 -> service1.getName().equals(service.getName()))){
            connectedServices.add(service);
            return true;
        }else {
            return false;
        }
    }

    public boolean unregisterService(String service){
        if (connectedServices.stream().anyMatch(service1 -> service1.getName().equals(service))){
            connectedServices.removeIf(service1 -> service1.getName().equals(service));
            return true;
        }else {
            return false;
        }
    }
}
