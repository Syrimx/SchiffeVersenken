package SchiffeVersenken;

import java.util.HashMap;
import java.util.Random;
import java.util.Map.Entry;

public class Spieler {

    Random random = new Random();
    private int roundToken = 0; //Token mit dem sich die Spieler gegenüber dem controller Identifizieren
                                //Controller nutzt diesen um zu schauen wer dran ist
    private DataModel datamodel = new DataModel();
    private Integer ships = 0;

    //der Spielerklasse wird die GUI als Objekt übergeben, damit die Spielerklasse auf deren Funktionen zugreifen kann
    //insbesondere receiveCoordinates()
    public Spieler() {
        this.roundToken = random.nextInt(100);
        System.out.println(this.roundToken);
    }

    //Constructs payload which is transmitted over with the client
    //it receives the coordinates triggered with playground.getCurrentPosition() 
    //and returns this objekt ->
    //-> this objekt will be send via the client to the server to fullfill a legit action during the respective turn
    public HashMap<Integer, int[]> buildPayload(int[] currentPosition) {
        HashMap<Integer, int[]> payload = new HashMap<Integer, int[]>();
        payload.put(this.roundToken, currentPosition); //Which datatype is returned by GUI ?
        return payload;
    } 

    //Sendet den initialenToken und das initiale Spielfeld an den Server
    public HashMap<Integer, char[][]> initialPayload(char[][] matrix) {
        System.out.println("Hallo");
        //HashMap<Integer, char[][]> payload = new HashMap<Integer, char[][]>();
        //payload.put(this.roundToken, matrix);
        return null;
    }

    //printHashMap
    public void printHashMap(HashMap<Integer, char[][]> matrix) {
        System.out.println("printTheMap");
        for(Entry<Integer, char[][]> entry : matrix.entrySet()) {
            System.out.println("Key: " + entry.getKey() + ", Value: " + entry.getValue());
        }
    }   
}