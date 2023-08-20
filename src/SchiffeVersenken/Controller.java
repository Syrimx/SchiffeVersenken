package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

/**
 * Die Controller Klasse ist verantwortlich für die Steuerung und Koordination
 * der Spiellogik und Benutzerinteraktion. Sie verwaltet den Spielzustand,
 * die Phasen des Spiels, Spieleraktionen und die Kommunikation mit dem
 * Datamodel.
 */
public class Controller {
	private DataModel datamodel;
	
	//Phase 1: Schiffe setzen, Phase 2: Spieler schießt, Phase 3: Bot schießt, Phase 4: Endphase
	public static int gamePhase = 1; // Aktuelle Phase des Spiels
	//Für Modus: gegen Freund
	private HashMap<Integer, char[][]> gameState = null;
	private Integer[] roundToken = null; // Rundentoken zur Spieleridentifikation
	private int[] firePosition = null; // Koordinaten für Schüsse
	private int counter = 0; // Zähler für die Rundentoken-Reihenfolge
	private Integer identifyToken = null; // Aktuelles Rundentoken
	
	/**
	 * Konstruktor für den Controller.
	 * 
	 * @param datamodel Das Datenmodell, das vom Controller verwaltet wird.
	 */
	public Controller(DataModel datamodel) {
	    gameState = new HashMap<Integer, char[][]>(); // Initialisiert den Spielzustand
	    this.datamodel = datamodel; // Weist das übergebene Datenmodell zu
	}
	
    //Auswahl Schiffstyp
    /**
     * Behandelt die Auswahl des Schiffstyps und veranlasst 
     * die Speicherung der Schiffslänge im Datamodel.
     * 
     * 
     * @param shipType
     */
    public void onShipTypeButtonClicked(String shipType) { 
        int shipLength;
        switch(shipType) {
            case "U-Boot":
                shipLength = 1;
                datamodel.setCurrentShipType(0);
                break;
            case "Fregatte":
                shipLength = 2;
                datamodel.setCurrentShipType(1);
                break;
            case "Kreuzer":
                shipLength = 3;
                datamodel.setCurrentShipType(2);
                break;
            case "Zerstörer":
                shipLength = 4;
                datamodel.setCurrentShipType(3);
                break;
            default:
                shipLength = 0;
                break;
        }
        datamodel.setShipLength(shipLength);
    }
    
    /**
     * Behandelt den Klick auf ein Feld im Spielerfeld, wenn der Spieler Schiffe platziert.
     * 
     * @param row Zeile des geklickten Feldes
     * @param col Spalte des geklickten Feldes
     */
    public void onPlayerButtonClicked(int row, int col) {
    	if(gamePhase == 1 && !datamodel.alleSpielerSchiffePlatziert()) {
    		if (datamodel.getStartRow() == -1 && datamodel.getStartCol() == -1 || datamodel.getTmpshipLength() != datamodel.getShipLength()) {
	              // Speichern der Startposition
	    			datamodel.setStartCell(row, col);
	    			datamodel.setTmpshipLength(datamodel.getShipLength()); 
	    			datamodel.setPickStartPosition(true);
	    			datamodel.setPickEndPosition(false);
	    		}else if(datamodel.getEndRow()== -1 && datamodel.getEndCol() == -1 && datamodel.isPickStartPosition() && !datamodel.isPickEndPosition()) {
	    			datamodel.setEndCell(row, col);
	    			datamodel.placeShips();
	    			datamodel.setStartCell(-1, -1);
	    			datamodel.setEndCell(-1, -1);
	    			datamodel.setTmpshipLength(0);
	    			datamodel.setPickStartPosition(false);
	    			datamodel.setPickEndPosition(true);
	    			int[] shipCounter = datamodel.getShipCounts();
	    			 // Prüfen ob die Anzahl für den gewählten Schiffstyp auf 0 ist und ggf. shipLength zurücksetzen
	    		    if (shipCounter[datamodel.getCurrentShipType()]==0) {
	    		        datamodel.setShipLength(0); // shipLength zurücksetzen
	    		    }
	    		}
    	}  //Wechsel zur Phase 2
        if(datamodel.alleSpielerSchiffePlatziert()) {
        	//Spiel gegen Bot
        	if(datamodel.getGameModi()==0) {
        		//Bot generiert eigene Map
        		datamodel.setPhase(2);
        		gamePhase=2;
        		
             //Spiel gegen Freund
        	}else if(datamodel.getGameModi()==1) {
                datamodel.setPhase(2);
                gamePhase = 2;
        	}

        }
    }

    /**
     * Setzt den Spielmodus auf Bot Spiel.
     */
    public void openGameWindowBot() {
        datamodel.setGameModi(0);
    }
    
    /**
     * Setzt den Spielmodus auf Spiel gegen Freund.
     */
    public void openGameWindowFriend() {
    	datamodel.setGameModi(1);
    }
    
    /**
     * Verarbeitet den Schuss auf das Gegnerfeld während der Schießphase (Phase 2).
     * Bisher nur für Spielmodus Bot implementiert.
     * @param row Zeilennummer des geschossenen Feldes
     * @param col Spaltennummer des geschossenen Feldes
     */
    public void onEnemyButtonClicked(int row, int col) {
        // Schieß-Phase gegen den Bot
        if (gamePhase == 2) {
            if (datamodel.getGameModi() == 0) {
                datamodel.shootEnemyField(row, col);
                char enemyCellStatus = datamodel.getEnemyCellStatus(row, col);

                if (enemyCellStatus == 'x') {
                    // Ein Schiff wurde getroffen, der Spieler darf weiter schießen
                    if (datamodel.playerWon) {
                        gamePhase = 4; // Ende
                    }
                } else if (enemyCellStatus == 'b') {
                    // Kein Treffer, der Bot ist an der Reihe
                    gamePhase = 3;
                    datamodel.BOTShoot(); // Der Bot darf schießen
                    if (datamodel.enemyWon) {
                        gamePhase = 4; // Ende
                    }
                }
            }	
        } else if (datamodel.getGameModi() == 1) {
            // Schießen gegen einen Freund
        }
    }

