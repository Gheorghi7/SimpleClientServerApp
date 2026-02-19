package dev.lpa.client;

import java.io.IOException;
import java.net.*;

public class UDPClient {
    private static final int SERVER_PORT = 5000;
    private static final int PACKET_SIZE = 1024;

    static void main() {

        try
                (DatagramSocket socket = new DatagramSocket();) {
            byte[] audioFileName = "AudioClip.wav".getBytes();
            DatagramPacket packet1 = new DatagramPacket(audioFileName, audioFileName.length,
                    InetAddress.getLocalHost(),
                    SERVER_PORT
            );
            socket.send(packet1);

        } catch (SocketException e) {
            System.out.println(e.getMessage());
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
