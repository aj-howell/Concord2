package sprint3Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
public class New_GroupTest
{
	private NavigationModel Nav;
	private DataModel D;
	Registry registry;
	
	@Start
	public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NavigationModel.class.getResource("../views/New_Group_View.fxml"));
		
		VBox view = loader.load();
		Scene s = new Scene(view);
		stage.setScene(s);
			
		MainController m = loader.getController();
		Database DB = new Database();
		Server mc = new Server(DB);
		int a = (int) Math.ceil(Math.random()*(9999-1+1)+1);
		registry = LocateRegistry.createRegistry(a); //acts as local "host"
		registry.rebind("Concord", mc); // renames server (remote obejct) for lookup
		
		ServerInterface proxy;
		proxy = (ServerInterface)registry.lookup("Concord"); //sets the proxy so that it limits access
		Client c = new Client(proxy);
		
		D = new DataModel(c);
		D.addUser("aj12", "howell22");
		D.UserName="aj12";

		Nav=mock(NavigationModel.class);
		
		m.setModel(Nav,D);
			
		stage.show();	
		}
	
	@Test
	public void TestMainPage(FxRobot robot)
	{	
		robot.clickOn("#Main");
		verify(Nav).showMainPage("aj12", "howell22");
	}
	
	@AfterEach
	void tearDown() throws AccessException, RemoteException, NotBoundException
	{
		registry.unbind("Concord");
		 Platform.setImplicitExit(true);
	}
}
