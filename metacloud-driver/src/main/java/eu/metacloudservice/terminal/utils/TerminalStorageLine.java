/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.utils;

import java.util.List;
import java.util.function.Consumer;


public final class TerminalStorageLine {

    public List<String> tabCompletes;
    public Consumer<String> inputs;


    public TerminalStorageLine(Consumer<String> inputs, List<String> tabCompletes) {
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
