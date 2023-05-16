package sprint3.views;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import sprint1.Channel;
import sprint1.SwearBot;
import sprint3.models.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainController implements Initializable {

	@FXML
	private Button Login;

	@FXML
	private Button register;

	@FXML
	private TextField textMessage;

	@FXML
	private ScrollPane scrollMessage;

	@FXML
	public ListView<Text> ChannelList;

	@FXML
	private Label channelName;

	@FXML
	private Button groupButton;

	@FXML
	public MenuButton menubutton;

	@FXML
	private TextField password;

	@FXML
	private TextField UserName;

	@FXML
	private Button MainPageButton;
	
	@FXML
	private MenuButton usersMenuButton;
	
    @FXML
    private ListView<String> UsersList;
    
    @FXML
    private Button UserButton;

    @FXML
    private Label UserLabel;


	@FXML
	private Label WelcomeSign;
	
	public boolean isNull;

	private DataModel D;
	private VBox v;
	private NavigationModel model;
	private SwearBot bot;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.v = new VBox();
		v.setId("messages");
		bot = new SwearBot();
		
	}

	public void setModel(NavigationModel model, DataModel D) {
		this.model = model;
		this.D = D;
	}

	@FXML
	public void onClickLogin(ActionEvent event) { // model.showMainPage(UserName.getText(), password.getText());
		if (D.getGroups(UserName.getText()) == null) {
			System.out.println("No groups");
		} 
			model.showMainPage(UserName.getText(), password.getText());
			System.out.println("Login Clicked");
	}

	@FXML
	public void onClickRegister(ActionEvent event) {
		model.showNewGroup(UserName.getText(), password.getText());
		System.out.println("Register Clicked");
	}

	@FXML
	public void onClickLogOut()
	{
		model.showLogIn();
	}

	@FXML
	public void mainPage(ActionEvent Event) {
		int id = D.findUser(D.UserName);
		model.showMainPage(D.UserName, D.getUser(id).Password);
	}
	
	public void loadData(DataModel D)
	{
		D.getGroups(D.UserName).forEach(g->{
			System.out.println("X"+D.getGroup(g).GroupName);
			String groupName = D.getGroup(g).GroupName;
			MenuItem group = new MenuItem(groupName);
			group.setId(String.valueOf(g));
			
			//loads appropriate channels and messages within that group
			group.setOnAction(new EventHandler<ActionEvent>(){

				@Override
				public void handle(ActionEvent event)
				{
					menubutton.setText(group.getText());
					menubutton.setId(group.getId());
					ChannelList.getItems().clear();
					v.getChildren().clear();
					ArrayList<Integer> ch = D.getChannels(D.UserName, g);
					ch.forEach(ch_ids ->
					{
						Text channel_names = new Text(D.getChannel(ch_ids).Topic);
						ChannelList.getItems().add(channel_names);
					});
				}
				
			});
			menubutton.getItems().add(group);
		});
				
		// clears listview when channel is clicked on and loads appropriate messages
		ChannelList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				v.getChildren().clear();
				textMessage.clear();
				if (ChannelList.getSelectionModel().getSelectedItem().equals(null)){
					System.out.println("Cannot Change because it is already null");
				}
				
				
				channelName.setText("Channel: " + ChannelList.getSelectionModel().getSelectedItem().getText());
				System.out.println("clicked on " + ChannelList.getSelectionModel().getSelectedItem().getText());
				int cId=D.findChannelbyName(ChannelList.getSelectionModel().getSelectedItem().getText());
				channelName.setId(String.valueOf(cId));

				D.getChannel(Integer.valueOf(channelName.getId())).messageIDs.forEach(m -> {
					System.out.println("MessageID: "+m);
					Label message = new Label();
					message.setText(D.getMessage(m).getContent());
					message.setId("message: "+D.getMessage(m).getMessageID());
					System.out.println("Message: " + D.getMessage(m).getContent());
					v.getChildren().add(message); // Not displaying
					//scrollMessage.setContent(v);
				});

			}
				
		});
				//bot.execute(contents, v, D);
				v.setFillWidth(true);
				scrollMessage.setContent(v);
				//ChannelList.getItems().forEach(c->c.setVisible(false));
	}

	
	public void loadUsers(DataModel D)
	{
		/* loads the correct users of specific list after signing in and selecting a category*/
		usersMenuButton.getItems().get(0).setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event)
			{
				UsersList.getItems().clear();
				usersMenuButton.setText("View: Banned");
				UserLabel.setText("Banned Users");
				UserLabel.setVisible(true);
				
				//loads user that are banned
				int gID=D.findGroupbyName(menubutton.getText());
				D.getGroup(gID).BannedUsers.forEach(ban ->
				{
					 String UserName=D.getUser(ban).Name;
					 UsersList.getItems().add(UserName);
				});
			}
		});
		
		usersMenuButton.getItems().get(1).setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event)
			{
				UsersList.getItems().clear();
				usersMenuButton.setText("View: Users");
				UserLabel.setText("Group Users");
				UserLabel.setVisible(true);
				
				//loads users that are not banned or kicked
				int gID=D.findGroupbyName(menubutton.getText());
				D.getGroup(gID).UserIDs.forEach(users ->
				{
					 String UserName=D.getUser(users).Name;
					 UsersList.getItems().add(UserName);
				});
			}
		});
		
		usersMenuButton.getItems().get(2).setOnAction(new EventHandler<ActionEvent>(){

			@Override
			public void handle(ActionEvent event)
			{
				UsersList.getItems().clear();
				usersMenuButton.setText("View: Kicked");
				UserLabel.setText("Kicked Users");
				UserLabel.setVisible(true);
				
				//loads user that are kicked
				int gID=D.findGroupbyName(menubutton.getText());
				D.getGroup(gID).Kicked.forEach(kick ->
				{
					 String UserName=D.getUser(kick).Name;
					 UsersList.getItems().add(UserName);
				});
			}
		});
	}
	
	@FXML
	public void addGroup(ActionEvent event) {
		
		int id = D.findUser(D.UserName);
		int groupID = D.addGroup(textMessage.getText(), id);
		D.getGroup(groupID).setAdmin(id);

		MenuItem group = new MenuItem(textMessage.getText());
		group.setId(String.valueOf(groupID));
		
		//loads appropriate channels and messages within that group
		group.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				textMessage.clear();
				// when clicked change the name to selected menu item
				menubutton.setText(group.getText());
				menubutton.setId(group.getId());
				System.out.println("GroupName: " + group.getText() + "id: " + group.getId());

				ChannelList.getItems().clear();
				v.getChildren().clear();
				D.getChannels(D.UserName, Integer.valueOf(menubutton.getId())).forEach(i -> {
					Text t = new Text(D.getChannel(i).getTopic());
					t.setId(D.getChannel(i).getTopic());
					ChannelList.getItems().add(t);
				});

			}
		});

		menubutton.getItems().add(group);
		v.getChildren().clear();
		menubutton.setId(group.getId());

		// change to new group when added
		menubutton.setText(textMessage.getText());
		ChannelList.getItems().clear(); // once a new group is added then clear Channel list
		textMessage.clear();
	}

	@FXML
	public void onClickRegisterChannel(ActionEvent event) {
		int cID = D.addChannel(textMessage.getText(), Integer.valueOf(menubutton.getId()), D.UserName);
		textMessage.clear();
		Channel c = D.getChannel(cID);
		channelName.setId(String.valueOf(cID)); // only way to recognize what channel you are currently in
		channelName.setText("Channel: " + c.getTopic());
		Text t = new Text(c.getTopic());
		t.setId(c.getTopic());
		ChannelList.getItems().add(t);
		v.getChildren().clear();

		// clears listview when channel is clicked on and loads appropriate messages
		ChannelList.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				v.getChildren().clear();
				textMessage.clear();
				if (ChannelList.getSelectionModel().getSelectedItem().equals(null)) {
					System.out.println("Cannot Change because it is already null");
				}
				
				channelName.setText("Channel: " + ChannelList.getSelectionModel().getSelectedItem().getText());
				System.out.println("clicked on " + ChannelList.getSelectionModel().getSelectedItem().getText());
				int cId=D.findChannelbyName(ChannelList.getSelectionModel().getSelectedItem().getText());
				channelName.setId(String.valueOf(cId));

				D.getChannel(Integer.valueOf(channelName.getId())).messageIDs.forEach(m -> {
					System.out.println("MessageID: "+m);
					Label message = new Label();
					message.setText(D.getMessage(m).getContent());
					System.out.println("Message: " + D.getMessage(m).getContent());
					v.getChildren().add(message);
				});
				
			}
		});
		System.out.println("Channel Created " + c.getTopic());
		textMessage.clear();
	}

	@FXML
	public void onClickSend(ActionEvent event) {
		
		//cannot send an empty message
		if (textMessage.getText().isEmpty()){
			System.out.println("No Message Found");
			isNull=true;
		}
		
		else {
		isNull=false;
		int c = Integer.valueOf(channelName.getId());
		
		int g_id = Integer.valueOf(menubutton.getId());

		int UserID = D.findUser(D.UserName);

		System.out.println("Sender: " + UserID);

		boolean canSend=D.SendMessage(c, textMessage.getText(), UserID, g_id);

		if(canSend) {
		Label message = new Label();
		message.setText(textMessage.getText());
		message.setId("message: "+D.getChannel(c).LastMessageID);

		v.getChildren().add(message);
		v.setAlignment(Pos.BASELINE_RIGHT);

		System.out.println(v.alignmentProperty().get());
		
		v.setFillWidth(true);
		scrollMessage.setContent(v);
		}
		
		textMessage.clear();
		
		ArrayList<String> contents = new ArrayList<String>();
		D.getChannel(c).messageIDs.forEach(mId ->
		{
			contents.add(D.getMessage(mId).getContent());
		});
		
		bot.execute(v, D);
	}	
}	

   @FXML
   void addUser(ActionEvent event)
    {	
    	int UserID =D.findUser(textMessage.getText()); // this checks whether or not this user exists
		int g_id=D.findGroupbyName(menubutton.getText());
    	
		//add to banned users
    	if(!menubutton.getText().equals("GroupName")&&usersMenuButton.getText().equals("View: Banned"))
    	{
    		D.getGroup(g_id).BannedUsers.add(UserID);
    		UsersList.getItems().add(textMessage.getText());
    		textMessage.clear();
    	}
    	
    	//add user to the group
    	if(!menubutton.getText().equals("GroupName")&&usersMenuButton.getText().equals("View: Users"))
    	{
    		D.getGroup(g_id).UserIDs.add(UserID); //adds user to that group
    		D.getUser(UserID).GroupIDS.add(g_id); // add group to that Users set of groups
    		UsersList.getItems().add(textMessage.getText());
    		textMessage.clear();
    	}
    	
    	//add to kicked users
    	if(!menubutton.getText().equals("GroupName")&&usersMenuButton.getText().equals("View: Kicked"))
    	{
    		D.getGroup(g_id).Kicked.add(UserID);
    		UsersList.getItems().add(textMessage.getText());
    		textMessage.clear();
    	}
    }
}
