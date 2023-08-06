package eu.metacloudservice.timebaser;

import eu.metacloudservice.timebaser.interfaces.ITimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;

public class TimerBase implements ITimerBase {

    private Timer timer;

    public TimerBase() {}

    @Override
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
    @Override
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
    @Override
    public boolean isCanceled(){
        if (timer == null){
            return true;
        }else {
            return false;
        }
    }
    @Override
    public void cancel(){
        timer.cancel();
        timer = null;
    }
}
