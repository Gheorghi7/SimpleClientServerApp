package dev.lpa.server;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

public class UDPPacketServer {
    private static final int PORT = 5000;
    private static final int PACKET_SIZE = 1024;

    static void main() {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            byte[] buffer = new byte[PACKET_SIZE];
            System.out.println("Waiting for client connection...");
            DatagramPacket clientSocket = new DatagramPacket(buffer, buffer.length);
            serverSocket.receive(clientSocket);
            String audioFileName = new String(buffer, 0, clientSocket.getLength());
            System.out.println("Client request to listen to: " + audioFileName);
            try {
                File audioFile = new File(audioFileName);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
                System.out.println(audioInputStream.getFormat());
            } catch (UnsupportedAudioFileException e) {
                System.out.println(e.getMessage());
            }
            sendDataToClient(audioFileName, serverSocket, clientSocket);
        } catch (Exception e) {
            System.out.println("UDPPacketServer Exception: " + e.getMessage());
        }


    }

    private static void sendDataToClient(String file, DatagramSocket serverSocket, DatagramPacket clientSocket) {
        ByteBuffer buffer = ByteBuffer.allocate(PACKET_SIZE);

        try (FileChannel fileChannel = FileChannel.open(Paths.get(file), StandardOpenOption.READ)) {
            InetAddress clientIP = clientSocket.getAddress();
            int clientPort = clientSocket.getPort();
            while (true) {
                buffer.clear();
                if (fileChannel.read(buffer) == -1) {
                    break;
                }
                buffer.flip();
            }

            while (buffer.hasRemaining()) {
                byte[] data = new byte[buffer.remaining()];
                buffer.get(data);
                DatagramPacket packet = new DatagramPacket(data, data.length, clientIP, clientPort);
                serverSocket.send(packet);
            }
            try {
                TimeUnit.MILLISECONDS.sleep(22);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }


}
