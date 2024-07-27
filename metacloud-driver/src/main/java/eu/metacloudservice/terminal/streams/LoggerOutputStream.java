/*
 * this class is by RauchigesEtwas
 */

/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.terminal.streams;

import eu.metacloudservice.Driver;
import eu.metacloudservice.networking.NettyDriver;
import eu.metacloudservice.networking.packet.packets.in.node.PacketInSendConsole;
import eu.metacloudservice.terminal.enums.Type;
import lombok.NonNull;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public final class LoggerOutputStream extends ByteArrayOutputStream {

    private final Type logType;

    public LoggerOutputStream(@NonNull final  Type logType) {
        this.logType = logType;
    }

    @Override
    public void flush() {
        final var input = this.toString(StandardCharsets.UTF_8);
        this.reset();
        if (input != null && !input.isEmpty()) {
            if (Driver.getInstance().getMessageStorage().sendConsoleToManager){
                NettyDriver.getInstance().nettyClient.sendPacketsAsynchronous(new PacketInSendConsole(Driver.getInstance().getMessageStorage().sendConsoleToManagerName, input));
            }
            Driver.getInstance().getTerminalDriver().log(this.logType, input.split("\n"));
            Driver.getInstance().getTerminalDriver().getLineReader().getTerminal().flush();
        }
    }
}
