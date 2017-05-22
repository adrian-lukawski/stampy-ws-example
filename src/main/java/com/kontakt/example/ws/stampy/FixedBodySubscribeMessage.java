package com.kontakt.example.ws.stampy;


import asia.stampy.client.message.subscribe.SubscribeMessage;

/**
 * SUBSCRIBE message should not have a payload.
 * At the moment due {@link asia.stampy.common.message.AbstractMessage#toStompMessage(boolean)}
 * inappropriate implementation it has "null" in payload.
 */
public class FixedBodySubscribeMessage extends SubscribeMessage {

    FixedBodySubscribeMessage(String destination, String id) {
        super(destination, id);
    }

    @Override
    protected String postHeader() {
        return "";
    }
}
