package sprint1;
import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable
{
	private static final long serialVersionUID = -5568085119273317419L;
	
	String content;
	int MessageID;
	int SenderID;
	LocalDateTime Timestamp;
	
	public Message(){}

	public Message(String content, int SenderID) {
		this.content = content;
		this.SenderID = SenderID;
		this.Timestamp = LocalDateTime.now();
		this.MessageID = 0;

	}
	
	public void setContent(String content) {
		this.content = content;
	}

	public void setSenderID(int senderID) {
		SenderID = senderID;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		Timestamp = timestamp;
	}

	public int getMessageID() {
		return MessageID;
	}

	public void setMessageID(int messageID) {
		this.MessageID = messageID;
	}

	public String getContent() {
		return content;
	}

	public int getSenderID() {
		return SenderID;
	}

	public LocalDateTime getTimestamp() {
		return Timestamp;
	}
}
