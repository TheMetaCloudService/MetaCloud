package eu.themetacloudservice.timebaser;

import eu.themetacloudservice.timebaser.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

public class TimerBase {

    private Timer timer;

    public TimerBase() {}

    public void schedule(TimerTask runnable, Integer time, TimeUtil timeUtil){
        if (timeUtil == TimeUtil.SECONDS){
            timer =  new Timer();
            timer.schedule(runnable, time*1000);
        }
        if (timeUtil == TimeUtil.MINUTES){
            Timer timer = new Timer();
            timer.schedule(runnable, time*60*1000);
        }    if (timeUtil == TimeUtil.HOURS){
            Timer timer = new Timer();
            timer.schedule(runnable, time*60*60*1000);
        }

    }

    public void schedule(TimerTask runnable, Integer time, Integer secondTime, TimeUtil timeUtil){
        if (timeUtil == TimeUtil.SECONDS){
            timer = new Timer();
            timer.schedule(runnable, time*1000, secondTime*1000);
        }
        if (timeUtil == TimeUtil.MINUTES){
            Timer timer = new Timer();
            timer.schedule(runnable, time*60*1000, secondTime*60*1000);
        }    if (timeUtil == TimeUtil.HOURS){
            Timer timer = new Timer();
            timer.schedule(runnable, time*60*60*1000, secondTime*60*60*1000);
        }

    }

    public void cancel(){
        timer.cancel();
    }
}
