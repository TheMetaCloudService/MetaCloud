package eu.metacloudservice.timebaser;

import eu.metacloudservice.timebaser.interfaces.ITimerBase;
import eu.metacloudservice.timebaser.utils.TimeUtil;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class TimerBase implements ITimerBase {

    private  final long MILLISECONDS_PER_SECOND = 1000;
    private  final long MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND;
    private  final long MILLISECONDS_PER_HOUR = 60 * MILLISECONDS_PER_MINUTE;

    private Timer timer;

    @Override
    public void schedule(TimerTask runnable, long delay, TimeUtil timeUtil) {
        long delayInMillis = convertTimeToMillis(delay, timeUtil);
        timer = new Timer();
        timer.schedule(runnable, delayInMillis);
    }

    @Override
    public void scheduleAsync(TimerTask runnable, long delay, TimeUtil timeUtil) {
        CompletableFuture.runAsync(() -> {
            long delayInMillis = convertTimeToMillis(delay, timeUtil);
            Timer timer = new Timer();
            timer.schedule(runnable, delayInMillis);
        });
    }

    @Override
    public void schedule(TimerTask runnable, long delay, long period, TimeUtil timeUtil) {
        long delayInMillis = convertTimeToMillis(delay, timeUtil);
        long periodInMillis = convertTimeToMillis(period, timeUtil);
        timer = new Timer();
        timer.schedule(runnable, delayInMillis, periodInMillis);
    }

    @Override
    public void scheduleAsync(TimerTask runnable, long delay, long period, TimeUtil timeUtil) {
        CompletableFuture.runAsync(() -> {
            long delayInMillis = convertTimeToMillis(delay, timeUtil);
            long periodInMillis = convertTimeToMillis(period, timeUtil);
            Timer timer = new Timer();
            timer.schedule(runnable, delayInMillis, periodInMillis);
        });
    }

    @Override
    public boolean isCanceled() {
        return timer == null;
    }

    @Override
    public void cancel() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private long convertTimeToMillis(long time, TimeUtil timeUtil) {
        switch (timeUtil) {
            case SECONDS:
                return time * MILLISECONDS_PER_SECOND;
            case MINUTES:
                return time * MILLISECONDS_PER_MINUTE;
            case HOURS:
                return time * MILLISECONDS_PER_HOUR;
            case MILLISECONDS:
                return time;
            default:
                throw new IllegalArgumentException("Invalid time unit");
        }
    }
}
