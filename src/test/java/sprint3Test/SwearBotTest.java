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
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
public class SwearBotTest
{
	private NavigationModel Nav;
	private DataModel D;
	Registry registry;
	
	@Start
	public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NavigationModel.class.getResource("../views/ChatPage.fxml"));
		AnchorPane view = loader.load();
		Scene s = new Scene(view);
		stage.setScene(s);
		
		MainController m = loader.getController();
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
	void TestChat(FxRobot robot) throws InterruptedException
	{
		createGroup(robot, "CSC360");
		Thread.sleep(1_000L);
		createChannel(robot, "Homework");
		Thread.sleep(1_000L);
		sendMessage(robot,"Hello Cat");
		Thread.sleep(1_000L);
		
		VBox m1=(VBox)robot.lookup("#messages")
				.queryAs(VBox.class);
		assertEquals(true, m1.getChildren().get(0).toString().contains("This message was deleted"));
		
		sendMessage(robot,"Hello Dr. B");
		assertEquals(true, m1.getChildren().get(1).toString().contains("Hello Dr. B"));
		Thread.sleep(1_000L);
		
		sendMessage(robot,"Hello Dr. Chocolate");
		assertEquals(true, m1.getChildren().get(2).toString().contains("This message was deleted"));
		Thread.sleep(1_000L);
		
		sendMessage(robot,"Hello Dr. Coffee");
		assertEquals(true, m1.getChildren().get(3).toString().contains("This message was deleted"));
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
