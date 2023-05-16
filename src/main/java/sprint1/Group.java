package sprint1;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Group implements Serializable
{
	private static final long serialVersionUID = -8970905363480312990L;
	
	public ArrayList<Integer> UserIDs;
	public ArrayList<Integer> ChannelIDs;
	public HashMap<Integer, Boolean> Admin;
	public ArrayList<Integer> BannedUsers;
	public ArrayList<Integer> Kicked;
	public int GroupID;
	public String GroupName;
	
	public Group(){}

	public Group(String GroupName) {
		UserIDs = new ArrayList<Integer>();
		ChannelIDs = new ArrayList<Integer>();
		Admin = new HashMap<Integer, Boolean>();
		BannedUsers = new ArrayList<Integer>();
		Kicked = new ArrayList<Integer>();
		this.GroupName = GroupName;
		this.GroupID = 0;
	}

	public int getGroupID() {
		return GroupID;
	}
	
	public void setGroupID(int GroupID) {
		this.GroupID = GroupID;
	}

	public String getGroupName() {
		return GroupName;
	}

	public void setAdmin(int UserID){
		if(!BannedUsers.contains(UserID)||!Kicked.contains(UserID)|| UserIDs.contains(UserID))
		{
			Admin.put(UserID, true);
		}	
	}

	public boolean EditGroupName(int UserID, String GroupName) {
		if (Admin.containsKey(UserID) == true)
		{
			this.GroupName = GroupName; // changes the group name
			return true;
		}
		return false;
	}

	public boolean BanUser(int UserID, int UserID2) {
		if (Admin.containsKey(UserID) == true)
		{
			BannedUsers.add(UserID2); // adds UsersID to Banned Users Group
			return true;
		}
		return false;
	}

	public boolean KickUser(int UserID, int UserID2) {
		if (Admin.containsKey(UserID) == true)
		{
			Kicked.add(UserID2); // remove the user from the group
			return true;
		}
		return false;

	}

	public boolean CanPost(int UserID) {
		// in banned users can't post
		if (BannedUsers.contains(UserID) == true || Kicked.contains(UserID) == true)
		{
			return false;
		}
		return true;
	}

}
