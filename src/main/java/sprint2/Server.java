package sprint2;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import sprint1.*;

public class Server extends UnicastRemoteObject implements ServerInterface {

	private static final long serialVersionUID = 4435688779296605850L;
	Database DB;
	public ArrayList<Client> clients;
	
	public Server(Database DB) throws RemoteException {
		this.DB = DB;
		clients = new ArrayList<Client>();
	}

	@Override
	public Channel getChannel(int ChannelID) {
		// TODO Auto-generated method stub
		return DB.getChannel(ChannelID);
	}

	@Override
	public Message getMessage(int MessageID) {
		// TODO Auto-generated method stub
		return DB.getMessage(MessageID);
	}

	@Override
	public User getUser(int UserID) {
		return DB.getUser(UserID);
	}

	@Override
	public Group getGroup(int GroupID){
		return DB.getGroup(GroupID);
	}

	@Override
	public int getCh_ID(int ChannelID){
		return DB.getChannel(ChannelID).getCh_ID();
	}

	@Override
	public String getTopic(int ChannelID) {
		return DB.getChannel(ChannelID).getTopic();
	}

	@Override
	public void setTopic(int ChannelID, String topic){
		String OldName=DB.getChannel(ChannelID).Topic;
		DB.getChannel(ChannelID).setTopic(topic);
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify("Channel: "+OldName+" has changed to: "+DB.getChannel(ChannelID).Topic+" @"+DB.getUser(i).Name);
		}	
		DB.storeToDisk();
	}
	
	@Override
	public boolean SendMessage(int ChannelId, String Contents, int SenderID, int groupID)
	{
		boolean sent = DB.channels.get(ChannelId).SendMessage(Contents, SenderID, groupID, DB);
		DB.storeToDisk();
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify(DB.getUser(SenderID).Name+" has sent a message"+" @"+DB.getUser(i).Name);
		}	
		return sent;
	}

	@Override
	public int getGroupID(int GroupID) {
		return DB.getGroup(GroupID).getGroupID();
	}

	@Override
	public String getGroupName(int GroupID) {
		return DB.getGroup(GroupID).getGroupName();
	}

	@Override
	public void setAdmin(int GroupID, int UserID) {
		DB.getGroup(GroupID).setAdmin(UserID);
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify("User: "+DB.getUser(UserID).Name+" has been set to Admin"+" @"+DB.getUser(i).Name);
		}	
		DB.storeToDisk();
	}

	@Override
	public boolean EditGroupName(int GroupID, int UserID, String GroupName) {
		String OldName = DB.getGroup(GroupID).GroupName;
		
		boolean edited = DB.getGroup(GroupID).EditGroupName(UserID, GroupName);
		DB.storeToDisk();
		if(edited==true)
		{
			for(int i=0; i<=clients.size()-1; i++)
			{
				clients.get(i).Notify("Group: "+OldName+" has changed to: "+DB.getGroup(GroupID).GroupName+" @"+DB.getUser(i).Name);
			}		
		}
		return edited;
	}

	@Override
	public boolean BanUser(int GroupID, int UserID, int UserID2) {

		boolean banned=DB.getGroup(GroupID).BanUser(UserID, UserID2);
		DB.storeToDisk();
		if(banned==true)
		{
			for(int i=0; i<=clients.size()-1; i++)
			{
				clients.get(i).Notify("User: "+DB.getUser(UserID2).Name+" has been banned from "+DB.getGroup(GroupID).GroupName+" @"+DB.getUser(i).Name);
			}
		}
		
		return banned;
	}

	@Override
	public boolean KickUser(int GroupID, int UserID, int UserID2)
	{
		boolean kicked = DB.getGroup(GroupID).KickUser(UserID, UserID2);
		DB.storeToDisk();
		if(kicked==true)
		{
			for(int i=0; i<=clients.size()-1; i++)
			{
				clients.get(i).Notify("User: "+DB.getUser(UserID2).Name+" has been kicked from "+DB.getGroup(GroupID).GroupName+" @"+DB.getUser(i).Name);
			}
		}
		return kicked;
	}

	@Override
	public boolean CanPost(int GroupID, int UserID) {
		return DB.getGroup(GroupID).CanPost(UserID);
	}

	@Override
	public int getMessageID(int MessageID) {

		return DB.getMessage(MessageID).getMessageID();
	}

	@Override
	public String getContent(int MessageID) {
		// TODO Auto-generated method stub
		return DB.getMessage(MessageID).getContent();
	}

	@Override
	public int getUserID(int UserID) {
		// TODO Auto-generated method stub
		return DB.getUser(UserID).getUserID();
	}

	@Override
	public LocalDateTime getTimestamp(int MessageID) {
		return DB.getMessage(MessageID).getTimestamp();
	}

	@Override
	public String getName(int UserID) {
		// TODO Auto-generated method stub
		return DB.getUser(UserID).getName();
	}

	@Override
	public void setName(int userID, String name){
		String OldName = DB.getUser(userID).Name;
		DB.getUser(userID).setName(name);
		DB.storeToDisk();
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify("User: "+OldName+" has changed their name to: "+DB.getUser(userID).Name+" @"+DB.getUser(i).Name);
		}
	}

	@Override
	public String getPassword(int UserID) {
		return DB.getUser(UserID).getPassword();
	}

	@Override
	public void setPassword(int UserID, String password) {
		DB.getUser(UserID).setPassword(password);
		DB.storeToDisk();
		//Don't want to notify users when someone's password has changed
	}

	@Override
	public String getStatus(int UserID) {
		return DB.getUser(UserID).getStatus();
	}

	@Override
	public int getSenderID(int MessageID) {

		return DB.getMessage(MessageID).getSenderID();
	}

	@Override
	public int insertUser(String Name, String Password) {
		// TODO Auto-generated method stub
		int userId = DB.insertUser(Name, Password);
		DB.storeToDisk();
		
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify("Welcome: "+Name+" @"+DB.getUser(i).Name);
		}
		return userId;
	}

	@Override
	public int insertGroup(String Name){
		int groupId = DB.insertGroup(Name);
	
		DB.storeToDisk();
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify("The group "+Name+" has been added"+" @"+DB.getUser(i).Name);
		}

		return groupId;
	}

	@Override
	public int insertChannel(String Name) {
		int chID=DB.insertChannel(Name);
		DB.storeToDisk();
		
		for(int i=0; i<=clients.size()-1; i++)
		{
			clients.get(i).Notify("The channel "+Name+" has been added"+" @"+DB.getUser(i).Name);
		}
		
		return chID;
	}

	@Override
	public ArrayList<Integer> getChannels(String Username, int GroupID) {
		findUserID(Username);
		return DB.getGroup(GroupID).ChannelIDs;
	}

	@Override
	public ArrayList<Integer> getGroups(String Username) {
		int userID = findUserID(Username);
		return DB.getUser(userID).GroupIDS;
	}

	@Override
	public ArrayList<Integer> getMessages(int cID){
		return DB.getChannel(cID).messageIDs;
	}

	@Override
	public int findUserID(String Username) {
		return DB.members.stream().filter(m -> m.getName().equals(Username)).map(m -> m.getUserID()).findFirst()
				.orElseThrow();
	}

	@Override
	public boolean containsUserName(String UserName) {
		int ans = (int) DB.members.stream().filter(m -> m.getName().equals(UserName)).count();
		return ans == 0 ? false : true;
	}

	@Override
	public int findChannelbyName(String ChannelName)
	{
	  return DB.channels
		.stream()
		.filter(ch->ch.Topic.equals(ChannelName))
		.map(ch->ch.Ch_ID)
		.findFirst()
		.orElseThrow();
	}

	@Override
	public int findGroupbyName(String groupName)
	{
		return  DB.groups
				.stream()
				.filter(g->g.GroupName.equals(groupName))
				.map(g->g.GroupID)
				.findFirst()
				.orElseThrow();
	}

	@Override
	public void addUserClient(Client C)
	{
		clients.add(C);
		Collections.sort(clients);
	}

}
