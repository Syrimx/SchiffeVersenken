package SchiffeVersenken;

import java.util.HashMap;

public class Main {

	public static void main(String[] args) 
	{
		MenuWindow menuWindow 	= new MenuWindow();
		Spieler spieler 		= new Spieler("Mona");
		Client client 			= new Client();
		
		//GameLoop
		//Preparation Phase
		while(true) {

			if(Controller.gamePhase == 0) {
				//GameFeld aufbauen
				//initiales Feld und spielerToken an den Server/Controller senden
				spieler.initialPayload();
			} else if(Controller.gamePhase == 1) {	//-> Main Game Phase
				//Spieler Schuss
				Integer again = 0;
				do {
					HashMap<Integer, char[][]> newGameField = client.writeServerData(spieler.buildPayload());
					//prüft ob nochmal geschossen werden darf
					again = newGameField.getKey();
					//neues Spielfeld wird an Spieler und von diesem an das DataModel weitergegeben
					spieler.pushNewGameField(newGameField);
				} while(again == 1);

			} else if (Controller.gamePhase == 2) {	// -> Engame Phase
				break;
			}
		}	
		

		
	}

}
