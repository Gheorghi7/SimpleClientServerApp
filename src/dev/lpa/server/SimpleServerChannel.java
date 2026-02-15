package dev.lpa.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class SimpleServerChannel {
    static void main() {

        try (ServerSocketChannel serverChannel = ServerSocketChannel.open()) {
            serverChannel.socket().bind(new InetSocketAddress(5000));
            System.out.println("Server listening on port " +
                    serverChannel.socket().getLocalPort());
            while (true) {
                SocketChannel clientChannel = serverChannel.accept();
                System.out.printf("Client %s connected\n", clientChannel.socket().getRemoteSocketAddress());
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                SocketChannel channel = clientChannel;
                int readBytes = clientChannel.read(buffer);

                if(readBytes > 0 ) {
                    buffer.flip();
                    channel.write(ByteBuffer.wrap("Echo from server: ".getBytes()));
                    while(buffer.hasRemaining()) {
                        channel.write(buffer);
                    }
                }
                else if(readBytes == -1) {
                    System.out.printf("Connection to %s lost", channel.socket().getRemoteSocketAddress() );
                }
            }
        } catch (IOException e) {
            System.out.println("Server exeption" + e.getMessage());
        }
    }
}
