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
		//DataModel datamodel	= new DataModel();
		//Client client 		= new Client();
		

		GUI gameWindow = new GUI();
		gameWindow.drawMenu();
		//noch nicht ideal ausgelagert aus GUI 
//		if(gameWindow.getGameBotOrFriend()) {
//			
//			gameWindow.resetGUI();
//			gameWindow.drawMap();
//			gameWindow.setInstruction("Test");
//		
//		}
		

		
		/* 
		//GameLoop
		while(true) {
			//Preparation Phase
			if(Controller.gamePhase == 0) {
				//GameFeld aufbauen
				//initiales Feld und spielerToken an den Server/Controller senden
				spieler.initialPayload();
				//Game Phase umstellen
			//-> Main Game Phase
			} else if(Controller.gamePhase == 1) {	
				//Spieler Schuss
				Integer again = 0;
				do {
					//-> spieler prüft status ?
					//neues datamodel
					for(Entry<Integer, char[][]> element : newGameField.entrySet()) {
						datamodel.setEnemyMatrix(element.getValue());
						again = newGameField.getKey();
					}
					//GUI neu zeichnen
					menuWindow.refreshMaps();
					//prüft ob nochmal geschossen werden darf
				} while(again == 1);

			} else if (Controller.gamePhase == 2) {	// -> Engame Phase
				break;
			}
		}	*/
		

		
	}

}
