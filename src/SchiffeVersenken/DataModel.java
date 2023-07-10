package SchiffeVersenken;
import java.util.*;

/* Model -> Client
 * |-> Spieler sendet Daten an das Model
 * 
 * Methoden:
    public int[][] getData() 
    public void setData(int[][] data) 


 * 
*/

public class DataModel {
    private char[][] playgroundMatrix    = null; //Wie groß darf sie werden ?                               //int durch char getauscht, um mehr möglichkeiten zu haben, wie Nutzung von X
    private int[] fireLocation          = null; //gewonnen durch GUI.getCurrentLocation


    //verschiedene Schiffsklassen und deren Anzahl 
    int submarine = 4;
    int frigate = 3;
    int cruiser = 2;
    int battleship = 1;

    public char [][] DataMode() {
        // -> Intiale Matrix erstellen // also einmal beim Initialisierendes Objektes
        // Groeße des Feldes nach klassischen Spielregeln 10 x 10 
        this.playgroundMatrix = new char [10][10];

            //Erzeugen eines 10 x 10 Feldes gefüllt mit Nullen zum testen des Arrays
            for(int i = 0; i < playgroundMatrix.length; i++){
                for(j = 0; j < playgroundMatrix[i].length; j++){
                    System.out.print(playgroundMatrix[i][j] + " ");
                }
                System.out.println();           //geht eine Zeile nach unten
            }
            return this.playgroundMatrix;
    } 

     public void placeShips(int startX, int startY, int endX, int endY){


        //Diagonalität vermeiden
        while(startX == endX || startY == endY){
            System.out.println("Dieses Feld ist grün");
        }

        while(startX != endX || startY != endY){
            System.out.println("Dieses Feld ist rot");
        }


        //Bedingungen für setzen der Schiffe 
        if(endX - startX == 1 || startX - endX == -1 || endY - startY == 1 || startY - endY == -1){
            submarine--;
            if(submarine <= 0){
                System.out.print("Du hast keine U-Boote mehr");
            }else{}

        }else if(endX - startX == 2 || startX - endX == -2 || endY - startY == 2 || startY - endY == -2){
            frigate--;
            if(frigate <= 0){
                System.out.print("Du hast Frigatten mehr");
            }else{}

        }else if (endX - startX == 3 || startX - endX == -3 || endY - startY == 3 || startY - endY == -3){
            cruiser--;
            if(cruiser <= 0){
                System.out.print("Du hast keine Kreuzer mehr");
            }else{}

        }else if(endX - startX == 4 || startX - endX == -4 || endY - startY == 4 || startY - endY == -4){
            battleship--;
            if(battleship <= 0){
                System.out.print("Du hast keine Schlachtschiffe mehr");
            }else{}

        }else{
            System.out.println("Kein zulaessiger Schiffstyp");
        }


     }


    }

    public void setData(char[][] data) {
        this.playgroundMatrix = data;
    }


    /*
     * TO-DO
     * Mouse-Listener, um Array auswählen zu können oder doch lieber Tastatur Eingabe oder sollte das eher ins GUI
     * Kollisionsabfrage mit anderen Schiffen aber auch dem Spielfeldrand
     * tatsächlich schiff setzen
     * Frage: muss überprüft werden, ob Koordinaten innerhalb des Feldes liegen, oder wird das im GUI gemacht
     */