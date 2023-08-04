package SchiffeVersenken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.security.KeyPair;

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
 *  [x,!,x] A[0][0-2] ! = int[0,1]
 *  [x,x,?] A[1][0-2] ? = int[1][2]
 *  [x,*,x] A[2][0-2] * = int[2][1]
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
 * Hello Ronja
 */

//Anzeige Phasen, Schiffe setze Phase: wie viele Schiffe noch vorhanden?, Schieße Phase und Pause Phase
//GameWindow
public class GUI extends JFrame
{
	private DataModel model;
	
    private MenuWindow menuWindow;
    private JPanel instructionPanel;
    private JPanel playerLabelPanel;
    private JPanel enemyLabelPanel;
    private JPanel enemyPanel;
    private JPanel playerPanel;
    private JLabel instructionLabel;
    private JPanel mainPanel;
    private JMenuBar menubar;
    private JMenu menuGame;
    private JMenuItem backtoMenu;
    private JButton[][] playerButtons;
    private JButton[][] enemyButtons;
    private JLabel xAxisLabeling;
    private JLabel yAxisLabeling;
    //
    //für Übergabe an Controller
    private int[] currentposition = new int[2];
    private char[][] enemyMatrix;
    private char[][] playerMatrix;
    private char playerStatus;
    private char enemyStatus;
    
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
		
	    ////////!!!!
		
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
		
		///////!!!!
		
		//Felder erstellen für playerPanel und enemyPanel
		drawMap();
		
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
    public void drawMap() 
    {
		//Felder erstellen für playerPanel und enemyPanel
		playerButtons = new JButton[10][10];
		enemyButtons = new JButton[10][10];
		
		for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				playerButtons[row][col] = new JButton();
				playerButtons[row][col].setPreferredSize(new Dimension(40,40)); //Größe festlegen
				playerButtons[row][col].setBackground(Color.BLUE);
				
				final int finalRow = row; // temporäre final Variablen 
	            final int finalCol = col;
	            
				//Aktion Button
				playerButtons[row][col].addActionListener(e -> onPlayerButtonClicked(finalRow, finalCol));
				
				playerPanel.add(playerButtons[row][col]);
				
				enemyButtons[row][col] = new JButton();
				enemyButtons[row][col].setPreferredSize(new Dimension(40,40)); //Größe festlegen
				enemyButtons[row][col].setBackground(Color.BLUE);
				//Aktion Button
				enemyButtons[row][col].addActionListener(e -> onEnemyButtonClicked(finalRow, finalCol));

				enemyPanel.add(enemyButtons[row][col]);
			}
		}
    }
    
    
    //JButtons[][] in char[][] konvertieren
    public char[][] convertButtonsToCharArray(JButton[][] buttons) {
        char[][] charArray = new char[10][10];
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                JButton button = buttons[row][col];
                // Status = Wasser
                if (button.getBackground() == Color.BLUE) {
                    charArray[row][col] = 'w'; 
                // Status = Schiff
                } else if (button.getBackground() == Color.YELLOW) {
                    charArray[row][col] = 's'; 
                // Status = Schiff getroffen
                } else if (button.getBackground() == Color.RED && button.getText().equals("x")) {
                    charArray[row][col] = 'x'; 
                // Status = Wasser getroffen, bekanntes Feld
                } else if (button.getBackground() == Color.LIGHT_GRAY && button.getText().equals("o")) {
                    charArray[row][col] = 'b'; 
                // Unbekannter Status oder leeres Feld
                } else {
                    charArray[row][col] = ' '; 
                }
            }
        }
        return charArray;
    }

///Aktionen    
    //Aktionen in der Schiffe setzen Phase, Spieler darf nur eigenes Feld manipulieren
    private void onPlayerButtonClicked(int row, int col) {
    	//Schiffe setzen
    	
   }
    
    //Aktionen in der Schießen Phase, Spieler darf nur Gegner Feld manipulieren
    private void onEnemyButtonClicked(int row, int col) {
    	//Schießen Phase
    	char enemyStatus = model.getEnemyStatus(row, col);
    	switch (enemyStatus) {
        case 'w': // schieße auf Wasser 
                    enemyButtons[row][col].setBackground(Color.LIGHT_GRAY);
                    enemyButtons[row][col].setText("o");
            break;
        case 's': // schieße auf Schiff
        			enemyButtons[row][col].setBackground(Color.RED);
        			enemyButtons[row][col].setText("x");  
            break;
        case 'x': // Scieße auf totes Schiff
                    //Passiert nichts
            break;
        case 'b': // schieße auf bekanntes Wasser Feld
	                //Passiert nichts
            break;
        default: // unbekannter Status
                    enemyButtons[row][col].setText("???");
            break;
    	}   
        //Aktuelle Position speichern im currentposition Array
    	currentposition[0]= row;
    	currentposition[1]= col;
   	
    	
    }
    
    
//Aktualisierung
    //Aktuallisierung des Status der enemy Map
    //Aktuallisierung des Status der player Map
    private void setButtonStatus(JButton Button, char status) {
        switch (status) {
            case 'w': // Wasser
                        Button.setBackground(Color.BLUE);
                        Button.setText("");
                break;
            case 's': // Schiff
                        Button.setBackground(Color.YELLOW);
                        Button.setText("");
                break;
            case 'x': // Schiff getroffen
                        Button.setBackground(Color.RED);
                        Button.setText("x");
                break;
            case 'b': // Wasser getroffen, jetzt bekannt
		                Button.setBackground(Color.LIGHT_GRAY);
		                Button.setText("o");
                break;
            default: // unbekannter Status
                        Button.setBackground(Color.WHITE);
                        Button.setText("");
                break;
        }
    }
///////////
    
    public void refreshMap() 
    {
    	for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				char playerStatus = model.getPlayerStatus(row, col); //hole Status von Datamodel
		        char enemyStatus = model.getEnemyStatus(row, col);
		        
				setButtonStatus(playerButtons[row][col],playerStatus);
				setButtonStatus(enemyButtons[row][col],enemyStatus);
			}
		}
    }

   
    //aktuell geklickter Button übergeben
    public int[] getCurrentPosition() {
        return currentposition;      
    }
    
    //Enemy Matrix übergeben
    public char[][] getEnemyMatrix(){
    	enemyMatrix = convertButtonsToCharArray(enemyButtons);
		return enemyMatrix;
    	
    }
    
    //Player Matrix übergeben
    public char[][] getPlayerMatrix(){
    	playerMatrix = convertButtonsToCharArray(playerButtons);
		return playerMatrix;
    	
    }
    
    
}
