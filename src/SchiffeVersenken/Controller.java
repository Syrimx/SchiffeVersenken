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
 *  public void sendData(int[][] data) {
    public int[][] listenAndReceive() {

*/

public class Controller implements Server {
    /* Eigenschaften */
    private HashMap<String, int[][]> gameState = null;

    public Controller() {
        gameState = new HashMap<String, int[][]>();
    }

    /*Controller Methoden */
    public void gameLoop() {

    }

    public void preparationPhase() {

    }

    public void endPhase() {

    }

    public void setGameState() {

    }


    /*Server Methoden*/
    @Override
    public int[][] listenAndReceive() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'listenAndReceive'");
    }

    @Override
    public void sendData(int[][] data) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'sendData'");
    }

}

