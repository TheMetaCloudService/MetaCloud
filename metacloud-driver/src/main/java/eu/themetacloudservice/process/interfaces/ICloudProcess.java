package eu.themetacloudservice.process.interfaces;

import eu.themetacloudservice.process.CloudProcess;

public interface ICloudProcess {

    CloudProcess build();
    void run();
    void shutdown();

}
