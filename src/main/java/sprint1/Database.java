package sprint1;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Database implements Serializable
{
	private static final long serialVersionUID = -1176958778798520549L;
	
	public ArrayList<User> members;
	public ArrayList<Group> groups;
	public ArrayList<Channel> channels;
	public ArrayList<Message> messages;

	public Database() {
		members = new ArrayList<User>();
		groups = new ArrayList<Group>();
		channels = new ArrayList<Channel>();
		messages = new ArrayList<Message>();
	}

	// Inserts
	public int insertUser(String Name, String Password) {
		User newUser = new User(Name, Password);
		members.add(newUser);
		newUser.setUserID(members.size() - 1); // sets the ID incrementally
		return members.size() - 1;
	}

	public int insertGroup(String Name) {
		Group newGroup = new Group(Name);
		groups.add(newGroup);
		newGroup.setGroupID(groups.size() - 1); // sets the ID incrementally
		return groups.size() - 1;
	}

	public int insertChannel(String Name) {
		Channel newChannel = new Channel(Name);
		channels.add(newChannel);
		newChannel.setCh_ID(channels.size() - 1); // sets the ID incrementally
		return channels.size() - 1;
	}

	public int insertMessage(Message m1) // inserts message into database and the channel
	{
		messages.add(m1);
		m1.setMessageID(messages.size() - 1);
		return messages.size() - 1;

	}
	
	// Getters
	public Channel getChannel(int ChannelID) {
		return channels.get(ChannelID);
	}

	public Message getMessage(int MessageID) {
		return messages.get(MessageID);
	}

	public User getUser(int UserID) {
		return members.get(UserID);
	}

	public Group getGroup(int GroupID) {
		return groups.get(GroupID);
	}

	public void storeToDisk()
	{
		XMLEncoder encoder=null;
		try{
		encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Database.xml")));
		}
		
		catch(FileNotFoundException fileNotFound)
		{
			System.out.println("ERROR: While Creating or Opening the File Database.xml");
		}
		encoder.writeObject(this);
		encoder.close();
	}
	
	public static Database loadFromDisk()
	{
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream("Database.xml")));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File Database.xml not found");
		}
		Database DB=(Database)decoder.readObject();
		System.out.println(DB);
		return DB;
	}
}
