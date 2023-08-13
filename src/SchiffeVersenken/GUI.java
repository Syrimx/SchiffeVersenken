package SchiffeVersenken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.security.KeyPair;
import java.util.List;

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
    private JPanel enemyPanel;
    private JPanel playerPanel;
    private JLabel instructionLabel;
    private JLabel instructionshipLabel;
    private JLabel playerTitelLabel;
    private JLabel enemyTitelLabel;
    private JPanel mainPanel;
    private JMenuBar menubar;
    private JMenu menuGame;
    private JMenuItem backtoMenu;
    private JButton[][] playerButtons;
    private JButton[][] enemyButtons;
    private JButton[] shipTypeButtons; //Länge auswählen
    private JPanel shipSelectionPanel;
    private JLabel[] shipCountLabels;
    //
    //für Übergabe an Controller
    private int[] currentposition = new int[2];
    private char[][] enemyMatrix;
    private char[][] playerMatrix;
  
    ////
    //Schiffe setzen 
    
    private int shipLength = 0;
    private int tmpshipLength = 0;
    private int startRow = -1;
    private int startCol = -1;
    private int endRow = -1;
    private int endCol = -1;
    ////
    
    public GUI(MenuWindow menuWindow) {
    	this.menuWindow = menuWindow;
    	//Datenmodel in View eingebunden, sodass Daten einfacher aufrufbar sind
        this.model = new DataModel();
    	
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
		instructionPanel.setLayout(new GridLayout(2, 1));
		add(instructionPanel,BorderLayout.NORTH);
		
		
		playerPanel = new JPanel(new GridLayout(10,10));
		playerPanel.setBackground(Color.WHITE);
		
		enemyPanel = new JPanel(new GridLayout(10,10));
		enemyPanel.setBackground(Color.BLACK);
		
		//Felder erstellen für playerPanel und enemyPanel
		drawMap();
		
		menubar = new JMenuBar();
		menuGame = new JMenu("Menü");
		backtoMenu = new JMenuItem("Spiel beenden");
		menubar.add(menuGame);
		menuGame.add(backtoMenu);
		this.setJMenuBar(menubar);
		backtoMenu.addActionListener(e -> menuWindow.backToMenu());
		 
		
		instructionLabel = new JLabel();
		instructionLabel.setText("Platziere die Schiffe!");
		instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionLabel.setVerticalAlignment(SwingConstants.CENTER);
		instructionPanel.add(instructionLabel);
		
		instructionshipLabel = new JLabel();
		instructionshipLabel.setLayout(new GridBagLayout()); 
		
		instructionshipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionshipLabel.setVerticalAlignment(SwingConstants.TOP);
		instructionPanel.add(instructionshipLabel);
		
		 playerTitelLabel = new JLabel("Player Spielfeld", SwingConstants.CENTER);
    	 enemyTitelLabel = new JLabel("Enemy Spielfeld", SwingConstants.CENTER);
	  	 instructionLabel.add(playerTitelLabel, BorderLayout.EAST);
		 instructionLabel.add(enemyTitelLabel, BorderLayout.WEST);
		
		//Auswahl Buttons für Schiffart
		setupShipSelectionButtons();
    	
    	setVisible(true);
    }

  
    //Buttonfelder und x-und y-Achsen Beschriftung erstellen
    public void drawMap() {    
    	// Erstelle panels für das player Feld, x-achse labels, and y-achse labels
    	JPanel playerXAxisLabelPanel = new JPanel(new GridLayout(1, 10));
    	JPanel playerYAxisLabelPanel = new JPanel(new GridLayout(10, 1));
    	JPanel labeledPlayerPanel = createLabeledPanel(playerPanel, playerXAxisLabelPanel, playerYAxisLabelPanel, Color.WHITE);

    	// Erstelle panels für das enemy Feld, x-Achse labels, and y-Achse labels
    	JPanel enemyXAxisLabelPanel = new JPanel(new GridLayout(1, 10));
    	JPanel enemyYAxisLabelPanel = new JPanel(new GridLayout(10, 1));
    	JPanel labeledEnemyPanel = createLabeledPanel(enemyPanel, enemyXAxisLabelPanel, enemyYAxisLabelPanel, Color.BLACK);
    	
        addLabeledPanelToMainPanel(labeledEnemyPanel, 0, 0);
        addLabeledPanelToMainPanel(labeledPlayerPanel, 1, 0);
        
        // Erstelle player/enemy buttons und labels
        addButtonstoPanel();

	    addAxisLabels(playerYAxisLabelPanel, playerXAxisLabelPanel, Color.BLACK);
	    addAxisLabels(enemyYAxisLabelPanel, enemyXAxisLabelPanel, Color.WHITE);
    }
    
    //Hilfsfunktionen für drawMap:
    private void addLabeledPanelToMainPanel(JPanel labeledPanel, int gridX, int gridY) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.gridx = gridX;
        gbc.gridy = gridY;
        gbc.insets = new Insets(30, 30, 30, 30);
        mainPanel.add(labeledPanel, gbc);
    }

    private JPanel createLabeledPanel(JPanel panel, JPanel xAxisLabelPanel, JPanel yAxisLabelPanel, Color background) {
        JPanel labeledPanel = new JPanel(new BorderLayout());
        labeledPanel.add(xAxisLabelPanel, BorderLayout.NORTH);
        labeledPanel.add(yAxisLabelPanel, BorderLayout.WEST);
        labeledPanel.add(panel, BorderLayout.CENTER);

        xAxisLabelPanel.setBackground(background);
        yAxisLabelPanel.setBackground(background);
        return labeledPanel;
    }
    
    private void addButtonstoPanel() {
    	// Erstelle player/enemy buttons und labels
        playerButtons = new JButton[10][10];
        enemyButtons = new JButton[10][10];

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                playerButtons[row][col] = new JButton();
                playerButtons[row][col].setPreferredSize(new Dimension(40, 40)); // Größe festlegen
                playerButtons[row][col].setBackground(Color.BLUE);

                final int finalRow = row; // temporäre final Variablen
                final int finalCol = col;

                // Aktion playerButton
                playerButtons[row][col].addActionListener(e -> onPlayerButtonClicked(finalRow, finalCol));

                playerPanel.add(playerButtons[row][col]);
                
                enemyButtons[row][col] = new JButton();
                enemyButtons[row][col].setPreferredSize(new Dimension(40, 40)); // Größe festlegen
                enemyButtons[row][col].setBackground(Color.BLUE);

                // Aktion enemyButton
                enemyButtons[row][col].addActionListener(e -> onEnemyButtonClicked(finalRow, finalCol));

                enemyPanel.add(enemyButtons[row][col]);
            }
        }
    }
    
    private void addAxisLabels(JPanel yAxisLabelPanel, JPanel xAxisLabelPanel, Color foreground) {
        for (int i = 0; i < 10; i++) {
            char yLabel = (char) ('A' + i);
          
            JLabel yAxisLabelForeground = new JLabel(String.valueOf(yLabel), SwingConstants.CENTER);
            yAxisLabelForeground.setForeground(foreground); 
            yAxisLabelPanel.add(yAxisLabelForeground);
            
            String xLabel = String.valueOf(i + 1);
          
            JLabel xAxisLabelForeground = new JLabel(String.valueOf(xLabel), SwingConstants.CENTER);
            xAxisLabelForeground.setForeground(foreground); 
            xAxisLabelPanel.add(xAxisLabelForeground);
        }
    }

 
    
    private void setupShipSelectionButtons() {
        String[] shipTypes = model.getShipTypes();
        int[] shipCounts = model.getShipCounts();
        
        instructionshipLabel.setText("Wähle ein Schiffstyp aus!");
        
        shipSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        instructionshipLabel.add(shipSelectionPanel);

        shipTypeButtons = new JButton[shipTypes.length];
        shipCountLabels = new JLabel[shipTypes.length]; 

        for (int i = 0; i < shipTypes.length; i++) {
            final int shipIndex = i;
            shipTypeButtons[i] = new JButton(shipTypes[i]);
            shipTypeButtons[i].setPreferredSize(new Dimension(100, 40));
            shipSelectionPanel.add(shipTypeButtons[i]);
            shipTypeButtons[i].addActionListener(e -> onShipTypeButtonClicked(shipTypes[shipIndex]));
            
            shipCountLabels[i] = new JLabel("Anzahl: " + shipCounts[i]); 
            shipSelectionPanel.add(shipCountLabels[i]);
        }
    }
   
    private void updateShipCountLabels(){
    	String[] shipTypes = model.getShipTypes();
        int[] shipCounts = model.getShipCounts();
    	for (int i = 0; i < shipTypes.length; i++) {
    		shipCountLabels[i] = new JLabel("Anzahl: " + shipCounts[i]);
    	}
    }
    
    
    //Aktion: Auswahl Schiff, speicher die Länge
    private void onShipTypeButtonClicked(String shipType) {
    	switch(shipType) {
    	case "Submarine":
    		shipLength= 1;
    		break;
    	case "Frigate":
    		shipLength=2;
    		break;
    	case "Cruiser":
    		shipLength=3;
    		break;
    	case "Battleship":
    		shipLength=4;
    		break;
    	default:
    		shipLength=0;
    	break;
    	}
    }
    
    
    private void SelectionButtonsEnabled() {
    	int[] shipCounts = model.getShipCounts();
    	for(int i = 0; i < 4; i++) {
    		if(shipCounts[i]== 0) {
    			shipTypeButtons[i].setEnabled(false);
    		}
    	}
    }
    
  ///Aktionen   
