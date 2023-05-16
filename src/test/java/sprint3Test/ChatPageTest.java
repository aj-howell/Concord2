package sprint3Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sprint1.Database;
import sprint2.Client;
import sprint2.Server;
import sprint2.ServerInterface;
import sprint3.models.DataModel;
import sprint3.models.NavigationModel;
import sprint3.views.MainController;

@ExtendWith(ApplicationExtension.class)
public class ChatPageTest
{
	private NavigationModel Nav;
	private DataModel D;
	Registry registry;
	MainController m;
	
	@Start
	public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NavigationModel.class.getResource("../views/ChatPage.fxml"));
		AnchorPane view = loader.load();
		Scene s = new Scene(view);
		stage.setScene(s);
		
		m = loader.getController();
		Database DB = new Database();
		Server mc = new Server(DB);
		int a = (int) Math.ceil(Math.random()*(9999-1+1)+1); //random numbers for ports 
		registry = LocateRegistry.createRegistry(a); //acts as local "host"
		registry.rebind("Concord", mc); // renames server (remote obejct) for lookup
		
		ServerInterface proxy;
		proxy = (ServerInterface)registry.lookup("Concord"); //sets the proxy so that it limits access
		Client c = new Client(proxy);
		
		D = new DataModel(c);
		D.addUser("aj123", "123");
		D.UserName="aj123";

		Nav=new NavigationModel(c);
	
		m.setModel(Nav,D);
		
		stage.show();
	}
	
	@Test
	void TestChat(FxRobot robot)
	{
		createGroup(robot, "CSC360");
		createChannel(robot, "Homework");
		sendMessage(robot,"Hello Cat");
		
		Label m1=(Label) robot.lookup("#messages")
		.queryAs(VBox.class)
		.getChildren()
		.get(0);
	
		assertEquals("This message was deleted",m1.getText());
	}
	
	@Test
	void TestChatFail(FxRobot robot)
	{
		createGroup(robot, "CSC360");
		createChannel(robot, "Homework");
		sendMessage(robot,"");
		
		assertEquals(true,m.isNull);
	}
	
	@Test
	void TestSwitchChannel(FxRobot robot)
	{
		createGroup(robot, "CSC360");
		createChannel(robot, "Homework");
		createChannel(robot,"Test");
		sendMessage(robot,"Hello");
		
		Label m1=(Label) robot.lookup("#messages")
				.queryAs(VBox.class)
				.getChildren()
				.get(0);
		
		assertEquals("Hello",m1.getText());
		
		robot.clickOn("#Homework");	
		
		Label c1 = robot.lookup("Channel: Homework")
				.queryAs(Label.class);
		assertEquals(true,c1.getText().contains("Homework"));
	}
	
	@Test
	void TestSwitchGroup(FxRobot robot)
	{
		createGroup(robot, "CSC360");
		createGroup(robot, "CSC362");
		createChannel(robot,"Test");
		sendMessage(robot,"Hello");
	
		robot.clickOn("CSC362");
		robot.clickOn("CSC360");
		
		
		VBox m1=(VBox)robot.lookup("#messages")
				.queryAs(VBox.class);
		
		assertEquals(0, m1.getChildren().size());
		
		MenuButton  mB1=(MenuButton)robot.lookup("CSC360")
		.queryAs(MenuButton.class);
	
		assertEquals("CSC360",mB1.getText());
	}
	
	@Test
	void TestGroup(FxRobot robot)
	{
		createGroup(robot, "CSC360");
		MenuButton  mB1=(MenuButton)robot.lookup("CSC360")
		.queryAs(MenuButton.class);
	
		assertEquals("CSC360",mB1.getText());
	}
	
	@Test
	void TestLogout(FxRobot robot)
	{
		robot.clickOn("#logout");
		Assertions.assertThat(Nav.online).isFalse();
	}
	
	private void createChannel(FxRobot robot, String cName)
	{
		robot.clickOn("#text");
		robot.write(cName);	
		robot.clickOn("#createChannel");
	}
	
	private void createGroup(FxRobot robot, String gName)
	{
		robot.clickOn("#text");
		robot.write(gName);	
		robot.clickOn("#createGroup");
	}
	
	private void sendMessage(FxRobot robot, String text)
	{
		robot.clickOn("#text");
		robot.write(text);
		robot.clickOn("#send");
	}
	
	@AfterEach
	void tearDown() throws AccessException, RemoteException, NotBoundException
	{
		registry.unbind("Concord");
		 Platform.setImplicitExit(true);
	}
	

}
