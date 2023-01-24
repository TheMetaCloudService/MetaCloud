package eu.themetacloudservice.node.cloudservices;

import eu.themetacloudservice.configuration.ConfigDriver;
import eu.themetacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.themetacloudservice.groups.dummy.Group;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedEntry;
import eu.themetacloudservice.manager.cloudservices.entry.TaskedService;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceExit;
import eu.themetacloudservice.network.nodes.from.PackageToManagerCallBackServiceLaunch;
import eu.themetacloudservice.networking.NettyDriver;
import eu.themetacloudservice.node.cloudservices.entry.QueueEntry;
import eu.themetacloudservice.process.ServiceProcess;
import eu.themetacloudservice.timebaser.TimerBase;
import eu.themetacloudservice.timebaser.utils.TimeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CloudServiceDriver {


    private final LinkedList<QueueEntry> queue;
    private final List<ServiceProcess> processes;
    private final NodeConfig config;


    public CloudServiceDriver() {
        queue = new LinkedList<>();
        processes = new ArrayList<>();
        config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
        handel();
    }

    public void sync(String service){
        processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get().sync();

    }

    public void shutdownALLFORCE(){
        processes.forEach(ServiceProcess::handelShutdown);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processes.clear();
    }

    public void addQueue(QueueEntry queueEntry){
        queue.add(queueEntry);
    }

    private void handel(){
        TimerBase base = new TimerBase();
        base.schedule(new TimerTask() {
            @Override
            public void run() {

                    if (!queue.isEmpty()){
                        QueueEntry entry = queue.removeFirst();
                        if (entry.isRun()){
                            Group group = (Group) new ConfigDriver().convert(entry.getGroup(), Group.class);
                            String  service = entry.getService();
                            if (processes.stream().noneMatch(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service))){
                                int port = getFreePort(group.getGroupType().equalsIgnoreCase("PROXY"));
                                processes.add(new ServiceProcess(group, service, port , entry.isUseProtocol()));
                                processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get().handelLaunch();

                                NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);
                                PackageToManagerCallBackServiceLaunch callBack = new PackageToManagerCallBackServiceLaunch(entry.getService(), config.getNodeName(),port);
                                NettyDriver.getInstance().nettyClient.sendPacket(callBack);
                            }
                        }else {
                            String service = entry.getService();
                            if (processes.stream().anyMatch(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service))){
                                processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get().handelShutdown();
                                try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
                                processes.removeIf(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service));
                                PackageToManagerCallBackServiceExit callBack = new PackageToManagerCallBackServiceExit(service);
                                NettyDriver.getInstance().nettyClient.sendPacket(callBack);
                            }
                        }

                    }

            }
        }, 1, 1, TimeUtil.SECONDS);
    }

    private Integer getFreePort(boolean proxy) {
        List<Integer> ports = processes.stream()
                .map(ServiceProcess::getPort)
                .collect(Collectors.toList());
        return IntStream.range(proxy ? this.config.getBungeecordPort() : this.config.getSpigotPort(), Integer.MAX_VALUE)
                .filter(p -> !ports.contains(p))
                .findFirst()
                .orElse(0);
    }
}
