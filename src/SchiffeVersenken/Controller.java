package SchiffeVersenken;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Arrays;
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
    private Integer identifyToken = null;
    private int counter = 0; //Anzahl der Schiffe ?
    Random random = new Random();
    
    public Controller() {
        gameState = new HashMap<Integer, char[][]>();
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
    public boolean testIfValid() {
        if(this.roundToken[0] == this.identifyToken) {
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
            field[this.firePosition[0]][this.firePosition[1]] = "b";    //setze feld auf bekannt
        } else if(token == 's') {   //schiff
            status = 1;
            field[this.firePosition[0]][this.firePosition[1]] = "x"; //setze feld auf getroffen
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
            this.roundToken = item.getValue();
            this.firePosition = item.getKey();
            this.testIfValid(data.getKey());
        }
         //bedingung if/else
        //Spielfeld auslesen -> bei den schusswerten character setzen in gamestate einfügen
        HashMap<Integer, char[][]> newGameField = this.evaluateImpact(data);  //Prüft wie effektiv der gesetzte Schuss war
        return null;
    }

    //setze initiale schiffe für den roboter
    public void setShipsRobot() {
        char[][] robotMatrix = new Array<char[][]>();


    }
}

