package SchiffeVersenken;

import java.util.HashMap;
import java.util.Random;

public class Spieler {
    Random random = new Random();
    private String name = null;
    private int roundToken = 0; //Token mit dem sich die Spieler gegenüber dem controller Identifizieren
                                //Controller nutzt diesen um zu schauen wer dran ist
    private GUI playground = null;

    //der Spielerklasse wird die GUI als Objekt übergeben, damit die Spielerklasse auf deren Funktionen zugreifen kann
    //insbesondere receiveCoordinates()
    public Spieler(String name, GUI playground) {
        this.name = name;
        this.roundToken = random.nextInt(100);
        this.playground = playground;
    }

    //Constructs payload which is transmitted over with the client
    //it receives the coordinates triggered with playground.getCurrentPosition() 
    //and returns this objekt
    public HashMap<Integer, int[]> buildPayload() {
        HashMap<Integer, int[]> payload = new HashMap<Integer, int[]>();
        payload.put(this.roundToken, this.playground.getCurrentPosition());

        return payload;
    }

    /* Helper Methoden */
    public String getName() {
        return this.name;
    }
}