package dev.lpa.WEB;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import static java.net.HttpURLConnection.HTTP_OK;

public class WebContent {
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
            connection.setRequestMethod("GET");
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", "Chrome");
            connection.setRequestProperty("Accept", "application/json , text/html");
            connection.setReadTimeout(30_000);
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
