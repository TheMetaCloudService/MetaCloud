package eu.metacloudservice.events;

import eu.metacloudservice.events.entrys.ICloudListener;
import eu.metacloudservice.events.entrys.IEventAdapter;
import eu.metacloudservice.events.entrys.Subscribe;

import java.lang.reflect.Method;
import java.util.*;

public class EventDriver {
    public static final int PRE = -1;
    public static final int ALL = 0;
    public static final int POST = 1;

    private final Map<Class<? extends IEventAdapter>, Collection<EventProcess>> bindings;
    private final Set<ICloudListener> registeredListeners;

    public EventDriver() {
        this.bindings = new HashMap<>();
        this.registeredListeners = new HashSet<>();
    }

    public List<EventProcess> getListenersFor(Class<? extends IEventAdapter> clazz) {
        if (!this.bindings.containsKey(clazz))
            return new ArrayList<>(); // No handlers so we return an empty list
        return new ArrayList<>(this.bindings.get(clazz));
    }

    public <T extends IEventAdapter> T executeEvent(T event, int i) {
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
    public <T extends IEventAdapter> T executeEvent(T event) {
        return this.executeEvent(event, ALL);
    }

    public void registerListener(final ICloudListener listener) {
        if (registeredListeners.contains(listener)) {
            return;
        }

        Method[] methods = listener.getClass().getDeclaredMethods();
        this.registeredListeners.add(listener);
        for (final Method method : methods) {
            Subscribe annotation = method.getAnnotation(Subscribe.class);
            if (annotation == null)
                continue;

            Class<?>[] parameters = method.getParameterTypes();
            if (parameters.length != 1) // all listener methods should only have one parameter
                continue;

            Class<?> param = parameters[0];

            if (!method.getReturnType().equals(void.class)) {
                continue;
            }

            if (IEventAdapter.class.isAssignableFrom(param)) {
                @SuppressWarnings("unchecked") // Java just doesn't understand that this actually is a safe cast because of the above if-statement
                Class<? extends IEventAdapter> realParam = (Class<? extends IEventAdapter>) param;

                if (!this.bindings.containsKey(realParam)) {
                    this.bindings.put(realParam, new TreeSet<>());
                }
                Collection<EventProcess> eventHandlersForEvent = this.bindings.get(realParam);
                eventHandlersForEvent.add(createEventHandler(listener, method, annotation));
            }
        }
    }

    private EventProcess createEventHandler(final ICloudListener listener, final Method method, final Subscribe annotation) {
        return new EventProcess(listener, method, annotation);
    }

    public void clearListeners() {
        this.bindings.clear();
        this.registeredListeners.clear();
    }

    public void removeListener(ICloudListener listener) {
        for (Map.Entry<Class<? extends IEventAdapter>, Collection<EventProcess>> ee : bindings.entrySet()) {
            ee.getValue().removeIf(curr -> curr.getListener() == listener);
        }
        this.registeredListeners.remove(listener);
    }
    public Map<Class<? extends IEventAdapter>, Collection<EventProcess>> getBindings() {
        return new HashMap<>(bindings);
    }
    public Set<ICloudListener> getRegisteredListeners() {
        return new HashSet<>(registeredListeners);
    }
}
