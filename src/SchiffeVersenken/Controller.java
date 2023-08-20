package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Controller {
	//Verbindungen
	private DataModel datamodel;
    /* Eigenschaften */
    private HashMap<Integer, char[][]> gameState = null;    //-> vielleicht eine HashMap<Hashmap<Integer, String>, char[][]> ?
    public static int gamePhase = 1; //Phase 1: Schiffe setzen Phase, Phase 2: Spieler schießt, Phase 3: Bot schießt,Phase 4: Endphase
    private Integer[] roundToken = null;
    private int[] firePosition = null;
    private int counter = 0;
    private Integer identifyToken = null;
    Random random = new Random();
    
    public Controller(DataModel datamodel) {
        gameState = new HashMap<Integer, char[][]>();
        this.datamodel = datamodel;
    }


    //Auswahl Schiffstyp
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

    //Schiffe setzen
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

    
    //Spielmodus setzen
    public void openGameWindowBot(){
    	datamodel.setGameModi(0);
    }
    public void openGameWindowFriend() {
    	datamodel.setGameModi(1);
    }
    
    
    //Schießen Funktion, Phase 2
    public void onEnemyButtonClicked(int row, int col) {
	    //Schießen Phase BOT
    	if (gamePhase == 2) {
    		if(datamodel.getGameModi()== 0) {
    			datamodel.shootEnemyField(row, col);
    			char enemyCellStatus = datamodel.getEnemyCellStatus(row, col);

    			if (enemyCellStatus == 'x') {
    			    // Ein Schiff wurde getroffen, der Spieler darf weiter schießen
    			    if(datamodel.playerWon) {
    			    	gamePhase = 4; //Ende
    			    }
    			} else if (enemyCellStatus == 'b') {
    			    // Kein Treffer, der Bot ist an der Reihe
    			    gamePhase = 3;
    			    datamodel.BOTShoot(); // Der Bot darf schießen
    			    if(datamodel.enemyWon) {
    			    	gamePhase=4; //Ende
    			    }
    			}
    		 }	
    	}else if(datamodel.getGameModi()==1) {
    		//Schießen gegen Freund
    	}
    }
    
    
    public Integer[] getRoundToken() {
        return this.roundToken;
    }

    //Verändere die Reihenfolge der Spieler -> Seiteneffekt reihenfolge der spieler ist getauscht
    public void swapRoundToken() {
        Integer placeholder = this.roundToken[0];
        this.roundToken[0] = this.roundToken[1];
        this.roundToken[1] = placeholder;
    }
    
    //Setze den Initialen Gamestate : roundtoken -> reihenfolge & char[][] -> initiales Spielfeld
    public void setInitialGameState(HashMap<Integer, char[][]> data) {
        this.gameState = data;
        for(Integer item : data.keySet()) {
            this.roundToken[this.counter] = item;
        }
        this.counter++;

        Controller.gamePhase = 1;
    }

    //Teste ob der Spieler (erkennbar am roundToken welcher mit dem Schuss mitgesendet wird)
    //befähigt ist einen zug zu machen
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

        /* was sind die feldcharacter ?*/
        //wasser
        if(token == 'w') {
            status = 0;
            field[this.firePosition[0]][this.firePosition[1]] = 'b';    //setze feld auf bekannt
        } else if(token == 's') {   //schiff
            status = 1;
            field[this.firePosition[0]][this.firePosition[1]] = 'x'; //setze feld auf getroffen
        } else {    //kein gültiger schuss -> vielleicht vorher schon abfangen (GUI?)
            status = 2;
        }
        
        //Neues Gamestate Object
        response.put(status, field);
        //Gamestate im controller anpasssen, indem feidl eingelesen wird
        
        return response;
    }

    //Spielfeld auslesen, prüfen ob der Zug legit ist
    //Schuss verarbeiten, neues spielfeld zurückgeben (ggf anzahl der schiffe zurückgeben)
    public HashMap<Integer, char[][]> manipulateData(HashMap<Integer, int[]> data) {
        //Testen ob RoundToken valide ist -> wer ist aktuell dran ?
        for(Entry<Integer, int[]> item : data.entrySet()) {
            System.out.println(item.getKey() + item.getValue()[0]);
            //this.tempRoundToken = item.getKey();
            this.firePosition = item.getValue();
            this.testIfValid(item.getKey());
        }
         //bedingung if/else
        //Spielfeld auslesen -> bei den schusswerten character setzen in gamestate einfügen
        HashMap<Integer, char[][]> newGameField = this.evaluateImpact(data);  //Prüft wie effektiv der gesetzte Schuss war
        return null;
    }

}

