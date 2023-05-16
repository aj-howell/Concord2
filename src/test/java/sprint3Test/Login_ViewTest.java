package sprint3Test;
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
import javafx.stage.Stage;
import sprint1.Database;
import sprint2.Client;
import sprint2.Server;
import sprint2.ServerInterface;
import sprint3.main.Main;
import sprint3.models.DataModel;
import sprint3.models.NavigationModel;
import sprint3.views.MainController;

import static org.mockito.Mockito.*;
/*when using Mockito use 4.8.1  as its compatible with ByteBuddy and previous versions aren't*/

@ExtendWith(ApplicationExtension.class)
public class Login_ViewTest
{
	private MainController m;
	private NavigationModel Nav;
	private DataModel D;
	Registry registry;
	
	@Start
	public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/Login_View.fxml"));
		AnchorPane view = loader.load();
		Scene s = new Scene(view);
		stage.setScene(s);
		
		m = loader.getController();
		Database DB = new Database();
		Server mc = new Server(DB);
		int a = (int) Math.ceil(Math.random()*(9999-1+1)+1);
		registry = LocateRegistry.createRegistry(a); //acts as local "host"
		registry.rebind("Concord", mc); // renames server (remote obejct) for lookup
		
		ServerInterface proxy;
		proxy = (ServerInterface)registry.lookup("Concord"); //sets the proxy so that it limits access
		Client c = new Client(proxy);
		
		D = new DataModel(c);
		D.addUser("aj123", "howell22");
		D.UserName="aj123";
	
		Nav=mock(NavigationModel.class);
	
		m.setModel(Nav,D);
		
		stage.show();
	}
	
	@Test
	public void TestLogin(FxRobot robot )
	{
		Login(robot,"aj123","howell22");
		verify(Nav).showMainPage("aj123", "howell22");
	}

	@Test
	public void TestRegister(FxRobot robot)
	{
		Register(robot, "Thomas12","spiderman123");
		verify(Nav).showNewGroup("Thomas12", "spiderman123");
	}
	
	private void Login(FxRobot robot, String name, String pass)
	{
		robot.clickOn("#UserName");
		robot.write(name);
		
		robot.clickOn("#password");
		robot.write(pass);
		
		robot.clickOn("#Login");
		
		verify(Nav).showMainPage(name, pass);
	}
	
	private void Register(FxRobot robot, String name, String pass)
	{
		robot.clickOn("#UserName");
		robot.write(name);
		
		robot.clickOn("#password");
		robot.write(pass);
		
		robot.clickOn("#register");
		
		verify(Nav).showNewGroup(name, pass);
	}

	@AfterEach
	void tearDown() throws AccessException, RemoteException, NotBoundException
	{
		registry.unbind("Concord");
		 Platform.setImplicitExit(true);
	}
}
