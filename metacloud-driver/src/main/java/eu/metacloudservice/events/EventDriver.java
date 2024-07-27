package eu.metacloudservice.events;

import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.IEventAdapter;
import eu.metacloudservice.events.entrys.Subscribe;
import lombok.NonNull;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class EventDriver {
    public final int PRE = -1;
    public final int ALL = 0;
    public final int POST = 1;

    private final Map<Class<? extends IEventAdapter>, Collection<EventProcess>> bindings;
    private final Set<ICloudListener> registeredListeners;

    public EventDriver() {
        this.bindings = new ConcurrentHashMap<>();
        this.registeredListeners = new HashSet<>();
    }


    public <T extends IEventAdapter> T executeEvent(@NonNull final T event, final int i) {
        Collection<EventProcess> handlers = this.bindings.get(event.getClass());
        if (handlers == null) {
            return event;
        }
        for (EventProcess handler : handlers) {
            if (i == PRE && handler.getPriority() >= 0)
                continue;
            if (i == POST && handler.getPriority() < 0)
                continue;
            handler.execute(event);
        }
        return event;

    }
    public <T extends IEventAdapter> T executeEvent(@NonNull final T event) {
        return this.executeEvent(event, ALL);
    }

    public void registerListener(@NonNull final ICloudListener listener) {
        if (registeredListeners.contains(listener)) {
            return;
        }

        final Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (final Method method : methods) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if (annotation == null)
                continue;

            final  Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) // all listener methods should only have one parameter
                continue;

           final Class<?> param = parameters[0];

            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            if (IEventAdapter.class.isAssignableFrom(param)) {
                @SuppressWarnings("unchecked") // Java just doesn't understand that this actually is a safe cast because of the above if-statement
              final Class<? extends IEventAdapter> realParam = (Class<? extends IEventAdapter>) param;

                if (!this.bindings.containsKey(realParam)) {
                    this.bindings.put(realParam, new TreeSet<>());
                }
                final Collection<EventProcess> eventHandlersForEvent = this.bindings.get(realParam);
                eventHandlersForEvent.add(createEventHandler(listener, method, annotation));
            }
        }
    }

    private EventProcess createEventHandler(@NonNull final ICloudListener listener, @NonNull final Method method, @NonNull final Subscribe annotation) {
        return new EventProcess(listener, method, annotation);
    }

    public void clearListeners() {
        this.bindings.clear();
        this.registeredListeners.clear();
    }

}
