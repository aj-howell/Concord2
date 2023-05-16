package sprint3Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sprint1.Database;
import sprint2.Client;
import sprint2.Server;
import sprint2.ServerInterface;
import sprint3.main.Main;
import sprint3.models.DataModel;
import sprint3.models.NavigationModel;
import sprint3.views.MainController;

@ExtendWith(ApplicationExtension.class)
class LoadTest {

	private MainController m;
	private NavigationModel Nav;
	private DataModel D;
	private Server mc;
	Registry registry;
	AnchorPane view;
	
	@Start
	public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/Login_View.fxml"));
		view = loader.load();
		Scene s = new Scene(view);
		stage.setScene(s);
		
		m = loader.getController();
		Database DB = new Database();
		mc = new Server(DB);
		int a = (int) Math.ceil(Math.random()*(9999-1+1)+1);
		registry = LocateRegistry.createRegistry(a); //acts as local "host"
		registry.rebind("Concord", mc); // renames server (remote obejct) for lookup
		
		ServerInterface proxy;
		proxy = (ServerInterface)registry.lookup("Concord"); //sets the proxy so that it limits access
		Client c = new Client(proxy);
		
		D = new DataModel(c);
		
		int UserID = D.addUser("aj123", "howell22");
		D.UserName = "aj123";
		
		int id = D.addGroup("Soccer", D.findUser(D.UserName));
		
		D.addChannel("Messi", id, D.UserName);
		
		D.SendMessage(D.findChannelbyName("Messi"), "Messi is the best!", UserID, id);
		
		D.addChannel("Ronaldo", id, D.UserName);
		
		int id2 = D.addGroup("Football", D.findUser(D.UserName));
		D.addChannel("Tom Brady", id2, D.UserName);
		
		D.addChannel("Odell Beckham", id2, D.UserName);
	
		int UserId2 = D.addUser("Dan123", "22");
		D.getGroup(id).BannedUsers.add(UserId2);
		
		D.addUser("David123", "55");
		
		int UserId4 = D.addUser("Fred22", "28");
		D.getGroup(id).Kicked.add(UserId4);
		
		D.addUser("Tony123", "55");
		D.addUser("Hannah123", "55");
		
		Nav= new NavigationModel(c);
	
		m.setModel(Nav,D);
		
		stage.show();
	}

	@Test
	public void TestLoadingData(FxRobot robot )
	{
		/* a test for data that needs to load in correctly.*/
		Login(robot,"aj123","howell22");
		assertEquals(mc.getChannel(0).Topic,"Messi");
		
		robot.clickOn("GroupName");
		
		MenuButton MB1=(MenuButton)robot.lookup("GroupName")
		.queryAs(MenuButton.class);
		
		assertEquals(true,MB1.getItems().get(0).getText().equals("Soccer"));
		
		robot.clickOn("#0");
		robot.clickOn("Messi");

		robot.clickOn("Ronaldo");
		robot.clickOn("Messi");

		VBox v=(VBox)robot.lookup("#messages")
		.queryAs(VBox.class);
		assertEquals(true, v.getChildren().get(0).toString().contains("Messi is the best!"));
		
		robot.clickOn("#usersMenuButton");
		robot.clickOn("Banned");
		robot.clickOn("#text");
		robot.write("Hannah123");
		robot.clickOn("#UserButton");
		
		javafx.scene.control.ListView<Object> lv1=robot.lookup("#UsersList")
		.queryListView();
		
		assertEquals(true,lv1.getItems().get(0).toString().contains("Dan123"));
		
		robot.clickOn("#usersMenuButton");
		robot.clickOn("Kicked");
		robot.clickOn("#text");
		robot.write("Tony123");
		robot.clickOn("#UserButton");
		
		assertEquals(true,lv1.getItems().get(0).toString().contains("Fred22"));
	
		robot.clickOn("#usersMenuButton");
		robot.clickOn("Users");
		
		robot.clickOn("#text");
		robot.write("David123");
		
		robot.clickOn("#UserButton");
		assertThat(D.getGroup(0).UserIDs.get(D.getUser(0).getUserID())).isNotNull();
	
	}
	
	private void Login(FxRobot robot, String name, String pass)
	{	
		robot.clickOn("#UserName");
		robot.write(name);
		
		robot.clickOn("#password");
		robot.write(pass);	
		
		robot.clickOn("#Login");
		
	}
	
	@AfterEach
	void tearDown() throws AccessException, RemoteException, NotBoundException
	{
		registry.unbind("Concord");
		 Platform.setImplicitExit(true);
	}
}
