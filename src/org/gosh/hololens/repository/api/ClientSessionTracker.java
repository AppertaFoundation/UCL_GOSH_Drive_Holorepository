package org.gosh.hololens.repository.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

import org.gosh.hololens.repository.api.websocket.Room;

@ApplicationScoped
public class ClientSessionTracker {
    private final Map<String, Room> rooms = Collections.synchronizedMap(new HashMap<String, Room>());

    private static final String[] roomNames = {"one"};


    @PostConstruct
    public void initialise() {
        Arrays.stream(roomNames).forEach(roomName -> rooms.putIfAbsent(roomName, new Room(roomName)));
        Logger.getLogger("InfoLogging").info(rooms.get("one").toString());
        ;
    }


    public Map<String, Room> getRooms() {
        return rooms;
    }

}
