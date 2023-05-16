package sprint2Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sprint1.Database;
import sprint2.Client;
import sprint2.Server;
import sprint2.ServerInterface;

class ConcordServerTest
{
	Database DB;
	Server mc;
	Registry registry;
	
	@BeforeEach
	void setUp() throws Exception 
	{
		DB = new Database();
		mc = new Server(DB);
		int a = (int) Math.ceil(Math.random()*(9999-1+1)+1);
		registry = LocateRegistry.createRegistry(a); //acts as local "host"
		registry.rebind("Concord", mc); // renames server (remote obejct) for lookup
		
		DB.insertUser("AJ", "Howell"); //0
		DB.insertUser("Christian", "Landaverde"); //1
		DB.insertUser("Croslin", "Johnson"); //2
		DB.insertUser("Glahens", "Paul"); //3
		DB.getUser(0).setPassword("Turtle");
	}
	
	@AfterEach
	void tearDown() throws Exception
	{
		registry.unbind("Concord");
	}

	@Test
	void test(){
		
		Client c1;
		Client c2;
		Client c3;
		Client c4;
		
		try {		
			ServerInterface proxy;
			//set the database
			proxy = (ServerInterface)registry.lookup("Concord"); //sets the proxy so that it limits access
			c1 = new Client(proxy);
			c1.addClienttoList(0);
			
			c2 = new Client(proxy);
			c2.addClienttoList(1);
			
			c3 = new Client(proxy);
			c3.addClienttoList(2);
			
			c4 = new Client(proxy);
			c4.addClienttoList(3);
			
			assertEquals(4,c1.insertUser("TOM", "123"));
			assertEquals(0,c1.insertGroup("CSC360"));
			assertEquals(0,c1.insertChannel("Homework"));
			
			c1.getGroup(0).ChannelIDs.add(0);
			c1.getUser(0).GroupIDS.add(0);
			c1.getUser(0).ChannelIDs.add(0);
			
			c1.getGroup(0).UserIDs.add(0);
			c1.getGroup(0).UserIDs.add(1);
			c1.getGroup(0).UserIDs.add(2);
			c1.getGroup(0).UserIDs.add(3);
			c1.getGroup(0).UserIDs.add(4);
			
			c1.setAdmin(0, 0);
			assertEquals(true,DB.groups.get(0).Admin.containsKey(0));
		
			assertEquals(true, c1.BanUser(0, 0, 1));
			assertEquals(true,DB.getGroup(0).BannedUsers.contains(1));
			
			assertEquals(false, c1.BanUser(0, 1, 0));
			assertEquals(false,DB.getGroup(0).BannedUsers.contains(0));
			
			assertEquals(true, c1.CanPost(0, 0));
			assertEquals(0,DB.getGroup(0).UserIDs.get(0));
			
			assertEquals(false, c1.CanPost(0, 1));
			assertEquals(1,DB.getGroup(0).BannedUsers.get(0));
			
			assertEquals(false,c1.EditGroupName(0, 1, "CSC362"));
			assertEquals("CSC360", DB.getGroup(0).getGroupName()); // didn't change orignal name
			
			assertEquals(true,c1.EditGroupName(0, 0, "CSC362"));
			assertEquals("CSC362", DB.getGroup(0).getGroupName());
			
			assertEquals(true,c1.KickUser(0, 0, 2));
			assertEquals(true,DB.getGroup(0).Kicked.contains(2));
			
			assertEquals(false,c1.KickUser(0, 1, 0));
			assertEquals(false,DB.getGroup(0).Kicked.contains(0));
			
			assertEquals(true, c1.SendMessage(0, "Hello", 0, 0)); //first message sent to & inserted into DB
			assertEquals(0,DB.getMessage(0).getMessageID());
			assertEquals("Hello", DB.getMessage(0).getContent());
			assertEquals(true,DB.getChannel(0).messageIDs.contains(0));
			
			assertEquals(false, c1.SendMessage(0, "Bye", 2, 0)); //first message sent to & inserted into DB
			assertEquals(false, DB.getChannel(0).messageIDs.contains(1));
			
			DB.getUser(0).setStatus("Offline");
			assertEquals("Offline",c1.getStatus(0));
			
			assertEquals(DB.getMessage(0).getContent(),c1.getContent(0));
			assertEquals(DB.getGroup(0).GroupID,c1.getGroupID(0));
			
			assertEquals(0,c1.getUserID(0));
			assertEquals(0,c1.getChannel(0).getCh_ID());
			
			assertEquals("Turtle",c1.getPassword(0));
			
			LocalDateTime time = c1.getTimestamp(0);
			assertEquals(time,c1.getTimestamp(0));
			
			assertEquals(0,c1.getMessageID(0));
			assertEquals(0,c1.getCh_ID(0));
			
			c1.ChangeTopic(0, "Projects");
			assertEquals("Projects", c1.getTopic(0));
			
			c1.ChangeName(0, "Anazjai");
			assertEquals("Anazjai", c1.getName(0));
			
			c1.ChangePassword(0, "Joselyn");
			assertEquals("Joselyn", c1.getPassword(0));
			
			assertEquals(DB.getMessage(0), c1.getMessage(0));
			assertEquals(DB.getGroup(0).getGroupName(), c1.getGroupName(0));
			assertEquals(DB.getGroup(0), c1.getGroup(0));
			assertEquals(DB.getUser(0), c1.getUser(0));
			assertEquals(DB.getMessage(0).getSenderID(), c1.getSenderID(0));
			
			c1.setAdmin(0, 3);
			assertEquals(true,DB.getGroup(0).Admin.containsKey(3));
			assertEquals("User: Glahens has been set to Admin @Anazjai",c1.recentNotification);
			
			assertEquals(DB.getGroup(0).ChannelIDs.get(0), c1.getChannels("Anazjai", 0).get(0));
			assertEquals(DB.getGroup(0).getGroupID(), c1.getGroups("Anazjai").get(0));
			assertEquals(0,c1.findUserID("Anazjai"));
			assertEquals(0,c1.findChannelbyName("Projects"));
			assertEquals(true,c1.containsUserName("Glahens"));
			assertEquals(DB.getChannel(0).messageIDs.get(0),c1.getMessages(0).get(0));
			assertEquals(0,c1.findGroupbyName("CSC362"));
			Client c5 = new Client(proxy);
			c5.addClienttoList(46);

			Client c6 = new Client(proxy);
			c6.addClienttoList(4);
			
			DB.storeToDisk();
			Database newDB = Database.loadFromDisk();
			
			//Either create new Equals method or check each test individually
			assertThat(DB.equals(newDB));
			assertEquals(DB.getGroup(0).GroupName,newDB.getGroup(0).GroupName);
			assertEquals(DB.getGroup(0).UserIDs.contains(0),newDB.getGroup(0).UserIDs.contains(0));
			assertEquals(DB.getGroup(0).UserIDs.contains(1),newDB.getGroup(0).UserIDs.contains(1));
			assertEquals(DB.getGroup(0).UserIDs.contains(2),newDB.getGroup(0).UserIDs.contains(2));
			assertEquals(DB.getGroup(0).UserIDs.contains(3),newDB.getGroup(0).UserIDs.contains(3));
			assertEquals(DB.getGroup(0).BannedUsers.contains(1), newDB.getGroup(0).BannedUsers.contains(1));
			assertEquals(DB.getGroup(0).Kicked.contains(2), newDB.getGroup(0).Kicked.contains(2));
			assertEquals(DB.getGroup(0).Admin.get(3), newDB.getGroup(0).Admin.get(3));
			
			assertEquals(DB.getChannel(0).Topic, newDB.getChannel(0).Topic);
			assertEquals(DB.getMessage(0).getContent(), newDB.getMessage(0).getContent());
			assertEquals(DB.getUser(0).Name,DB.getUser(0).Name);
			assertEquals(DB.getUser(1).Name,DB.getUser(1).Name);
			assertEquals(DB.getUser(2).Name,DB.getUser(2).Name);
			assertEquals(DB.getUser(3).Name,DB.getUser(3).Name);
			assertEquals(false,c1.containsUserName("Jake909"));
			
			
		}
		catch(RemoteException | NotBoundException e)
		{
			e.printStackTrace();
		} 
	}
	
	
}
