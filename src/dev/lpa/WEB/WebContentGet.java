package dev.lpa.WEB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.stream.Stream;

import static java.net.HttpURLConnection.HTTP_OK;

public class WebContentGet {
    static void main() {
        try {
            URL url = new URL("http://localhost:8080/");
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(url.toURI())
                    .header("User-Agent", "Chrome")
                    .headers("Accept", "application/json", "Accept", "text/html")
                    .timeout(Duration.ofSeconds(30))
                    .build();

            HttpResponse<Stream<String>> response = client.send(request,
                    HttpResponse.BodyHandlers.ofLines());
            if (response.statusCode() != HTTP_OK) {
                System.out.println("Error reading web page " + url);
                return;
            }
            response.body()
                    .filter(s -> s.contains("<h1>"))
                    .map(s -> s.replace("<[^>]*>", "").strip())
                    .forEach(System.out::println);

            System.out.println(response.body());

        } catch (IOException | URISyntaxException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


}
