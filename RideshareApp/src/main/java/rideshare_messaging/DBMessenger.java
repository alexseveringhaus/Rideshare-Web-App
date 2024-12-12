package rideshare_messaging;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import rideshare_ridehandling.DBConnector;

public class DBMessenger extends DBConnector {
	//date format
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	//constructors
	public DBMessenger(String db, String user, String pass) {
		super(db, user, pass);
	}
	public DBMessenger() {
		super();
	}
	
	//tries to insert a new message into the database, returning true for success and false otherwise
	public boolean handleMessage(String content, int senderID, int recipientID, String timestamp) {
		PreparedStatement iSt = null;
		try {
			iSt = conn.prepareStatement("INSERT INTO Messages (message_content, sender_id, recipient_id, timestamp) VALUES (?, ?, ?, ?)");
			iSt.setString(1, content);
			iSt.setInt(2, senderID);
			iSt.setInt(3, recipientID);
			iSt.setTimestamp(4, Timestamp.valueOf(timestamp));
			return iSt.executeUpdate() > 0;
		}
		catch(Exception e) {
			System.out.println("Error while inserting message: " + e.getMessage());
		}
		finally {
			cleanUp(iSt);
		}
		return false;
	}
	
	
	//retrieves a list of Message objects for two particular users
	public List<Message> retrieveMessages(int senderID, int recipientID){
		PreparedStatement qSt = null;
		ResultSet qResults = null;
		List<Message> result = new ArrayList<Message>();
		try {
			//prepare query
			qSt = conn.prepareStatement("SELECT message_id, message_content, sender_id, recipient_id, timestamp " +
										"FROM Messages WHERE (sender_id = ? AND recipient_id = ?) OR (sender_id = ? AND recipient_id = ?) " +
										"ORDER BY timestamp ASC");
			qSt.setInt(1, senderID);
			qSt.setInt(2, recipientID);
			qSt.setInt(3, recipientID);
			qSt.setInt(4, senderID);
			
			//get results
			qResults = qSt.executeQuery();
			while(qResults.next()) {
				int message_id = qResults.getInt(1);
				String message_content = qResults.getString(2);
				int sender_id = qResults.getInt(3);
				int recipient_id = qResults.getInt(4);
				String timestamp = qResults.getString(5);
				result.add(new Message(message_id, message_content, sender_id, recipient_id, timestamp));
			}
		}
		catch(Exception e) {
			System.out.println("Error while retrieving messages: " + e.getMessage());
		}
		finally {
			cleanUp(qResults);
			cleanUp(qSt);
		}
		return result;
	}

    //retreives a list of conversations
    public List<Conversation> retrieveConversations(int userID){
        PreparedStatement qSt = null;
		ResultSet qResults = null;
		List<Conversation> result = new ArrayList<Conversation>();
		try {
			//prepare query
			qSt = conn.prepareStatement("SELECT DISTINCT users.user_id, username " +
                                        "FROM users " + 
                                        "INNER JOIN ( " +
                                            "SELECT sender_id, recipient_id FROM messages " +
                                            "GROUP BY sender_id, recipient_id " +
                                            "HAVING sender_id = ? OR recipient_id = ? " +
                                        ") AS t2 " +
                                        "ON users.user_id = t2.sender_id OR users.user_id = t2.recipient_id " + 
                                        "WHERE users.user_id != 3");
			qSt.setInt(1, userID);
			
			//get results
			qResults = qSt.executeQuery();
			while(qResults.next()) {
				int otherID = qResults.getInt(1);
				String username = qResults.getString(2);
				result.add(new Conversation(userID, otherID, username));
			}
		}
		catch(Exception e) {
			System.out.println("Error while retrieving conversations: " + e.getMessage());
		}
		finally {
			cleanUp(qResults);
			cleanUp(qSt);
		}
		return result;
    }
	
	//testing
	public static void main(String[] args) {
		DBMessenger db = new DBMessenger();
		//db.handleMessage("test message", 1, 2, "2024-12-03 08:25:00");
		//db.handleMessage("hello from 2 to 1", 2, 1, "2024-12-03 08:34:00");
		List<Message> results = db.retrieveMessages(1, 2);
		System.out.println(results);
		db.close();
	}
}
