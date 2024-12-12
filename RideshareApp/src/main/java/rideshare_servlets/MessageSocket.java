
package rideshare_servlets;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import rideshare_messaging.ClientMessage;
import rideshare_messaging.DBMessenger;

@ServerEndpoint(value = "/message-socket")
public class MessageSocket {

	private static Map<String, ArrayList<Session>> sessions = Collections.synchronizedMap(new HashMap<String, ArrayList<Session>>());
	
	//helper functions to retrieve information about the session
	private String getConvoID(Session session) {
		Map<String, List<String>> params = session.getRequestParameterMap();
		int senderID = Integer.parseInt(params.get("sender").get(0));
		int recipientID = Integer.parseInt(params.get("recipient").get(0));
		if(senderID < recipientID) {
			return senderID + "-" + recipientID;
		}
		return recipientID + "-" + senderID;
	}
	private String getConvoID(int senderID, int recipientID) {
		if(senderID < recipientID) {
			return senderID + "-" + recipientID;
		}
		return recipientID + "-" + senderID;
	}
	
	@OnOpen
	public void open(Session session) {
		//insert into current map of sessions
		String id = getConvoID(session);
		if(!sessions.containsKey(id)) {
			ArrayList<Session> s = new ArrayList<Session>();
			s.add(session);
			sessions.put(id, s);
		}
		else {
			sessions.get(id).add(session);
		}
		System.out.println("Added new session with id " + id);
	}
	
	@OnMessage
	public void onMessage(String message, Session session) {
		try {
			//try to parse it with GSON
			Gson gson = new Gson();
			ClientMessage msgObj = gson.fromJson(message, ClientMessage.class);
			
			//getting information
			String msgText = msgObj.getMessage();
			int sender = msgObj.getSender();
			int recipient = msgObj.getRecipient();
			String time = msgObj.getTimestamp();
			//System.out.println("Got message: " + msgText + " (sender: " + sender + " recipient: " + recipient + " time: " + time + ")");
			
			//broadcast it to the other session, if present
			String id = getConvoID(sender, recipient);
			ArrayList<Session> ls = sessions.get(id);
			synchronized(ls) {
				for(Session s : ls) {
					if(s != session) {
						s.getBasicRemote().sendText(msgText);
					}
				}
			}
			
			//also add it to the database
			DBMessenger db = new DBMessenger();
			db.handleMessage(msgText, sender, recipient, time);
		} 
		catch (Exception e) {
			System.out.println("Problem handling message: " + e.getMessage());
			close(session);
		}
	}
	
	@OnClose
	public void close(Session session) {
		//remove this from the active sessions
		String id = getConvoID(session);
		sessions.get(id).remove(session);
		if(sessions.get(id).isEmpty()) {
			sessions.remove(id);
		}
		System.out.println("Closed session with id " + id);
	}
	
	@OnError
	public void error(Throwable error) {
		System.out.println("Error: " + error.getMessage());
	}
}