 // Für den Spielmodus gegen einen Freund (nicht fertig implementiert)
    /**
     * Gibt ein Array der Runden-Tokens zurück, das die Reihenfolge der Spieler angibt.
     * 
     * @return Ein Array der Runden-Tokens, das die Spielerreihenfolge repräsentiert.
     */
    public Integer[] getRoundToken() {
        return this.roundToken;
    }
    
    /**
     * Verändert die Reihenfolge der Spieler im Runden-Token-Array, wodurch die Reihenfolge getauscht wird.
     */
    public void swapRoundToken() {
        Integer placeholder = this.roundToken[0];
        this.roundToken[0] = this.roundToken[1];
        this.roundToken[1] = placeholder;
    }
    
    // Setzt den initialen Spielzustand: Runden-Token -> Reihenfolge & char[][] -> initiales Spielfeld
    /**
     * Setzt den Anfangszustand des Spiels, indem die Runden-Token auf die Spielerreihenfolge abgestimmt
     * und das initiale Spielfeld festgelegt wird.
     * 
     * @param data Eine HashMap, die die Zuordnung von Runden-Token zu den initialen Spielfeldern enthält.
     */
    public void setInitialGameState(HashMap<Integer, char[][]> data) {
        this.gameState = data;
        for(Integer item : data.keySet()) {
            this.roundToken[this.counter] = item;
        }
        this.counter++;

        Controller.gamePhase = 1;
    }
    
    // Testet, ob der Spieler (erkennbar am Runden-Token, der mit dem Schuss mitgesendet wird) berechtigt ist, einen Zug zu machen
    /**
     * Überprüft, ob der Spieler an der Reihe ist, basierend auf dem übergebenen Runden-Token. 
     * Falls der Spieler an der Reihe ist, wird die Reihenfolge der Spieler getauscht und true zurückgegeben.
     * Andernfalls wird false zurückgegeben.
     * 
     * @param identifyToken Das Runden-Token, das den Spieler identifiziert.
     * @return true, wenn der Spieler an der Reihe ist und erfolgreich getauscht wurde, ansonsten false.
     */
    public boolean testIfValid(Integer identifyToken) {
        if(this.roundToken[0] == identifyToken) {
            this.swapRoundToken();
            return true;
        } 
        return false;
    }

    //nimmt die Koordinaten entgegen und liest sie gegen das aktuelle feld, gibt statuscode zurück
    //0 -> wasser; 1 -> neu getroffen; -> 2 -> bekannt getroffen
    //gibt zusätzlich das neue Feld zurück
    /**
     * Bewertet den Effekt eines Schusses, indem die übergebenen Koordinaten gegen das aktuelle Spielfeld geprüft werden.
     * Je nachdem, ob das Feld Wasser ('w') oder ein Schiff ('s') ist, wird der entsprechende Statuscode und das aktualisierte
     * Spielfeld zurückgegeben.
     * 
     * @param data Ein Set von Daten
     * @return Eine HashMap, die den Statuscode und das aktualisierte Spielfeld enthält.
     */
    public HashMap<Integer, char[][]> evaluateImpact(HashMap<Integer, int[]> data) {
        HashMap<Integer, char[][]> response = new HashMap<Integer, char[][]>();
        char[][] field = null;
        for(Entry<Integer, char[][]> element : this.gameState.entrySet()) {
            if(element.getKey() != this.identifyToken) {
                field = element.getValue();
            }
        }
        char token = field[this.firePosition[0]][this.firePosition[1]]; //Value des felds
        Integer status = null;
        
        //wasser
        if(token == 'w') {
            status = 0;
            field[this.firePosition[0]][this.firePosition[1]] = 'b';    //setze feld auf bekannt
        } else if(token == 's') {   //schiff
            status = 1;
            field[this.firePosition[0]][this.firePosition[1]] = 'x'; //setze feld auf getroffen
        } else {    //kein gültiger schuss 
            status = 2;
        }
        
        //Neues Gamestate Object
        response.put(status, field);
        //Gamestate im Controller anpasssen, indem Feind eingelesen wird
        
        return response;
    }
    
    /**
     * Liest das Spielfeld aus, überprüft die Legitimität des Zugs, verarbeitet den Schuss und gibt ein neues Spielfeld zurück.
     * Außerdem kann die Anzahl der verbleibenden Schiffe zurückgegeben werden.
     * 
     * @param data Ein Set von Daten
     * @return Eine HashMap, die das aktualisierte Spielfeld und ggf. Informationen zur Anzahl der Schiffe enthält.
     */
    public HashMap<Integer, char[][]> manipulateData(HashMap<Integer, int[]> data) {
        //Testen ob RoundToken valide ist -> wer ist aktuell dran ?
        for(Entry<Integer, int[]> item : data.entrySet()) {
            System.out.println(item.getKey() + item.getValue()[0]);
            //this.tempRoundToken = item.getKey();
            this.firePosition = item.getValue();
            this.testIfValid(item.getKey());
        }
         //Bedingung if/else
        //Spielfeld auslesen -> bei den schusswerten character setzen in gamestate einfügen
        HashMap<Integer, char[][]> newGameField = this.evaluateImpact(data);  //Prüft wie effektiv der gesetzte Schuss war
        return null;
    }

}

