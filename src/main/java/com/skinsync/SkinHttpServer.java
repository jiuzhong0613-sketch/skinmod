package com.skinsync;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.Executors;

public class SkinHttpServer {
    private static HttpServer server;

    public static synchronized void start() {
        if (server != null) return;
        try {
            int port = SkinConfig.HTTP_PORT.get();
            server = HttpServer.create(new InetSocketAddress("0.0.0.0", port), 0);
            server.createContext("/skins/", exchange -> {
                try {
                    String path = exchange.getRequestURI().getPath();
                    String file = path.substring("/skins/".length());
                    if (!file.endsWith(".png")) {
                        exchange.sendResponseHeaders(404, -1);
                        return;
                    }
                    UUID id;
                    try {
                        id = UUID.fromString(file.substring(0, file.length() - 4));
                    } catch (IllegalArgumentException e) {
                        exchange.sendResponseHeaders(400, -1);
                        return;
                    }
                    SkinRecord rec = SkinStore.get(id);
                    if (rec == null) {
                        exchange.sendResponseHeaders(404, -1);
                        return;
                    }
                    byte[] data = rec.png();
                    exchange.getResponseHeaders().add("Content-Type", "image/png");
                    exchange.getResponseHeaders().add("Cache-Control", "no-cache");
                    exchange.sendResponseHeaders(200, data.length);
                    try (OutputStream out = exchange.getResponseBody()) {
                        out.write(data);
                    }
                } catch (Exception e) {
                    try {
                        exchange.sendResponseHeaders(500, -1);
                    } catch (Exception ignored) {
                    }
                } finally {
                    exchange.close();
                }
            });
            server.setExecutor(Executors.newFixedThreadPool(2));
            server.start();
            SkinSync.LOGGER.info("Skin HTTP server listening on 0.0.0.0:{}", port);
        } catch (Exception e) {
            SkinSync.LOGGER.error("Failed to start skin HTTP server", e);
        }
    }
}
