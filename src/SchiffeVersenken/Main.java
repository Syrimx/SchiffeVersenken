package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;

public class Main {

	public static void main(String[] args) 
	{
		MenuWindow menuWindow 	= new MenuWindow();
		//-> GUI?
		Spieler spieler 		= new Spieler("Mona");
		DataModel datamodel		= new DataModel();
		
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
					HashMap<Integer, char[][]> newGameField = client.writeServerData(spieler.buildPayload(menuWindow.getCurrentPosition()));
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
