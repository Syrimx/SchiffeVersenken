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
    private int[][] playgroundMatrix    = null; //Wie groß darf sie werden ?
    private int[] fireLocation          = null; //gewonnen durch GUI.getCurrentLocation

    /* DataModel Methoden */
    public int[][] getData() {

        Scanner sc = new Scanner(System.in);
        
        System.out.print("Wie lang soll das Spielfeld sein?");
         int rows = sc.nextInt();

        System.out.print("Und wie breit soll das Spielfeld werden?");
         int columns = sc.nextInt();

        int [][] playgroundMatrix = new int [rows][columns];        //User kann bestimmen, wie groß das Spielfeld sein soll, muss hier bereits Eingabe erfolgen oder eher in der Gui? Nochmal recherchieren!

        return playgroundMatrix;
    }

    public void setData(int[][] data) {
        this.playgroundMatrix = data;
    }

}