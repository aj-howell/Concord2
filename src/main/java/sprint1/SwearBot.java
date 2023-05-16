package sprint1;

import java.util.ArrayList;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import sprint3.models.DataModel;

public class SwearBot extends Bot
{
	boolean isPresent = false;
	@Override
	public ArrayList<Integer> track(VBox v)
	{
		String[] swears = {"coffee","chocolate","cat"}; // for the purpose of this class we will make coffee, chocolate, cat swears
		ArrayList<Integer> messageIds = new ArrayList<Integer>();
		//v.getChildren().get(0).toString();		
		v.getChildren().forEach(m->
		{
			for(int i=0; i<=swears.length-1; i++)
			{
				if(m.toString().toLowerCase().contains(swears[i])==true)
				{
					int index = v.getChildren().indexOf(m);
					messageIds.add(index);	
					isPresent=true;
				}
			}
		});
		
		return messageIds;
	}
	
	@Override
	public void action(ArrayList<Integer> messageIds, VBox v, DataModel D)
	{
		if(isPresent==true){
		messageIds.forEach(mId ->
		{
			int Db_ID=Integer.valueOf(v.getChildren().get(mId).getId().replace("message: ",""));
			D.getMessage(Db_ID).setContent("This message was deleted");
			Label L = new Label("This message was deleted");
			v.getChildren().set(mId, L);
		});}
		
	}
	
	@Override
	public void Notify()
	{
		if(isPresent==true){
		System.out.println("Do Not Send Swear");
		isPresent=false;
		}
		
	}
}
