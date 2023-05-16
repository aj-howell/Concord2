package sprint1Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint1.Channel;
import sprint1.Database;
import sprint1.Group;

class SprintTest1
{
	Database DB;
	
	@BeforeEach
	void setUp() throws Exception 
	{
		DB = new Database();
		DB.insertUser("AJ", "Howell"); //0
		DB.insertUser("Christian", "Landaverde"); //1
		DB.insertUser("Croslin", "Johnson"); //2
		DB.insertGroup("CSC360");
		DB.insertChannel("Homework");
		DB.groups.get(0).setAdmin(0);
	}
	
	@Test
	void test(){
		Group g1 = DB.getGroup(0);
		ArrayList<Integer> users1 = g1.UserIDs;
		users1.add(DB.getUser(0).getUserID());
		
		Channel c1 = DB.getChannel(0);
		g1.ChannelIDs.add(c1.Ch_ID); // adds channel id to group
		//System.out.print(g1.Admin.get(1));
		
		assertEquals(0, users1.get(0));
		assertEquals("AJ", DB.getUser(0).getName());
		assertEquals(0,g1.GroupID);
		assertEquals("CSC360",g1.GroupName);
		assertEquals("Homework",c1.getTopic());
		assertEquals(true,c1.SendMessage("Hello", 0, 0, DB));
		
		assertEquals(0,g1.ChannelIDs.get(0)); //a channel is inside a specific group
		
		assertEquals(false,DB.getGroup(0).EditGroupName(1, "CSC362"));
		assertEquals("CSC360",DB.getGroup(0).GroupName); // group name should remain the same
		
		assertEquals(true,DB.getGroup(0).EditGroupName(0, "CSC362"));
		assertEquals("CSC362",DB.getGroup(0).GroupName);
		
		assertEquals(true,DB.getGroup(0).KickUser(0, 1));
		assertEquals(true,DB.getGroup(0).Kicked.contains(1));
		
		assertEquals(true, DB.getGroup(0).BanUser(0, 2));
		assertEquals(true,DB.getGroup(0).BannedUsers.contains(2));
		
		assertEquals(false, DB.getGroup(0).CanPost(1));
		assertEquals(true, DB.getGroup(0).CanPost(0));
	
		assertEquals(false,DB.getGroup(0).BanUser(1, 0));
		assertEquals(false,DB.getGroup(0).BannedUsers.contains(0));
		
		assertEquals(false,DB.getGroup(0).KickUser(1, 0));
		assertEquals(false,DB.getGroup(0).Kicked.contains(0));
		
		DB.getUser(0).setPassword("Turtle");
		assertEquals("Turtle", DB.getUser(0).getPassword());
	}

}
