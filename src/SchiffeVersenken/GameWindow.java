package SchiffeVersenken;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;

public class GameWindow extends JFrame
{
	//private DataModel data;
    private MenuWindow menuWindow;
    private JButton backtoMenuButton;
    
    public GameWindow(MenuWindow menuWindow) {
    	this.menuWindow = menuWindow;
//    	this.data = d;
    	
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
}
