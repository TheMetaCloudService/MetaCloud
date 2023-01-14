package eu.themetacloudservice.queue.dummy;

import eu.themetacloudservice.process.CloudProcess;

public class QueueTask {

    public CloudProcess process;

    public QueueTask(CloudProcess process) {
        this.process = process;
    }

    public CloudProcess getProcess() {
        return process;
    }
}
