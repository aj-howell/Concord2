package sprint1;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable
{
	private static final long serialVersionUID = -4212896729215512012L;
	
	public String Name;
	public String Password;
	public String Status;
	int UserID;
	public ArrayList<Integer> ChannelIDs;
	public ArrayList<Integer> GroupIDS;
	
	public User(){}

	public User(String Name, String Password) {
		this.Name = Name;
		this.Password = Password;
		this.Status = null;
		this.UserID = 0;
		this.ChannelIDs = new ArrayList<Integer>();
		this.GroupIDS=new ArrayList<Integer>();

	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		this.Name = name;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		this.Password = password;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		this.Status = status;
	}

	public int getUserID() {
		return UserID;
	}

	public void setUserID(int userID) {
		this.UserID = userID;
	}
}
