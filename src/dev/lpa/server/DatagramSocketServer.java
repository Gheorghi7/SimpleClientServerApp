package dev.lpa.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DatagramSocketServer {

    private static final int SERVER_PORT = 5000;
    private static final int PACKET_SIZE = 1024;

    static void main() {
        try (DatagramChannel channel = DatagramChannel.open()) {
            channel.bind(new InetSocketAddress(SERVER_PORT));
            System.out.println("Server started on port " + SERVER_PORT);
            Selector selector = Selector.open();
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_READ);
            ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);

            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();

                    if (key.isReadable()) {

                        var registerEvent = (DatagramChannel) key.channel();
                        buffer.clear();
                        var clientAddress = registerEvent.receive(buffer);
                        buffer.flip();
                        byte[] bytes = new byte[buffer.remaining()];
                        buffer.get(bytes);
                        String audioFileName = new String(bytes);
                        System.out.println("Client request to listen to: " + audioFileName);
                        new Thread(() -> {
                            try {
                                sendDataToClient(audioFileName, clientAddress, registerEvent);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }).start();
                    }
                }
            }


        } catch (IOException e) {
            System.out.println("Server stopped: " + e.getMessage());
        }

    }

    private static void sendDataToClient(String file, SocketAddress address,
                                         DatagramChannel channel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);

        try (
                FileChannel fileChannel = FileChannel.open(Paths.get(file), StandardOpenOption.READ);
        ) {
            while (true) {
                buffer.clear();
                int read = fileChannel.read(buffer);
                if (read == -1) {
                    break;
                }
                buffer.flip();//=(when you flip)=> limit = position =(than)=> position = 0;
                while (buffer.hasRemaining()) {
                    channel.send(buffer, address);
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(22);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }


            }


        }


    }
}
