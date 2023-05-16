package sprint2;
import java.time.LocalDateTime;
import java.util.ArrayList;
import sprint1.*;


public class Client implements Comparable<Client>
{
	private ServerInterface mc;
	private int ClientID;
	public String recentNotification; 
	
	public Client(ServerInterface mc)
	{
		this.mc=mc;
	}
	
	public int insertUser(String Name, String Password)
	{
		return mc.insertUser(Name, Password);
	}

	public int insertGroup(String Name)
	{
		return mc.insertGroup(Name);
	}

	public int insertChannel(String Name)
	{
		return mc.insertChannel(Name);
	}

	public Channel getChannel(int ChannelID)
	{
		return mc.getChannel(ChannelID);
	}
	
	public Message getMessage(int MessageID)
	{
		return mc.getMessage(MessageID);
	}
	
	public User getUser(int UserID)
	{
		return mc.getUser(UserID);
	}
	public Group getGroup(int GroupID)
	{
		return mc.getGroup(GroupID);
	}
	public int getCh_ID(int ChannelID)
	{
		return mc.getCh_ID(ChannelID);
	}

	public String getTopic(int ChannelID)
	{
		return mc.getTopic(ChannelID);
	}
	public void ChangeTopic(int ChannelID, String topic)
	{
		mc.setTopic(ChannelID, topic);
	}
	
	public boolean SendMessage(int ChannelId,String Contents, int SenderID, int groupID)
	{
		return mc.SendMessage(ChannelId, Contents, SenderID, groupID);
	}
	
	public int getGroupID(int GroupID)
	{
		return mc.getGroupID(GroupID);
	}

	public String getGroupName(int GroupID)
	{
		return mc.getGroupName(GroupID);
	}

	public void setAdmin(int GroupID,int UserID)
	{
		mc.setAdmin(GroupID, UserID);
	}
	
	public boolean EditGroupName(int GroupID,int UserID, String GroupName)
	{
		return mc.EditGroupName(GroupID, UserID, GroupName);
	}
	
	public boolean BanUser(int GroupID,int UserID, int UserID2)
	{
		return mc.BanUser(GroupID, UserID, UserID2);
	}
	
	public boolean KickUser(int GroupID,int UserID, int UserID2)
	{
		return mc.KickUser(GroupID, UserID, UserID2);
	}
	
	public boolean CanPost(int GroupID,int UserID)
	{
		return mc.CanPost(GroupID, UserID);
	}
	
	public int getMessageID(int MessageID)
	{
		return mc.getMessageID(MessageID);
	}
	
	public String getContent(int MessageID)
	{
		return mc.getContent(MessageID);
	}

	public int getUserID(int UserID)
	{
		return mc.getUserID(UserID);
	}
	
	public LocalDateTime getTimestamp(int MessageID)
	{
		return mc.getTimestamp(MessageID);
	}
	
	public String getName(int UserID)
	{
		return mc.getName(UserID);
	}
	
	public void ChangeName(int UserID,String name)
	{
		mc.setName(UserID, name);
	}
	
	public String getPassword(int UserID)
	{
		return mc.getPassword(UserID);
	}
	
	public void ChangePassword(int UserID, String password)
	{
		mc.setPassword(UserID, password);
	}
	
	public String getStatus(int UserID)
	{
		return mc.getStatus(UserID);
	}
	
	public int getSenderID(int MessageID)
	{
		return mc.getSenderID(MessageID);
	}
	
	public ArrayList<Integer> getChannels(String Username, int GroupID)
	{
		return mc.getChannels(Username, GroupID);
	}
	
	public ArrayList<Integer> getGroups(String Username)
	{
		return mc.getGroups(Username);
	}
	
	public int findUserID(String Username)
	{
		return mc.findUserID(Username);
	}
	
	public ArrayList<Integer> getMessages(int cID)
	{	
		return mc.getMessages(cID);
	}
	
	public boolean containsUserName(String UserName)
	{
		return mc.containsUserName(UserName);
	}
	
	public int findChannelbyName(String ChannelName)
	{
		return mc.findChannelbyName(ChannelName);
	}
	
	public int findGroupbyName(String groupName)
	{
		return mc.findGroupbyName(groupName);
	}
	
	public void setClientID(int UserID)
	{
		this.ClientID=UserID;
	}
	
	public void addClienttoList(int UserID)
	{
		this.ClientID=UserID;
		mc.addUserClient(this);
	}
	
	public int getClientID()
	{
		return ClientID;
	}
	
	protected void Notify(String alert)
	{
		this.recentNotification=alert;
		System.out.println(alert);
	}
	
	@Override
	public int compareTo(Client o)
	{
	  if(ClientID>o.getClientID())
		{
			return 1;
		}
		
	  else
		{
			return -1;
		}
	}
	
}
