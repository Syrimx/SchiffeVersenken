package SchiffeVersenken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/* View
	1)
 * Action Listener für Buttons Implementieren
 * -> Rückgabe position auf dem Feld -> Datentyp ? 
 * -> Datamodel datentyp char[][]
 * 
 * [		Zeilen
 *  [x,x,x] A[0][0-2]
 *  [x,x,x] A[1][0-2]
 *  [x,x,x] A[2][0-2]
 *
 * ]
 * 
 * Spalten	 0
 * A[0-2][0] x
 * 			 x
 * 			 x
 * 
 * 			  1
 * A[0-2][1]  x
 * 			  x
 * 			  x
 * 
 * 			   2
 * A[0-2][2]   x
 * 			   x
 * 			   x
 * 
 * 2)
 * Einbindung des akuallisierten Feldes -> resresh(DataModel.playgroundMatrix)
 *  
 * 
 * Hello 
 */
//GameWindow
public class GUI extends JFrame
{
    private MenuWindow menuWindow;
    private JPanel instructionPanel;
    private JPanel playerLabelPanel;
    private JPanel enemyLabelPanel;
    private JPanel enemyPanel;
    private JLabel instructionLabel;
    private JPanel mainPanel;
    private JMenuBar menubar;
    private JMenu menuGame;
    private JMenuItem backtoMenu;
    private JButton[][] playerButtons;
    private JButton[][] enemyButtons;
    private JLabel xAxisLabeling;
    private JLabel yAxisLabeling;
    private DataModel model;
    
    public GUI(MenuWindow menuWindow) {
    	this.menuWindow = menuWindow;
    	//Datenmodel in View eingebunden, sodass Daten einfacher aufrufbar sind
        //this.model = new DataModel();
    	
    	this.setLayout(new BorderLayout());
    	this.setTitle("Schiffe versenken");
		this.getContentPane().setBackground(Color.LIGHT_GRAY);
		this.setLocation(200, 200);
		this.setSize(900, 700); 
		
		mainPanel = new JPanel(new GridBagLayout());
		mainPanel.setBackground(Color.LIGHT_GRAY);
		add(mainPanel,BorderLayout.CENTER);
		
		instructionPanel = new JPanel();
		instructionPanel.setPreferredSize(new Dimension(900,200));
		instructionPanel.setBackground(Color.LIGHT_GRAY);
		add(instructionPanel,BorderLayout.NORTH);
		
		playerPanel = new JPanel(new GridLayout(10,10));
		playerPanel.setBackground(Color.WHITE);
		
		playerLabelPanel = new JPanel(new GridLayout(11,1));
		playerLabelPanel.setBackground(Color.white);
		
		enemyPanel = new JPanel(new GridLayout(10,10));
		enemyPanel.setBackground(Color.DARK_GRAY);
		
		enemyLabelPanel = new JPanel(new GridLayout(11,1));
		enemyLabelPanel.setBackground(Color.black);
		
		GridBagConstraints gbc = new GridBagConstraints();
	    //Komponenten werden horizontal &vertikal gestreckt und nehmen den verfügbaren Paltz ein
	    gbc.fill = GridBagConstraints.BOTH;
	    //Gewicht der Komponenten in vertikaler und horizontaler Richtung,nehmen gleich stark Platz ein
	    gbc.weightx = 1.0;
	    gbc.weighty = 1.0;

	    //Festlegung der Position
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    
	    //Abstand um die Panels
	    gbc.insets = new Insets(40, 40, 40, 40); // Lücke um die Panels herum
	    mainPanel.add(enemyPanel, gbc);

	    gbc.gridx = 1;
	    mainPanel.add(playerPanel, gbc);
		
	    ////////
		
		//Beschriftung für x-Achse im playerPanel und enemyPanel
		for(int col = 0; col < 10; col++) {
			String xlabel = String.valueOf(col +1);
			xAxisLabeling = new JLabel(xlabel, SwingConstants.CENTER);
			playerLabelPanel.add(xAxisLabeling);
			enemyLabelPanel.add(xAxisLabeling);
		}
		
		//Beschriftung für y-Achse im playerPanel und enemyPanel
		for(int row = 0; row < 10; row++) {
			char ylabel = (char)('A' + row);
			yAxisLabeling = new JLabel(String.valueOf(ylabel), SwingConstants.CENTER);
			playerLabelPanel.add(yAxisLabeling);
			enemyLabelPanel.add(yAxisLabeling);
		}
		
		///////
		
		//Felder erstellen für playerPanel und enemyPanel
		playerButtons = new JButton[10][10];
		enemyButtons = new JButton[10][10];
		
		for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				playerButtons[row][col] = new JButton();
				playerButtons[row][col].setPreferredSize(new Dimension(40,40)); //Größe festlegen
				playerButtons[row][col].setBackground(Color.BLUE);
				playerPanel.add(playerButtons[row][col]);
				
				enemyButtons[row][col] = new JButton();
				enemyButtons[row][col].setPreferredSize(new Dimension(40,40)); //Größe festlegen
				enemyButtons[row][col].setBackground(Color.BLUE);
				enemyPanel.add(enemyButtons[row][col]);
			}
		}
		
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

    public Object getCurrentPosition() {
        return null;
    }
}
