package sprint3.models;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sprint2.*;
import sprint3.main.Main;
import sprint3.views.MainController;

/*Any function call that leads to a specific page needs that controller*/
public class NavigationModel 
{	
	Stage stage;
	Client C;
	public boolean online=true;

	public NavigationModel(Client C)
	{
		this.stage = new Stage();
		this.C=C;
	}
	
	public void showNewGroup(String UserName, String Password)
	{	
		if(C.containsUserName(UserName)==true)
		{
			System.out.println("USERNAME: "+UserName+" is already taken");
		}
		
		else{
		int id = C.insertUser(UserName, Password);
		C.addClienttoList(id);
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NavigationModel.class.getResource("../views/New_Group_View.fxml"));
		try 
		{
			VBox view = loader.load();
			Scene s = new Scene(view);
			stage.setScene(s);
			
			MainController m = loader.getController();
			DataModel D = new DataModel(C);
			D.UserName=UserName;
	
			m.setModel(this,D);
				
			stage.show();
			stage.setOnCloseRequest(event ->
			{
				System.out.println("System has been exited");
				//C.removeClient();
			});
			
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	  }
	}
	
	public void showMainPage(String UserName, String Password)
	{	
		int id = C.findUserID(UserName);
		if(id>-1) {
		C.setClientID(id);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NavigationModel.class.getResource("../views/ChatPage.fxml"));
	
		try 
		{
			AnchorPane view = loader.load();
	
			Scene s = new Scene(view);
			stage.setScene(s);
			
			MainController m = loader.getController();
			
			DataModel D = new DataModel(C);
			D.UserName=UserName;
			
			System.out.println("Nav UserID: "+D.findUser(D.UserName));
		
			m.loadData(D);
			m.loadUsers(D);
			
			m.setModel(this,D);
			
			stage.show();
			stage.setOnCloseRequest(event ->
			{
				System.out.println("System has been exited");
			});
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}	
	}
	}
	
	public void showLogIn()
	{
		online=false;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("../views/Login_View.fxml"));
		AnchorPane view;
		
		try{
			view = loader.load();
			Scene s = new Scene(view);
			stage.setScene(s);
			MainController m = loader.getController();
			
			DataModel D = new DataModel(C);
			m.setModel(this,D);
			
			stage.show();
			stage.setOnCloseRequest(event ->
			{
				System.out.println("System has been exited");
			});
			
		} catch (IOException e)
		
		{
			e.printStackTrace();
		}	
	}
}
