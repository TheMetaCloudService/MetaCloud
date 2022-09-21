package eu.themetacloudservice.networking.protocol;

public abstract class Packet {

    public abstract void write(IBuffer buffer);

    public abstract void read(IBuffer buffer);
}
