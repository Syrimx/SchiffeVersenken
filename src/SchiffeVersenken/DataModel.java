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

    /* DataModel Methoden */
    public int[][] getData() {

        // Groeße des Feldes nach klassischen Spielregeln 10 x 10 
        private char[][] playgroundMatrix = new char [10][10];

            //Erzeugen eines 10 x 10 Feldes gefüllt mit Nullen zum testen des Arrays
            for(int i = 0, i < playgroundMatrix.length; i++){
                for(j = 0, j < playgroundMatrix[i].length; j++){
                    System.out.print(playgroundMatrix[i][j] + " ");
                }
                System.out.println();           //geht eine Zeile nach unten
            }

        return playgroundMatrix;
    }

    public void setData(char[][] data) {
        this.playgroundMatrix = data;
    }

}