package dev.lpa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadedSimpleServer {

    static void main() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(5000);) {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Sever accept client connection");
                socket.setSoTimeout(900_000);
                executorService.submit(() -> handleClientRequest(socket));
            }

        } catch (IOException e) {
            System.out.println("Server exeption " + e.getMessage());
        }
    }

    private static void handleClientRequest(Socket socket) {
        try (socket;
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            while (true) {
                String echoString = in.readLine();
                System.out.println("Server got request data: " + echoString);
                if (echoString.equals("exit")) {
                    break;
                }
                out.println("Echo from server: " + echoString);

            }
        } catch (IOException e) {
            System.out.println("Client Socket resources closed here ");
        }
    }
}
