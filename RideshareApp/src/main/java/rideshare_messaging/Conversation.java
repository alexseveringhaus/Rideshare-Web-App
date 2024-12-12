
package rideshare_messaging;

public class Conversation {
	private int senderID;
	private int recipientID;
	private String recipientUsername;
	
	//constructor
	public Conversation(int senderID, int recipientID, String user) {
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.recipientUsername = user;
	}
	
	//getter methods
	public String getUsername() {
		return this.recipientUsername;
	}
	public int getSender() {
		return this.senderID;
	}
	public int getRecipient() {
		return this.recipientID;
	}
}
