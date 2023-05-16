package sprint1;
import java.util.ArrayList;
import javafx.scene.layout.VBox;
import sprint3.models.DataModel;

public class Bot
{
	public ArrayList<Integer> track(VBox v){return null;}
	
	public void action(ArrayList<Integer> messageIds, VBox v, DataModel D){}
	
	public void  Notify(){}
	
	public final void execute(VBox v, DataModel D)
	{
		ArrayList<Integer> messageIds = track(v);
		action(messageIds,v, D);
		Notify();	
	}
}
