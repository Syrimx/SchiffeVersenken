package SchiffeVersenken;

import java.util.HashMap;

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
    private int counter = 0;
    
    public Controller() {
        gameState = new HashMap<Integer, char[][]>();
    }


    public void endPhase() {

    }

    public void setGameState() {

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

        this.roundToken[this.counter] = data.getKey();  //nutze das Keyelement der Hashmap als rountToken
        this.counter++;

        Controller.gamePhase = 1;
    }

    //Teste ob der Spieler (erkennbar am roundToken welcher mit dem Schuss mitgesendet wird)
    //befähigt ist einen zug zu machen
    public boolean testIfValid(Integer roundToken) {
        if(this.roundToken[0] == roundToken) {
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
        int[] firePosition = data.getValues()[0];
        char[][] field = this.gameState.getValue(data.getKeys()[0]);
        char token = field[firePosition[0]][firePosition[1]];
        Integer status = null;
        
        /* was sind die feldcharacter ?*/
        //wassser
        if(token == 'w') {
            status = 0;
            field[firePosition[0]][firePosition[1]] = "b";    //setze feld auf bekannt
        } else if(token == 's') {   //schiff
            status = 1;
            field[firePosition[0]][firePosition[1]] = "x"; //setze feld auf getroffen
        } else {    //kein gültiger schuss -> vielleicht vorher schon abfangen (GUI?)
            status = 2;
        }
        
        //Neues Gamestate Object
        response.put(status, field);
        
        return response;
    }

    //Spielfeld auslesen, prüfen ob der Zug legit ist
    //Schuss verarbeiten, neues spielfeld zurückgeben (ggf anzahl der schiffe zurückgeben)
    public HashMap<Integer, char[][]> manipulateData(HashMap<Integer, int[]> data) {
        //Testen ob RoundToken valide ist -> wer ist aktuell dran ?
        this.testIfValid(data.getKey());
        //Spielfeld auslesen -> bei den schusswerten character setzen in gamestate einfügen
        char[][] newGameField = this.evaluateImpact(data);  //Prüft wie effektiv der gesetzte Schuss war
        return null;
    }
}

