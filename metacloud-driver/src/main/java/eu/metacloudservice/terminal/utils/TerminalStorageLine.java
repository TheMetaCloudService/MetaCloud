/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.utils;

import lombok.NonNull;

import java.util.List;
import java.util.function.Consumer;


public final class TerminalStorageLine {

    public final List<String> tabCompletes;
    public final Consumer<String> inputs;


    public TerminalStorageLine(@NonNull final Consumer<String> inputs, @NonNull final List<String> tabCompletes) {
        this.tabCompletes = tabCompletes;
        this.inputs = inputs;
    }

    public Consumer<String> inputs() {
        return inputs;
    }

    public List<String> tabCompletes() {
        return tabCompletes;
    }
}
