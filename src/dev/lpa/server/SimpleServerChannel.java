package dev.lpa.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SimpleServerChannel {
    static void main() {

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.socket().bind(new InetSocketAddress(5000));
            serverChannel.configureBlocking(false);
            System.out.println("Server listening on port " +
                    serverChannel.socket().getLocalPort());
            List<SocketChannel> clientChannels = new ArrayList<>();

            while (true) {
//                System.out.println("Waiting for connection...");
                SocketChannel clientChannel = serverChannel.accept();
                if (clientChannel != null) {
                    clientChannels.add(clientChannel);
                    System.out.printf("Client %s connected\n", clientChannel.socket().getRemoteSocketAddress());
                    clientChannel.configureBlocking(false);
                }

                ByteBuffer buffer = ByteBuffer.allocate(1024);
                for (int i = 0; i < clientChannels.size(); i++) {
                    SocketChannel channel = clientChannels.get(i);
                    clientChannels.add(clientChannel);
                    int readBytes = clientChannel.read(buffer);

                    if (readBytes > 0) {
                        buffer.flip();
                        channel.write(ByteBuffer.wrap("Echo from server: ".getBytes()));
                        while (buffer.hasRemaining()) {
                            channel.write(buffer);
                        }
                    } else if (readBytes == -1) {
                        System.out.printf("Connection to %s lost", channel.socket().getRemoteSocketAddress());
                        clientChannels.remove(i);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Server exeption" + e.getMessage());
        }
    }
}
