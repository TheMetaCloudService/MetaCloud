package eu.themetacloudservice.networking.channels;


import eu.themetacloudservice.networking.Networking;
import eu.themetacloudservice.networking.ServerSession;
import eu.themetacloudservice.networking.worker.Options;
import eu.themetacloudservice.networking.Structure;
import eu.themetacloudservice.networking.handlers.listener.ClientConnectEvent;
import eu.themetacloudservice.networking.handlers.listener.ClientDisconectEvent;
import eu.themetacloudservice.networking.handlers.listener.NetworkExceptionEvent;
import eu.themetacloudservice.networking.handlers.listener.PacketReceivedEvent;
import eu.themetacloudservice.networking.protocol.Buffer;
import eu.themetacloudservice.networking.protocol.IBuffer;
import eu.themetacloudservice.networking.protocol.Packet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
public class Channel implements IChannel, Runnable {

    private final Thread thread = new Thread(this);

    private final ChannelPipeline pipeline = new ChannelPipeline();

    private IChannelInitializer initializer;

    private final Structure structure;

    private final Socket socket;

    private boolean connected;


    public Socket getSocket() {
        return socket;
    }

    public Channel(Structure structure, Socket socket) {
        this.structure = structure;
        this.socket = socket;
        this.connected = true;
    }

    public Channel(Structure structure) {
        this.structure = structure;
        this.socket = new Socket();
    }

    public void connect(SocketAddress address) {
        try {
            socket.connect(address);
            connected = true;
            initializer.initChannel(this);
            socket.setKeepAlive(true);
            Networking.packetListenerHandler.executeEvent(new ClientConnectEvent(this));
        } catch (IOException e) {

        }

    }

    public void start() {
        thread.start();
    }

    @Override
    public void run() {

        while (connected) {
            try {
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                ByteArrayOutputStream array = new ByteArrayOutputStream();
                int read, count = 0;
                while (connected && (read = inputStream.readByte()) > -1) {
                    count++;
                    if (structure.getOption(Options.BUFFER_SIZE) < count) {
                        throw new Exception("The buffer is too big, allowed size " + structure.getOption(Options.BUFFER_SIZE));
                    }
                    array.write(read);
                }
                Packet decode = pipeline.getDecoder().decode(new Packet() {
                    @Override
                    public void write(IBuffer buffer) {}
                    @Override
                    public void read(IBuffer buffer) {}
                }, new Buffer(array.toByteArray()));
                Networking.packetListenerHandler.executeEvent(new PacketReceivedEvent(this, decode));
            } catch (IOException e) {
                Networking.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
                close();
            } catch (Exception exception) {
                Networking.packetListenerHandler.executeEvent(new NetworkExceptionEvent(exception));
                close();
            }
        }
    }

    @Override
    public void close() {
        try {
            if (socket != null && connected) {
                connected = false;
                socket.close();
                if (structure instanceof ServerSession) ((ServerSession) structure).getChannels().remove(this);
                Networking.packetListenerHandler.executeEvent(new ClientDisconectEvent(this));

            }
        } catch (IOException e) {
            Networking.packetListenerHandler.executeEvent(new NetworkExceptionEvent(e));
            close();
        }
    }

    @Override
    public void sendPacket(Packet packet) {
        try {
            DataOutputStream data = new DataOutputStream(socket.getOutputStream());
            pipeline.getEncoder().encode(data, packet, new Buffer());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void init(IChannelInitializer initializer) {
        this.initializer = initializer;
    }

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public ChannelPipeline getPipeline() {
        return pipeline;
    }

    @Override
    public SocketAddress getRemoteAddress() {
        return socket.getRemoteSocketAddress();
    }

    @Override
    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }
}
