package com.kontakt.example.ws.stampy;


import asia.stampy.client.message.connect.ConnectMessage;

/**
 * CONNECT message should not have a payload.
 * At the moment due {@link asia.stampy.common.message.AbstractMessage#toStompMessage(boolean)}
 * inappropriate implementation it has "null" in payload.
 */
public class FixedBodyConnectMessage extends ConnectMessage {

    FixedBodyConnectMessage(String host) {
        super(host);
    }

    @Override
    protected String postHeader() {
        return "";
    }
}
