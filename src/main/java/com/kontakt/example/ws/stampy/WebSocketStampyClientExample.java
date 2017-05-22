package com.kontakt.example.ws.stampy;

import asia.stampy.client.message.disconnect.DisconnectMessage;
import asia.stampy.client.message.subscribe.SubscribeMessage;

import javax.websocket.*;
import java.io.Closeable;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;

public class WebSocketStampyClientExample implements Closeable {

    private static final String TOPIC_PREFIX = "/presence/stream/";
    private static final String API_KEY_HEADER_NAME = "api-key";
    private static final int MESSAGE_BUFFER_SIZE = 1048576;
    private static final String END_HEADER_NAME = "end";
    private final String endpoint;
    private final String apiKey;
    private final String resourceId;
    private final Object webSocketMessageHandler;

    private Session session;

    public WebSocketStampyClientExample(String endpoint,
                                        String apiKey,
                                        String resourceId,
                                        Object webSocketMessageHandler) {
        this.endpoint = checkNotNull(endpoint, "WebSocket endpoint must be set!");
        this.apiKey = checkNotNull(apiKey, "Api key must be set!");
        this.resourceId = checkNotNull(resourceId, "Place id or gateway unique id must be set!");
        this.webSocketMessageHandler = checkNotNull(webSocketMessageHandler, "webSocketMessageHandler must be set!");
    }


    public void run() throws URISyntaxException, IOException, DeploymentException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(MESSAGE_BUFFER_SIZE);
        container.setDefaultMaxTextMessageBufferSize(MESSAGE_BUFFER_SIZE);
        this.session = container.connectToServer(this.webSocketMessageHandler, new URI(this.endpoint));

        RemoteEndpoint.Basic remote = this.session.getBasicRemote();
        remote.sendText(new FixedBodyConnectMessage(this.endpoint).toStompMessage(true));

        String id = UUID.randomUUID().toString();
        SubscribeMessage subscribeMessage = new FixedSubscribeMessage(TOPIC_PREFIX + this.resourceId, id);
        subscribeMessage.getHeader().addHeader(API_KEY_HEADER_NAME, this.apiKey);
        remote.sendText(subscribeMessage.toStompMessage(true));
    }

    public void close() throws IOException {
        if (this.session == null) {
            return;
        }

        final DisconnectMessage disconnectMessage = new DisconnectMessage();
        disconnectMessage.getHeader().setReceipt(END_HEADER_NAME);
        if (!this.session.isOpen()) {
            return;
        }
        this.session.getBasicRemote().sendText(disconnectMessage.toStompMessage(false));
        this.session.close();
    }
}
