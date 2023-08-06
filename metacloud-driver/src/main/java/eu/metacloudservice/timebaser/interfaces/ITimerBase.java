package eu.metacloudservice.timebaser.interfaces;

import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.TimerTask;

public interface ITimerBase {
    void schedule(TimerTask runnable, Integer time, TimeUtil timeUtil);
    void schedule(TimerTask runnable, Integer time, Integer secondTime, TimeUtil timeUtil);
    boolean isCanceled();
    void cancel();
}
