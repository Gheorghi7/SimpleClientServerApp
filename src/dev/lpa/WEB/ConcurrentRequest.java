package dev.lpa.WEB;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ConcurrentRequest {
    static void main() {
        Map<String, Integer> orderMap = Map.of(
                "apples", 500,
                "orange", 1000,
                "bananas", 750,
                "carrots", 2000,
                "cantaloupes", 1000
        );
        String urlparams = "product=%s&amount=%d";
        String urlBase = "http://localhost:8080";

        List<URI> sites = new ArrayList<>();
        orderMap.forEach((key, value) -> sites.add(URI.create(urlBase + "?" + urlparams.formatted(key, value))));
        HttpClient client = HttpClient.newHttpClient();
        sendGets(client, sites);
    }

    private static void sendGets(HttpClient client, List<URI> uris) {
        var futures = uris.stream()
                .map(uri -> HttpRequest.newBuilder(uri))
                .map(HttpRequest.Builder::build)
                .map(request -> client.sendAsync(request, HttpResponse.BodyHandlers.ofString()))
                .toList();

        var allFuturesRequest = CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[0]));
        allFuturesRequest.join();
        futures.forEach(f ->
                System.out.println(f.join().body()));


    }
}
