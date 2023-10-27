package com.example;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Calendar;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class MyApp {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(new InetSocketAddress(80), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started on port 80");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String response = "<html><head><title>Welcome to Harrier Panels Page!</title></head><body><div style=\"text-align:center\"><img src=\"https://blogger.googleusercontent.com/img/b/R29vZ2xl/AVvXsEid812UJHFiQs8-SCK2BoakeB1zGxXmqfVk1sfHzudQhd5wjnaoUbePaL-uR0Bqqx4sIW6grWYEk2QuhUjefeynN2wSIsLOo0kQI0MTfDn60VB84CnN6KPo-A98s7vzyg/s220/hp.png\"><br>Welcome to <a href=\"https://aviasimulator.blogspot.com\">Harrier Panels</a> Page!<br>Powered by Java &copy; Harrier Panels " + Calendar.getInstance().get(Calendar.YEAR) + "</div></body></html>";
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