//Aktionen in der Schiffe setzen Phase, Spieler darf nur eigenes Feld manipulieren
    private void onPlayerButtonClicked(int row, int col) {
    	if(model.isPhaseOne() && !model.alleSpielerSchiffePlatziert()) 
        	{
    		if (shipLength == 0) {
                instructionshipLabel.setText("Wähle einen Schiffstyp aus!");
            } if(playerButtons[row][col].getBackground() == Color.YELLOW) {
            	instructionshipLabel.setText("Auf diesem Feld steht bereits ein Schiff! Wähle ein anderes aus!");	
        	} else if (startRow == -1 && startCol == -1 || tmpshipLength != shipLength) {
                // Speichern der Startposition
            	refreshMap(); 
                startRow = row;
                startCol = col; 
                tmpshipLength = shipLength; //temporär gespeichert, zuständig zur Möglichkeit 
                //rumzuklicken bis man was wirklich setzen will
                playerButtons[startRow][startCol].setBackground(Color.CYAN); //Startposition markieren
           
                instructionshipLabel.setText("Wähle die Endposition aus!");
                //Markierung der möglichen Endpositionen
                markPossibleEndPositions(startRow, startCol);
            } else if (endRow == -1 && endCol == -1 && playerButtons[row][col].getBackground() == Color.GREEN) {
                // Speichern der Endposition
                endRow = row;
                endCol = col;
                // Schiffplatzierung 
                model.placeShips(startRow, startCol, endRow, endCol); 
                // Start- und Endpositionen zurücksetzen
                startRow = -1;
                startCol = -1; 
                endRow = -1;
                endCol = -1;
                //Schiffslänge zurücksetzen
                shipLength = 0;
                //Anzeige der Anzahl Schiffe Aktualisieren
                updateShipCountLabels();
                //Aktualisierung Status, holt Status von DataModel
                refreshMap(); 
                
                instructionshipLabel.setText("");
                SelectionButtonsEnabled();
   
                //Wechsel zur Phase 2
                if(model.alleSpielerSchiffePlatziert()) {
                	model.setPhase(2);
                	instructionLabel.setText("Schießen Sie auf das gegnerische Feld!");
                }
            }
        }
    }
    
    //Markierung der möglichen Endpositionen
    private void markPossibleEndPositions(int startRow, int startCol) {
        if (shipLength != 0) {
            int[][] offsets = {
                {1, 0}, {-1, 0}, {0, 1}, {0, -1}
            };

            for (int[] offset : offsets) {
                int endRow = startRow + offset[0] * (shipLength - 1); //Zeilenverschiebung
                int endCol = startCol + offset[1] * (shipLength - 1); //Spaltenverschiebung
                if (isValidPosition(endRow, endCol) && !model.checkCollision(startRow, startCol, endRow, endCol)) {
                    markPossiblePosition(endRow, endCol);
                }
            }
        } else {
            instructionshipLabel.setText("Wähle zuerst ein Schiffstyp aus!");
        }
    }

    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10;
    }

    private void markPossiblePosition(int row, int col) {
        playerButtons[row][col].setBackground(Color.GREEN);
    }


    
    ///////
    //Aktionen in der Schießen Phase, Spieler darf nur Gegner Feld manipulieren
    private void onEnemyButtonClicked(int row, int col) {
    	//Schießen Phase, Phase 2
    	if(model.isPhaseOne()== false) {
    	char enemyStatus = model.getEnemyCellStatus(row, col);
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
    	}else {
    		//Phase 1 vorhanden
    	}
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

    
    public void refreshMap() 
    {
    	for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				char playerStatus = model.getPlaygroundCellStatus(row, col); //hole Status von Datamodel
		        char enemyStatus = model.getEnemyCellStatus(row, col);
		        
				setButtonStatus(playerButtons[row][col],playerStatus);
				setButtonStatus(enemyButtons[row][col],enemyStatus);
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
