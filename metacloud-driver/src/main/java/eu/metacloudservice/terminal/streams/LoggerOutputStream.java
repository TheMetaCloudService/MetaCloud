package eu.metacloudservice.terminal.streams;

import eu.metacloudservice.Driver;
import eu.metacloudservice.terminal.enums.Type;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class LoggerOutputStream extends ByteArrayOutputStream {

    private final Type logType;

    public LoggerOutputStream(final Type logType) {
        this.logType = logType;
    }

    @Override
    public void flush() {
        final var input = this.toString(StandardCharsets.UTF_8);
        this.reset();
        if (input != null && !input.isEmpty()) {
            Driver.getInstance().getTerminalDriver().log(this.logType, input.split("\n"));
            Driver.getInstance().getTerminalDriver().getLineReader().getTerminal().flush();
        }
    }
}
