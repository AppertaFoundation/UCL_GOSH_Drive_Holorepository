package org.gosh.hololens.repository.api.websocket;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;


@ServerEndpoint(value = "/hololens/{userName}", encoders = MessageEncoder.class,decoders = MessageDecoder.class)
public class HololensWebSocket {

	//TODO add support for multiple "Holographic Sessions"
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    


	 
	
	@OnOpen
	public void joinSession(
			final Session session,
			@PathParam("userName") String userName
			) throws IOException, EncodeException {
		session.setMaxIdleTimeout(5*60*1000);
		session.getUserProperties().putIfAbsent("userName", userName);
		Logger.getLogger("InfoLogging").info(userName);;
		peers.add(session);
	}
	


	@OnMessage
	public void onUpdate(Message message,Session session) throws IOException, EncodeException {
	     for (Session peer : peers) {
	            if (!session.getId().equals(peer.getId())) { // do not resend the message to its sender
	                peer.getBasicRemote().sendObject(message);
	            }
	        }
	}

    @OnClose
    public void onClose(Session session, CloseReason reason)  {
	  	
    	 peers.remove(session);
    
    }

	@OnError
	public void onError(Session session, Throwable error) {
	  	Message errorMessage = new Message();
		StringWriter sw = new StringWriter();
		error.printStackTrace(new PrintWriter(sw));
		String exceptionAsString = sw.toString();
		System.out.println(exceptionAsString);
	  	errorMessage.setContent(exceptionAsString);
	  	peers.remove(session);
	}

}
