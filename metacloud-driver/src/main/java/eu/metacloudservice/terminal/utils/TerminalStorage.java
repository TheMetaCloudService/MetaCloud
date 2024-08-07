/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.utils;

import eu.metacloudservice.terminal.enums.Type;
import lombok.NonNull;

public final class TerminalStorage {

    private final Type type;
    private final String message;

    public TerminalStorage(@NonNull final Type type, @NonNull final String message) {
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
