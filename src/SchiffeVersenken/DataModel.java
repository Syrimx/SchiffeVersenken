package SchiffeVersenken;

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
        return playgroundMatrix;
    }

    public void setData(int[][] data) {
        this.playgroundMatrix = data;
    }

}