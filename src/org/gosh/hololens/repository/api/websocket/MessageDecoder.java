package org.gosh.hololens.repository.api.websocket;

import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.io.StringReader;

public class MessageDecoder implements Decoder.Text<Message> {
    @Override
    public Message decode(String s) throws DecodeException {
        Message message = new Message();
        JSONObject decoded = new JSONObject(s);
        JSONObject position = decoded.getJSONObject("position");
        message.setContent(decoded.getString("content"));
        message.setSender(decoded.getString("sender"));
        message.setDirect(decoded.getBoolean("direct"));
        message.setPosition(position.getDouble("x"),
                position.getDouble("y"),
                position.getDouble("z"));
        return message;
    }

    @Override
    public boolean willDecode(String s) {
        return true;
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
