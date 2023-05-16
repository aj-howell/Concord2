package sprint2;
import java.time.LocalDateTime;
import java.util.ArrayList;

import sprint1.*;

public interface ServerInterface 
{
	public int insertUser(String Name, String Password);
	public void addUserClient(Client C);
	public int insertGroup(String Name);
	public int insertChannel(String Name);
	public Channel getChannel(int ChannelID);
	public Message getMessage(int MessageID);
	public User getUser(int UserID);
	public Group getGroup(int GroupID);
	public int getCh_ID(int ChannelID);
	public String getTopic(int ChannelID);
	public void setTopic(int ChannelID, String topic);
	public boolean SendMessage(int ChannelId,String Contents, int SenderID, int groupID);
	public int getGroupID(int GroupID);
	public String getGroupName(int GroupID);
	public void setAdmin(int GroupID,int UserID);
	public boolean EditGroupName(int GroupID,int UserID, String GroupName);
	public boolean BanUser(int GroupID,int UserID, int UserID2);
	public boolean KickUser(int GroupID,int UserID, int UserID2);	
	public boolean CanPost(int GroupID,int UserID);
	public int getMessageID(int MessageID);
	public String getContent(int MessageID);
	public int getUserID(int UserID);
	public LocalDateTime getTimestamp(int MessageID);
	public String getName(int UserID);
	public void setName(int UserID,String name);
	public String getPassword(int UserID);
	public void setPassword(int UserID, String password);
	public String getStatus(int UserID);
	public int getSenderID(int MessageID);
	public ArrayList<Integer> getChannels(String Username, int GroupID);
	public ArrayList<Integer> getGroups(String Username);
	public int findUserID(String Username);
	public ArrayList<Integer> getMessages(int cID);
	public boolean containsUserName(String UserName);
	public int findChannelbyName(String ChannelName);
	public int findGroupbyName(String ChannelName);
}
