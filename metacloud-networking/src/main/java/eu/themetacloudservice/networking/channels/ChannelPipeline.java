package eu.themetacloudservice.networking.channels;



import eu.themetacloudservice.networking.Networking;
import eu.themetacloudservice.networking.handlers.bin.PacketListener;
import eu.themetacloudservice.networking.protocol.codec.IDecoder;
import eu.themetacloudservice.networking.protocol.codec.IEncoder;

public class ChannelPipeline {

    private IEncoder encoder = new IEncoder.NetPacketEncoder();
    private IDecoder decoder = new IDecoder.NetPacketDecoder();



    public ChannelPipeline addLast(IEncoder encoder) {
        if (encoder == null) return this;
        this.encoder = encoder;
        return this;
    }

    public ChannelPipeline addLast(IDecoder decoder) {
        if (decoder == null) return this;
        this.decoder = decoder;
        return this;
    }


    public void addLast(PacketListener packetListener){
        Networking.packetListenerHandler.registerListener(packetListener);
    }

    public IEncoder getEncoder() {
        return encoder;
    }

    public IDecoder getDecoder() {
        return decoder;
    }

}
