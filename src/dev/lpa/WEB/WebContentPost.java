package dev.lpa.WEB;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;

public class WebContentPost {
    static void main() {
        try {
//            URL url = new URL("http://example.com");
//            URL url = new URL("https://jsonplaceholder.typicode.com/todos?id=5");
            URL url = new URL("http://localhost:8080/");
////            printContent(url.openStream());
//            URLConnection connection = url.openConnection();
//            System.out.println(connection.getContentType());
//            connection.getHeaderFields().
//                    entrySet().
//                    forEach(System.out::println);
//            System.out.println(connection.getHeaderField("Cache-Control"));
//            connection.connect();
//            printContent(connection.getInputStream());

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Chrome");
            connection.setRequestProperty("Accept", "application/json , text/html");
            connection.setReadTimeout(30_000);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            String parameters = "first=Joe&last=Smith";
            int length = parameters.getBytes().length;
            connection.setRequestProperty("Content-Length", String.valueOf(length));

            DataOutputStream output = new DataOutputStream(connection.getOutputStream());
            output.writeBytes(parameters);
            output.flush();
            output.close();

            int responseCode = connection.getResponseCode();
            System.out.printf("Response code: %s%n", responseCode);
            if (responseCode != HTTP_OK) {
                System.out.println("Error reading web site " + url);
                return;
            }
            printContent(connection.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private static void printContent(InputStream is) {
        try (
                BufferedReader inputStream = new BufferedReader(new InputStreamReader(is));
        ) {
            String line;
            while ((line = inputStream.readLine()) != null) {
                System.out.println(line);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
