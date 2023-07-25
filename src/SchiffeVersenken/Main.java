package SchiffeVersenken;

import java.util.HashMap;

public class Main {

	public static void main(String[] args) 
	{
		MenuWindow menuWindow 	= new MenuWindow();
		//-> GUI?
		Spieler spieler 		= new Spieler("Mona");
		Client client 			= new Client();
		
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
					HashMap<Integer, char[][]> newGameField = client.writeServerData(spieler.buildPayload(menuWindow.getCurrentPosition()));
					//-> spieler prüft status ?
					//neues datamodel
					datamodel.setData(newGameField.getValues()[0]);
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
