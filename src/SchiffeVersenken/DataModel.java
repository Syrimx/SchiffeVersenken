package SchiffeVersenken;

/* Model -> Client
 * |-> Spieler sendet Daten an das Model
 * 
 * Methoden:
    public int[][] getData() 
    public void setData(int[][] data) 
    public int[][] connectToServerAndReceive() 
    public void sendData(int[][] data)

 * 
*/

public class DataModel implements Client {
    private int[][] playgroundMatrix    = null; //Wie groß darf sie werden ?
    private int[] fireLocation          = null; //gewonnen durch GUI.getCurrentLocation

    /* DataModel Methoden */
    public int[][] getData() {
        return playgroundMatrix;
    }

    public void setData(int[][] data) {
        this.playgroundMatrix = data;
    }

    /* Client Methoden */
    @Override
    public int[][] connectToServerAndReceive() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connectToServerAndReceiveData'");
    }

    @Override
    public void sendData() {
        //send fireLocation
    }

}