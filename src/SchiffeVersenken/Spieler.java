package SchiffeVersenken;

import java.util.HashMap;
import java.util.Random;

public class Spieler {

    Random random = new Random();
    private String name = null;
    private int roundToken = 0; //Token mit dem sich die Spieler gegenüber dem controller Identifizieren
                                //Controller nutzt diesen um zu schauen wer dran ist
    private DataModel datamodel = new DataModel();
    private Object currentPosition = null;
    private Integer ships = 0;

    //der Spielerklasse wird die GUI als Objekt übergeben, damit die Spielerklasse auf deren Funktionen zugreifen kann
    //insbesondere receiveCoordinates()
    public Spieler(String name) {
        this.name = name;
        this.roundToken = random.nextInt(100);
        //this.gui = new GUI();
        //this.datamodel = new DataModel();
    }

    //Constructs payload which is transmitted over with the client
    //it receives the coordinates triggered with playground.getCurrentPosition() 
    //and returns this objekt ->
    //-> this objekt will be send via the client to the server to fullfill a legit action during the respective turn
    public HashMap<Integer, Object> buildPayload(Object currentPosition) {
        HashMap<Integer, Object> payload = new HashMap<Integer, Object>();
        this.currentPosition = currentPosition;   //Speichert die, vom gui objekt bezogene Click Position
        payload.put(this.roundToken, this.currentPosition); //Which datatype is returned by GUI ?

        return payload;
    } 

    //Sendet den initialenToken und das initiale Spielfeld an den Server
    public HashMap<Integer, char[][]> initialPayload() {
        HashMap<Integer, char[][]> payload = new HashMap<Integer, char[][]>();
        payload.put(this.roundToken, datamodel.getPlaygroundMatrix());
        return payload;
    }


    //Basis for setting the action client side -> datamodel utilizes this method to fill the datamodel with initial ships
    public int[] setShip() {
        return this.gui.getCurrentPosition();
    }

    public void pushNewGameField(HashMap<Integer, char[][]> data) {
        this.checkResponseCode(data.getKey());
        this.datamodel.setData(data.getValue());
    }

    /* Helper Methoden */
    public String getName() {
        return this.name;
    }
}