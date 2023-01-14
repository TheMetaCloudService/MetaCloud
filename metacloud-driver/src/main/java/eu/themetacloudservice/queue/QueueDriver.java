package eu.themetacloudservice.queue;

import eu.themetacloudservice.process.CloudProcess;
import eu.themetacloudservice.queue.dummy.QueueTask;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

public class QueueDriver {

    Queue<QueueTask> queue;

    public QueueDriver() {
        this.queue = new LinkedList<>();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (!queue.isEmpty()){
                    QueueTask queueTask = queue.poll();
                    queueTask.process.build().run();
                }
            }
        }, 1000, 1000*5);

    }

    public void addToQueue(CloudProcess process){
        queue.add( new QueueTask(process));
    }
}
