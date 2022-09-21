package eu.themetacloudservice.terminal.utils;

import eu.themetacloudservice.terminal.enums.Type;

public class TerminalStorage {

    private Type type;
    private String message;

    public TerminalStorage(Type type, String message) {
        this.type = type;
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
