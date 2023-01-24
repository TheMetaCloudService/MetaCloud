package eu.themetacloudservice.events;



import eu.themetacloudservice.events.entry.EventHandler;
import eu.themetacloudservice.events.entry.ICloudEventListener;
import eu.themetacloudservice.events.entry.IEventAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EventProcess implements  Comparable<EventProcess>{
    private final ICloudEventListener listener;
    private final Method method;
    private final EventHandler annotation;

    public EventProcess(ICloudEventListener listener, Method method, EventHandler annotation) {
        this.listener = listener;
        this.method = method;
        this.annotation = annotation;
    }
    public EventHandler getAnnotation() {
        return annotation;
    }

    public Method getMethod() {
        return method;
    }
    public ICloudEventListener getListener() {
        return listener;
    }

    public void execute(IEventAdapter event) {
        try {
            method.invoke(listener, event);
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ignored) {
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
