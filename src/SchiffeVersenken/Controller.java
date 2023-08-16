package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;


/* Controller -> Server
 * 
 * Eigenschaften:
 *  Gamestate
 * 
 * Methoden:
 *  public void gameLoop()
    public void preparationPhase()
    public void endPhase()
    public void setGameState()

*/

public class Controller {
    /* Eigenschaften */
    private HashMap<Integer, char[][]> gameState = null;    //-> vielleicht eine HashMap<Hashmap<Integer, String>, char[][]> ?
    public static int gamePhase = 1; //-> 0 Preparation Phase; 1 Main Phase; 2 Endgame Phase
    private Integer[] roundToken = null;
    private int[] firePosition = null;
    private int counter = 0;
    private Integer identifyToken = null;
    private char[][] robotMatrix = new char[10][10];
    Random random = new Random();
    
    ////NEU:
    private DataModel datamodel;
    private Client client;
    private Spieler spieler;
    ////
    
    public Controller(DataModel datamodel) {
        gameState = new HashMap<Integer, char[][]>();
        
        ////NEU:
        this.datamodel = datamodel;
        ////
    }


    
    ////NEU:
    //Auswahl Schiffstyp
    public void onShipTypeButtonClicked(String shipType) {
        int shipLength;
        switch(shipType) {
            case "Submarine":
                shipLength = 1;
                break;
            case "Frigate":
                shipLength = 2;
                break;
            case "Cruiser":
                shipLength = 3;
                break;
            case "Battleship":
                shipLength = 4;
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
	    		}
    	}if(datamodel.alleSpielerSchiffePlatziert()) {
//    		 datamodel.setPhase(2);
//        	 Controller.gamePhase=2;
        	 
        	 //Error?
        	 System.out.println("Done");
             System.out.println(this.datamodel.getPlaygroundMatrix().toString());
             HashMap<Integer, char[][]> response = this.client.writeServerData(this.spieler.initialPayload(this.datamodel.getPlaygroundMatrix()));
         	 System.out.println(response.toString());
             datamodel.setPhase(2);
             //
        }
    }
    
    //Spielmodus setzen
    public void openGameWindowBot(){
    	datamodel.setGameModi(0);
    }
    public void openGameWindowFriend() {
    	datamodel.setGameModi(1);
    }
    
    
    //Schießen Funktion, Datamodel manipulieren
    public void onEnemyButtonClicked(int row, int col) {
    	
    }
    
    ////
    
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

    /*** Robot Section ***/
    /*check if field is set*/
    //                       left right        up/down
    ////-> {row and column}, {stepsHorizontal, stepsVertical}
    //return -1 in every field element to flag that the field is not fine
    public int[][] checkCollision(int row, int column, int shipLength) {
        int[][] validPosition = {{-1,-1}, {0,0}};
        int horizontalSteps = 0;
        int verticalSteps = 0;

        //check if the selected position is already set
        //return inital validPosition array if place is already set
        if(this.robotMatrix[row][column] != 'w') {
            return validPosition;
        //return valid array, when position is fine and ship length is 1
        } else if(shipLength == 1 && this.robotMatrix[row][column] == 'w') {
            int[][] temp = {{row, column},{0,0}};
            return temp;
        }   

        //check horizontally
        //negative numbers are representing down and left within the steps that can be taken
        for(int i = 0; i < this.robotMatrix.length; i++) {
            //check right
            for(int j = 1; i <= shipLength; j++) {
                //check if coordinate still within the field
                if(row + j > 9) {
                    break;
                }
                //check if fields to the left of given coordinate is valid
                if(this.robotMatrix[row+j][column] != 'w') {
                    break;
                }
                //if every field is check set the setps
                if(j == shipLength) {
                    horizontalSteps = shipLength; // -> place seems to valid so the horizontal field can be set
                }
            }

            //check left
            if(horizontalSteps == 0) {
                for(int j = 1; i <= shipLength; j++) {
                    //check if coordinate still within the field
                    if(row - j < 0) {
                        break;
                    }
                    //check if fields to the left of given coordinate is valid
                    if(this.robotMatrix[row-j][column] != 'w') {
                        break;
                    }
                    //if every field is check set the setps
                    if(j == shipLength) {
                        horizontalSteps = shipLength; // -> place seems to valid so the horizontal field can be set
                    }                
                }
            }
 
        }

        //check vertically 
        if(horizontalSteps == 0) {
            for(int i = 0; i < this.robotMatrix.length; i++) {
                //check up
                for(int j = 1; i <= shipLength; j++) {
                    //check if coordinate still within the field
                    if(column + j > 9) {
                        break;
                    }
                    //check if fields to the left of given coordinate is valid
                    if(this.robotMatrix[row][column+j] != 'w') {
                        break;
                    }
                    //if every field is check set the setps
                    if(j == shipLength) {
                        verticalSteps = shipLength; // -> place seems to valid so the horizontal field can be set
                    }                
                }

                //check down
                if(verticalSteps == 0) {
                    for(int j = 1; i <= shipLength; j++) {
                        //check if coordinate still within the field
                        if(column - j < 0) {
                            break;
                        }
                        //check if fields to the left of given coordinate is valid
                        if(this.robotMatrix[row][column-1] != 'w') {
                            break;
                        }
                        //if every field is check set the setps
                        if(j == shipLength) {
                            verticalSteps = shipLength*(-1); // -> place seems to valid so the horizontal field can be set
                        }         
                    }
       
                }
            }
        }


        //set new steps
        validPosition[1][0] = horizontalSteps;
        validPosition[1][1] = verticalSteps;


        return validPosition; // -> new positions must be validated while setting
    }

    //set Ships -> unimplemented 
    public void setShips(int[][] data) {

    }

    //setze initiale schiffe für den roboter
    //10 schiffe: [4mal 1x1, 3mal 2x2, 2mal 3x3, 1mal 4x4]
    public void setMatrixRobot() {
        int[] ships = {4,3,3,2,2,2,1,1,1,1};
        //initialize robotMatrix
        for(int i = 0; i < this.robotMatrix.length; i++) {
            for(int j = 0; j < this.robotMatrix.length; j++) {
                this.robotMatrix[i][j] = 'w';
            }
        }

        //check ships -> ungetested
        //check if position is valid                                        left/ right        up/down
        int[][] validPosition   = {{-1,-1}, {0,0}}; //-> {row and column}, {stepsHorizontal, stepsVertical}
        for(int ship : ships) {

            int row                 = 0;
            int column              = 0;
            do{
                row     = random.nextInt(10);
                column  = random.nextInt(10);
                validPosition = checkCollision(row, column, ship);
            } while(validPosition[0][0] == -1);

        }
        
        //setships -> ungetested
        this.setShips(validPosition);

    }


    //print the robot map
    public void printRobotMap() {
        for(int i = 0; i < robotMatrix.length; i++) {
            for(int j = 0; j < robotMatrix.length; j++) {
                System.out.println(this.robotMatrix[i][j]);
            }
        }
    }
}

