package dev.lpa.client;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class SimpleClient {
    static void main() {
        try (Socket socket = new Socket("localhost", 5000);) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String requestString;
            String responseString;
            do {
                System.out.println("Enter String to be echoed(Send to Server): ");
                requestString = scanner.nextLine();
                out.println(requestString);
                if (!requestString.equals("exit")) {
                    responseString = in.readLine();
                    System.out.println(responseString);
                }
            } while (!requestString.equals("exit"));
        } catch (UnknownHostException e) {
            System.out.println("Host not found");
        } catch (IOException e) {
            System.out.println("Client Error: " + e.getMessage());
        } finally {
            System.out.println("Server Disconnected");
        }
    }


}
