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

*/

public class Controller {
    /* Eigenschaften */
    private HashMap<String, int[][]> gameState = null;
    private int roundToken = 0; 

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

}

