package SchiffeVersenken;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

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

public class GameWindow extends JFrame
{
    private MenuWindow menuWindow;
    private JButton backtoMenuButton;
    /* Eigenschaften */
    private DataModel model;
    
    public GameWindow(MenuWindow menuWindow) {
    	this.menuWindow = menuWindow;
    	//Datenmodel in View eingebunden, sodass Daten einfacher aufrufbar sind
        this.model = new DataModel();
    	
    	this.setLayout(null);
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setLocation(200, 200);
		this.setSize(800, 600); 
		
    	backtoMenuButton = new JButton("Zurueck zum Menue");
    	backtoMenuButton.addActionListener(e -> menuWindow.backToMenu());
    	backtoMenuButton.setBounds(100, 5, 110, 30);
    	this.add(backtoMenuButton);
    	setVisible(true);
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
