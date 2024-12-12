
package rideshare_messaging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
	private static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private String content;
	private int senderID;
	private int recipientID;
	private int messageID;
	private LocalDateTime timestamp;
	
	//constructors
	public Message(String content, int senderID, int recipientID, String timestamp) {
		this.content = content;
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.timestamp = LocalDateTime.parse(timestamp, dateFormat);
		this.messageID = -1;
	}
	public Message(int messageID, String content, int senderID, int recipientID, String timestamp) {
		this.messageID = messageID;
		this.content = content; 
		this.senderID = senderID;
		this.recipientID = recipientID;
		this.timestamp = LocalDateTime.parse(timestamp, dateFormat);
	}
	
	//getter methods
	public String getContent() {
		return this.content;
	}
	public int getSender() {
		return this.senderID;
	}
	public int getRecipient() {
		return this.recipientID;
	}
	public LocalDateTime getTime() {
		return this.timestamp;
	}
	
	//for debugging
	public String toString() {
		return "\n{\n\t'messageID': " + (messageID == -1 ? "null":messageID) + ",\n" + 
				"\t'content': " + content + ",\n" + 
				"\t'senderID': " + senderID + ",\n" +
				"\t'recipientID': " + recipientID + ",\n" + 
				"\t'timestamp': " + timestamp.format(dateFormat) + "\n}\n";
	}
}
