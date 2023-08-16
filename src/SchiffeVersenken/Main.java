package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) 
	{
		//MenuWindow menuWindow 	= new MenuWindow();
		//-> GUI?
		Spieler spieler 		= new Spieler();
		DataModel datamodel	= new DataModel();
		Controller controller 	= new Controller(datamodel);
		//Client client 		= new Client();
		

		GUI gameWindow = new GUI();
		gameWindow.drawMenu();
		
	}	

}
