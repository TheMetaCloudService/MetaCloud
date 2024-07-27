package eu.metacloudservice.timebaser.interfaces;

import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.TimerTask;

public interface ITimerBase {

    void  schedule(TimerTask runnable, long delay, TimeUtil timeUtil);
    void scheduleAsync(TimerTask runnable, long time, TimeUtil timeUtil);
    void schedule(TimerTask runnable, long time, long secondTime, TimeUtil timeUtil);
    public void scheduleAsync(TimerTask runnable, long time, long secondTime, TimeUtil timeUtil);
    boolean isCanceled();
    void cancel();
}
