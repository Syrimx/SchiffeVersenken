package SchiffeVersenken;

import java.util.HashMap;
import java.util.Random;

public class Spieler {

    Random random = new Random();
    private String name = null;
    private int roundToken = 0; //Token mit dem sich die Spieler gegenüber dem controller Identifizieren
                                //Controller nutzt diesen um zu schauen wer dran ist
    private GUI gui = null;     //GUI Objekt

    //der Spielerklasse wird die GUI als Objekt übergeben, damit die Spielerklasse auf deren Funktionen zugreifen kann
    //insbesondere receiveCoordinates()
    public Spieler(String name) {
        this.name = name;
        this.roundToken = random.nextInt(100);
        this.gui = new GUI();
    }

    //Constructs payload which is transmitted over with the client
    //it receives the coordinates triggered with playground.getCurrentPosition() 
    //and returns this objekt ->
    //-> this objekt will be send via the client to the server to fullfill a legit action during the respective turn
    public HashMap<Integer, int[]> buildPayload() {
        HashMap<Integer, int[]> payload = new HashMap<Integer, int[]>();
        payload.put(this.roundToken, this.gui.getCurrentPosition()); //Which datatype is returned by GUI ?

        return payload;
    }

    //Basis for setting the action client side -> datamodel utilizes this method to fill the datamodel with initial ships
    public int[] setShip() {
        return this.gui.getCurrentPosition();
    }



    /* Helper Methoden */
    public String getName() {
        return this.name;
    }
}