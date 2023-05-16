package sprint1;
import java.io.Serializable;
import java.util.ArrayList;

public class Channel implements Serializable
{
	private static final long serialVersionUID = 1245330570559729688L;
	
	public ArrayList<Integer> messageIDs;
	public int Ch_ID;
	public String Topic;
	public int LastMessageID;
	
	public Channel(){}

	public Channel(String Topic) {
		messageIDs = new ArrayList<Integer>();
		this.Topic = Topic;
		Ch_ID = 0;
	}
	public int getCh_ID() {
		return Ch_ID;
	}

	public void setCh_ID(int Ch_ID) {
		this.Ch_ID = Ch_ID;
	}

	public String getTopic() {
		return Topic;
	}

	public void setTopic(String topic) {
		this.Topic = topic;
	}
	
	public ArrayList<Integer> getMessageIDs() {
		return messageIDs;
	}

	public void setMessageIDs(ArrayList<Integer> messageIDs) {
		this.messageIDs = messageIDs;
	}

	public boolean SendMessage(String Contents, int SenderID, int groupID, Database DB) {
		// needs access to the DB in order to actually grab data
		int gID = DB.getGroup(groupID).getGroupID();
		int sID = DB.getGroup(groupID).UserIDs.contains(SenderID) ? SenderID:0;
		if (gID == groupID && sID == SenderID && DB.getGroup(groupID).CanPost(SenderID))
		{
			Message newMessage = new Message(Contents, SenderID);
			int id =DB.insertMessage(newMessage);
			messageIDs.add(newMessage.MessageID);
			LastMessageID=id;
			return true;
		}
		return false;
	}
}
