package com.kontakt.example.ws.stampy;

import javax.websocket.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.google.common.base.Preconditions.checkNotNull;

@ClientEndpoint(subprotocols = "stomp")
public class WebSocketMessageHandler {
    private static final Logger LOGGER = Logger.getLogger(WebSocketMessageHandler.class.getName());
    private int NUMBER_OF_MESSAGES = 20;
    private final Object lock;
    private int counter = 0;

    public WebSocketMessageHandler(Object lock) {
        this.lock = checkNotNull(lock, "Lock object must be not null!");
    }

    @OnMessage
    public void onMessage(final String msg) {
        LOGGER.info(msg);
        synchronized (lock) {
            counter++;
            if (counter == NUMBER_OF_MESSAGES) {
                lock.notify();
            }
        }
    }

    @OnError
    public void onError(Throwable error) {
        LOGGER.log(Level.SEVERE, "Error during handling web socket", error);
        synchronized (lock) {
            lock.notify();
        }
    }

    @OnClose
    public void onClose(CloseReason reason) {
        LOGGER.info("Web socket closed: " + reason.getReasonPhrase());
        synchronized (lock) {
            lock.notify();
        }
    }
}
