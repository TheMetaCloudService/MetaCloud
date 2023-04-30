package eu.metacloudservice.node.cloudservices;

import eu.metacloudservice.Driver;
import eu.metacloudservice.configuration.ConfigDriver;
import eu.metacloudservice.configuration.dummys.nodeconfig.NodeConfig;
import eu.metacloudservice.groups.dummy.Group;
import eu.metacloudservice.manager.networking.node.HandlePacketInNodeActionSuccess;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.in.node.PacketInNodeActionSuccess;
import eu.metacloudservice.node.cloudservices.entry.QueueEntry;
import eu.metacloudservice.process.ServiceProcess;
import eu.metacloudservice.timebaser.TimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;
import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
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


    public void handleConsole(String service){
        ServiceProcess process = processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get();
        process.handelConsole();
    }
    @SneakyThrows
    public void execute(String service, String line){
       ServiceProcess process = processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get();
        process.getProcess().getOutputStream().write((line + "\n").getBytes());
        process.getProcess().getOutputStream().flush();
    }

    public void addQueue(QueueEntry queueEntry){
        queue.add(queueEntry);
    }

    private void handel(){
        TimerBase base = new TimerBase();
        base.schedule(new TimerTask() {
            @Override
            public void run() {
                NodeConfig config = (NodeConfig) new ConfigDriver("./nodeservice.json").read(NodeConfig.class);

                if (Driver.getInstance().getMessageStorage().getCPULoad() <= config.getProcessorUsage()){
                    if (!queue.isEmpty()){
                        QueueEntry entry = queue.removeFirst();
                        if (entry.isRun()){

                            Group group = (Group) new ConfigDriver().convert(entry.getGroup(), Group.class);
                            String  service = entry.getService();
                            if (processes.stream().noneMatch(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service))){
                                int port = getFreePort(group.getGroupType().equalsIgnoreCase("PROXY"));

                                processes.add(new ServiceProcess(group, service, port , entry.isUseProtocol()));
                                processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get().handelLaunch();


                                NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInNodeActionSuccess(true, service, config.getNodeName(), port));

                            }
                        }else {
                            String service = entry.getService();
                            if (processes.stream().anyMatch(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service))){
                                processes.stream().filter(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service)).findFirst().get().handelShutdown();
                                try {Thread.sleep(1000);} catch (InterruptedException ignored) {}
                                processes.removeIf(serviceProcess -> serviceProcess.getService().equalsIgnoreCase(service));

                                NettyDriver.getInstance().nettyClient.sendPacketSynchronized(new PacketInNodeActionSuccess(false, service, config.getNodeName(), 0));
                            }
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
