package SchiffeVersenken;

/* View
 * |-> bezieht Matrix von Model
 * 
 * Eigenschaften:
 *  model -> DataModel
 *  
 * 
 * Methoden:
 *  DrawMap()
 *  RefreshMap()
 *  getCurrentPosition() -> userinput
 * 
 */

public class GUI {
    /* Eigenschaften */
    private DataModel model  = null;

    public GUI() {
        //Datenmodel in View eingebunden, sodass Daten einfacher aufrufbar sind
        this.model = new DataModel();
    }


    /*Methoden*/
    public void drawMap() {

    }

    public void refreshMap() {

    }

    public int[] getCurrentPosition() {
        return null;
    }
}

