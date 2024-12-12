package rideshare_messaging;

public class ClientMessage {
	private String message;
	private String timestamp;
	private int senderID;
	private int recipientID;
	public ClientMessage(String message, String timestamp, int senderID, int recipient) {
		this.message = message;
		this.timestamp = timestamp;
		this.senderID = senderID;
		this.recipientID = recipient;
	}
	
	public String getMessage() {
		return this.message;
	}
	public String getTimestamp() {
		return this.timestamp;
	}
	public int getSender() {
		return this.senderID;
	}
	public int getRecipient() {
		return this.recipientID;
	}
}