package org.gosh.hololens.repository.api.websocket;

import org.json.JSONObject;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    @Override
    public String encode(Message object) throws EncodeException {

        JSONObject pos = new JSONObject();
        pos.put("x",object.getPosition()[0]);
        pos.put("y",object.getPosition()[1]);
        pos.put("z",object.getPosition()[2]);

        JSONObject res = new JSONObject();
        res.put("sender",object.getPosition());
        res.put("content",object.getContent());
        res.put("position",pos.toString());
        res.put("direct",object.isDirect());
        return res.toString();
    }

    @Override
    public void init(EndpointConfig config) {

    }

    @Override
    public void destroy() {

    }
}
