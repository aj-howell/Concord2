package sprint3.main;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sprint3.models.*;
import sprint1.Database;
import sprint2.Client;
import sprint2.Server;
import sprint2.ServerInterface;
import sprint3.views.MainController;

public class Main extends Application
{
	public Main()
	{
		
	}

	@Override
	// Our model will do all the functionality by calling this method
	// our controller will reference our model

	public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/Login_View.fxml"));
		AnchorPane view = loader.load();
		Scene s = new Scene(view);
		stage.setScene(s);
		
		MainController m = loader.getController();
		Database DB;
		try{
			DB=Database.loadFromDisk();
		}
		catch(RuntimeException e)
		{
			DB = new Database();
		}
		
		Server mc = new Server(DB);
		//int a = (int) Math.ceil(Math.random()*(9999-1+1)+1);
		Registry registry = LocateRegistry.createRegistry(2902); //acts as local "host"
		registry.rebind("Concord", mc); // renames server (remote obejct) for lookup
		
		ServerInterface proxy;
		proxy = (ServerInterface)registry.lookup("Concord"); //sets the proxy so that it limits access
		Client c = new Client(proxy);
		
		DataModel D = new DataModel(c);
		NavigationModel Nav = new NavigationModel(c);
		m.setModel(Nav,D);
		
		stage.show();
		stage.setOnCloseRequest(event ->
		{
			//c.removeClient();
			System.out.println("System has been exited");
		});
		
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}
}
