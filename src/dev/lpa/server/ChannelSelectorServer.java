package dev.lpa.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ChannelSelectorServer {
    static void main() {
        try (ServerSocketChannel server = ServerSocketChannel.open()
        ) {
            server.bind(new InetSocketAddress(5000));
            server.configureBlocking(false);
            Selector selector = Selector.open();
            server.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        SocketChannel clientChannel = server.accept();
                        System.out.println("Accepted connection from " + clientChannel.getRemoteAddress());
                        clientChannel.configureBlocking(false);
                        clientChannel.register(selector, SelectionKey.OP_READ);
                    } else if (key.isReadable()) {
                        echoData(key);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Message: " + e.getMessage());
        }
    }

    private static void echoData(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bufferRead = clientChannel.read(buffer);

        if (bufferRead > 0) {
            buffer.flip();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            String message = "Echo " + new String(bytes);
            clientChannel.write(ByteBuffer.wrap(message.getBytes()));
        } else if (bufferRead == -1) {
            System.out.println("Client disconnected " + clientChannel.getRemoteAddress());
            key.cancel();
            clientChannel.close();
        }

    }
}
