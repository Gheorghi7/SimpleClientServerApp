package dev.lpa.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServer {

    static void main() {
        try (ServerSocket serverSocket = new ServerSocket(5000);) {
            try (Socket socket = serverSocket.accept();) {
                System.out.println("Sever accept client connection");
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

                while(true){
                    String echoString = in.readLine();
                    System.out.println("Server got request data: "+echoString);
                    if(echoString.equals("exit")){
                        break;
                    }
                    out.println("Echo from server: "+echoString);

                }

            }
        } catch (IOException e) {
            System.out.println("Server exeption " + e.getMessage());
        }
    }
}
