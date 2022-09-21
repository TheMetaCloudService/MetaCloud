package eu.themetacloudservice.networking.handlers.listener;


import eu.themetacloudservice.networking.handlers.bin.IEvent;

public class NetworkExceptionEvent implements IEvent {

    private Exception exception;

    public NetworkExceptionEvent(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
