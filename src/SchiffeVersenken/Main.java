package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) 
	{
		//MenuWindow menuWindow 	= new MenuWindow();
		//-> GUI?
		Spieler spieler 		= new Spieler();
		Controller controller 	= new Controller();
		DataModel datamodel	= new DataModel();
		//Client client 		= new Client();
		

		GUI gameWindow = new GUI(datamodel);
		gameWindow.drawMenu();
		
		//noch nicht ideal ausgelagert aus GUI 
//		if(gameWindow.getGameBotOrFriend()) {
//			
//			gameWindow.resetGUI();
//			gameWindow.drawMap();
//			gameWindow.setInstruction("Test");
//		
//		}
	}	

}
