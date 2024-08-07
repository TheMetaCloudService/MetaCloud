/*
 * this class is by RauchigesEtwas
 */

package eu.metacloudservice.networking.packet.packets.out.service.playerbased.apibased;

import eu.metacloudservice.networking.packet.NettyBuffer;
import eu.metacloudservice.networking.packet.Packet;
import org.jetbrains.annotations.NotNull;

public class PacketOutAPIPlayerDispactchCommand extends Packet {

    private String userName;
    private String command;


    public PacketOutAPIPlayerDispactchCommand() {
        setPacketUUID(239489892);
    }

    public PacketOutAPIPlayerDispactchCommand(String userName, String command) {
        setPacketUUID(239489892);
        this.userName = userName;
        this.command = command;
    }

    @Override
    public void readPacket(@NotNull NettyBuffer buffer) {

        userName = buffer.readString();
        command = buffer.readString();

    }

    @Override
    public void writePacket(@NotNull NettyBuffer buffer) {
        buffer.writeString(userName);
        buffer.writeString(command);
    }

    public String getUserName() {
        return userName;
    }

    public String getCommand() {
        return command;
    }
}
