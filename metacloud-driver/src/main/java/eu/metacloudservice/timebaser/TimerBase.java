package eu.metacloudservice.timebaser;

import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

public class TimerBase {

    private Timer timer;

    public TimerBase() {}

    public void schedule(TimerTask runnable, Integer time, TimeUtil timeUtil){
        Timer timer = new Timer();
        if (timeUtil == TimeUtil.SECONDS){
            timer.schedule(runnable, time*1000);
        }
        else if (timeUtil == TimeUtil.MINUTES){
            timer.schedule(runnable, time*60*1000);
        }
        else if (timeUtil == TimeUtil.HOURS){
            timer.schedule(runnable, time*60*60*1000);
        }else if (timeUtil == TimeUtil.MILLISECONDS){
            timer.schedule(runnable, time);
        }

    }

    public void schedule(TimerTask runnable, Integer time, Integer secondTime, TimeUtil timeUtil){
        timer = new Timer();
        if (timeUtil == TimeUtil.SECONDS){
            timer.schedule(runnable, time*1000, secondTime*1000);
        }else if (timeUtil == TimeUtil.MINUTES){
            timer.schedule(runnable, time*60*1000, secondTime*60*1000);
        }  else  if (timeUtil == TimeUtil.HOURS){
            timer.schedule(runnable, time*60*60*1000, secondTime*60*60*1000);
        }else if (timeUtil == TimeUtil.MILLISECONDS){
            timer.schedule(runnable, time, secondTime);
        }

    }

    public void cancel(){
        timer.cancel();
    }
}
