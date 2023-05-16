package sprint3.models;
import java.rmi.RemoteException;
import java.util.ArrayList;
import sprint1.Channel;
import sprint1.Group;
import sprint1.Message;
import sprint1.User;
import sprint2.Client;

public class DataModel 
{
	private Client c;
	public String UserName;
	
	public DataModel(Client c) throws RemoteException
	{
		this.c=c;
	}
	
	public int addChannel(String topic, int groupId, String UserName)
	{
		Group g1 = c.getGroup(groupId);
		int chID = c.insertChannel(topic); 
		int userID=findUser(UserName);
		c.getUser(userID).ChannelIDs.add(chID);
		g1.ChannelIDs.add(chID);
		
		return chID;
	}
	
	public int addGroup(String Name, int UserID)
	{
		int groupID=c.insertGroup(Name);
		c.getGroup(groupID).UserIDs.add(UserID);
		c.getUser(UserID).GroupIDS.add(groupID);
		return groupID;
	}
	
	public int addUser(String Name, String password)
	{
		int userID=c.insertUser(Name, password);
		return userID;
	}
	
	public boolean SendMessage(int ChID,String message, int SenderID, int GroupID)
	{
		return c.SendMessage(ChID, message, SenderID, GroupID);
	}
	
	public Channel getChannel(int ChID)
	{
		return c.getChannel(ChID);
	}
	
	public Group getGroup(int groupID)
	{
		return c.getGroup(groupID);
	}
	
	public User getUser(int userID)
	{
		return c.getUser(userID);
	}
	
	public Message getMessage(int mID)
	{
		return c.getMessage(mID);
	}
	
	public int findUser(String Username)
	{
		return c.findUserID(Username);
	}
	
	public ArrayList<Integer> getChannels(String Username, int GroupID)
	{
		return c.getChannels(Username, GroupID);
	}
	
	public ArrayList<Integer> getGroups(String Username)
	{
		return c.getGroups(Username);
	}
	
	public int findChannelbyName(String ChannelName)
	{
		return c.findChannelbyName(ChannelName);
	}
	
	public int findGroupbyName(String groupName)
	{
		return c.findGroupbyName(groupName);
	}
}
