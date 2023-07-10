package SchiffeVersenken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

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
//GameWindow
public class GUI extends JFrame
{
    private MenuWindow menuWindow;
    private JPanel instructionPanel;
    private JPanel playerPanel;
    private JPanel enemyPanel;
    private JLabel instructionLabel;
    private JMenuBar menubar;
    private JMenu menuGame;
    private JMenuItem backtoMenu;
    private DataModel model;
    
    public GUI(MenuWindow menuWindow) {
    	this.menuWindow = menuWindow;
    	//Datenmodel in View eingebunden, sodass Daten einfacher aufrufbar sind
        this.model = new DataModel();
    	
    	this.setLayout(new BorderLayout());
    	this.setTitle("Schiffe versenken");
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setLocation(200, 200);
		this.setSize(800, 600); 
		
		instructionPanel = new JPanel();
		instructionPanel.setPreferredSize(new Dimension(800,50));
		instructionPanel.setBackground(Color.LIGHT_GRAY);
		add(instructionPanel,BorderLayout.NORTH);
		
		enemyPanel = new JPanel(new GridLayout(10,10));
		enemyPanel.setPreferredSize(new Dimension(400,550)); //Mindestgroess für playerPanel
		enemyPanel.setBackground(Color.DARK_GRAY);
		add(enemyPanel, BorderLayout.WEST);
		
		playerPanel = new JPanel(new GridLayout(10,10));
		playerPanel.setPreferredSize(new Dimension(400,550));
		playerPanel.setBackground(Color.WHITE);
		add(playerPanel, BorderLayout.EAST);
		
		menubar = new JMenuBar();
		menuGame = new JMenu("Menü");
		backtoMenu = new JMenuItem("Spiel beenden");
		menubar.add(menuGame);
		menuGame.add(backtoMenu);
		this.setJMenuBar(menubar);
		backtoMenu.addActionListener(e -> menuWindow.backToMenu());
		
		instructionLabel = new JLabel("Platziere deine Schiffe!");
		instructionLabel.setBounds(200, 5, 300, 40);
		instructionPanel.add(instructionLabel);
    	
    	
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
