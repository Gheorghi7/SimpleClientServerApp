package dev.lpa.WEB;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.net.HttpURLConnection.HTTP_BAD_REQUEST;
import static java.net.HttpURLConnection.HTTP_OK;

public class OrderFulfillmentServer {

    private static AtomicLong lastID = new AtomicLong(1);

    public static long getNextID() {
        return lastID.getAndIncrement();
    }

    static void main() {

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/", exchange -> {
                String getParameters = exchange.getRequestURI().toString();
                String requestMethod = exchange.getRequestMethod();
                System.out.printf("%s %s %s%n",
                        exchange.getRemoteAddress(),
                        exchange.getRequestMethod(),
                        getParameters);
                String data = "";
                String response = "";

                int responseCode = HTTP_OK;
                if (requestMethod.equals("GET")) {
                    data = getParameters.substring(getParameters.indexOf("?") + 1);
                } else if (requestMethod.equals("POST")) {
                    data = new String(exchange.getRequestBody().readAllBytes());
                }
                System.out.println("Body data: " + data);
                Map<String, String> parameters = parseParameters(data);
                System.out.println("Parameters: " + parameters);

                if (parameters.size() == 2) {
                    LocalDateTime time = LocalDateTime.now();
                    LocalDateTime deliveryTime = time.plusDays(3);
                    response = """
                            { "order":
                                {
                                    "orderID":"%010d",
                                    "product":"%s",
                                    "amount":"%s",
                                    "orderReceived":"%s",
                                    "orderDeliveryDate":"%s"
                                }
                            }
                            """.formatted(
                            getNextID(),
                            parameters.get("product"),
                            parameters.get("amount"),
                            time.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                            deliveryTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                    .replaceAll("\\s", "")
                    );
                    System.out.println(response);
                } else {
                    response = "{\"result\":\"Bad data sent\"}";
                    responseCode = HTTP_BAD_REQUEST;
                }

                var bytes = response.getBytes();


                exchange.sendResponseHeaders(responseCode, bytes.length);
                exchange.getResponseBody().write(bytes);
                exchange.close();
            });
            server.start();
            System.out.println("Server Started on port 8080");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> parseParameters(String requestBody) {

        Map<String, String> parameters = new HashMap<>();
        String[] params = requestBody.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue.length == 2) {
                parameters.put(keyValue[0], keyValue[1]);
            }
        }

        return parameters;
    }

}
