package org.gosh.hololens.repository.api.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.websocket.EncodeException;
import javax.websocket.Session;

public class Room {

	

    private String name;
    private List<Session> sessions = new ArrayList<>();

    public Room(String name) {
        this.name = name;
    }

    public synchronized void join(Session session) {
        sessions.add(session);
    }

    public synchronized void leave(Session session) {
        sessions.remove(session);
    }

    public synchronized void sendMessage(Message message) {
        sessions.parallelStream()
                .filter(Session::isOpen)
                .forEach(session -> sendMessage(message, session));
    }
    private void sendMessage(Message message, Session session) {
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }
	
}
