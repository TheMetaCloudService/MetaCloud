package eu.metacloudservice.events;

import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.IEventAdapter;
import eu.metacloudservice.events.entrys.Subscribe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class EventProcess implements  Comparable<EventProcess>{
    private final ICloudListener listener;
    private final Method method;
    private final Subscribe annotation;

    public EventProcess(ICloudListener listener, Method method, Subscribe annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }
    public Subscribe getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
    public ICloudListener getListener() {
        return listener;
    }

    public void execute(IEventAdapter event) {
            if (annotation.async()){
                CompletableFuture.runAsync(() -> {
                    try {
                        method.invoke(listener, event);
                    } catch (IllegalAccessException | InvocationTargetException ignored) {}
                });
            }else {
                try {
                    method.invoke(listener, event);
                } catch (IllegalAccessException | InvocationTargetException ignored) {};
            }
    }
    @Override
    public String toString() {
        return "(EventHandler " + this.listener + ": " + method.getName() + ")";
    }

    public int getPriority() {
        return annotation.priority();
    }

    @Override
    public int compareTo(EventProcess other) {
        int annotation = this.annotation.priority() - other.annotation.priority();
        if (annotation == 0)
            annotation = this.listener.hashCode() - other.listener.hashCode();
        return annotation == 0 ? this.hashCode() - other.hashCode() : annotation;
    }
}
