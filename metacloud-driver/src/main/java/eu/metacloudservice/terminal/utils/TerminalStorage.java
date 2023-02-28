package eu.metacloudservice.terminal.utils;

import eu.metacloudservice.terminal.enums.Type;

public class TerminalStorage {

    private final Type type;
    private final String message;

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
