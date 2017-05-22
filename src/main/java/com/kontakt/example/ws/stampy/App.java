package com.kontakt.example.ws.stampy;

import com.google.common.base.Throwables;

public class App {
    private static final String WEBSOCKET_ENDPOINT = "wss://ovs.kontakt.io:9090/stream";
    private static final String API_KEY = "xxx";
    private static final String GATEWAY_UNIQUE_ID = "xxx";

    public static void main(String[] args) {
        Object lock = new Object();
        try (WebSocketStampyClientExample client = new WebSocketStampyClientExample(WEBSOCKET_ENDPOINT, API_KEY, GATEWAY_UNIQUE_ID,
                        new WebSocketMessageHandler(lock))) {
            client.run();
            synchronized (lock) {
                lock.wait();
            }
        } catch (Exception e) {
            Throwables.propagate(e);
        }
        System.exit(0);
    }
}
