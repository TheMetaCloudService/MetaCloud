package eu.themetacloudservice.terminal.streams;

import eu.themetacloudservice.Driver;
import eu.themetacloudservice.terminal.enums.Type;
import lombok.var;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TerminalError extends ByteArrayOutputStream {

    private Type type;

    public TerminalError(Type type) {
    }

    @Override
    public void flush() throws IOException {
        final var input = this.toString();
        this.reset();
        if (input != null && !input.isEmpty()) {
            String[] inputs = input.split("\n");
            Driver.getInstance().getTerminalDriver().log(this.type, inputs, inputs);
        }
    }
}
