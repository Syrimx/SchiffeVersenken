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
    private JPanel playerLabelPanel;
    private JPanel enemyLabelPanel;
    private JPanel enemyPanel;
    private JPanel playerPanel;
    private JLabel instructionLabel;
    private JLabel instructionshipLabel;
    private JPanel mainPanel;
    private JMenuBar menubar;
    private JMenu menuGame;
    private JMenuItem backtoMenu;
    private JButton[][] playerButtons;
    private JButton[][] enemyButtons;
    private JButton[] shipType; //Länge auswählen
    private JLabel xAxisLabeling;
    private JLabel yAxisLabeling;
    private int shipLength;
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
		 
		
		instructionLabel = new JLabel();
		instructionLabel.setText("Platziere die Schiffe!");
		instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionLabel.setVerticalAlignment(SwingConstants.CENTER);
		instructionPanel.add(instructionLabel);
		//if case 1 (Schiffe setzen) Phase oder 2 (Angriff)
		
		
		instructionshipLabel = new JLabel();
		///
		instructionshipLabel.setLayout(new GridBagLayout()); // Verwende ein GridBagLayout
		///
		instructionshipLabel.setHorizontalAlignment(SwingConstants.CENTER);
		instructionshipLabel.setVerticalAlignment(SwingConstants.TOP);
		instructionPanel.add(instructionshipLabel);
		//Auswahl Buttons für Schiffart
		setupShipSelectionButtons();
    	
    	setVisible(true);
    }

    
    //Button Array Feld zeichnen
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
    
    
    //Buttons für Auswahl für Schifftyp
    private void setupShipSelectionButtons() {
        String[] shipTypes = model.getShipTypes();
        int[] shipCounts = model.getShipCounts();

        JPanel shipSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Zentriert, mit Abstand
        instructionshipLabel.add(shipSelectionPanel);

        for (int i = 0; i < shipTypes.length; i++) {
        	final int shipIndex = i; // Deklariere eine final Variable, um i zu speichern
            JButton shipTypeButton = new JButton(shipTypes[i]);
            shipTypeButton.setPreferredSize(new Dimension(100, 40)); // Größe der Buttons anpassen
            shipSelectionPanel.add(shipTypeButton);
            shipTypeButton.addActionListener(e -> onShipTypeButtonClicked(shipTypes[shipIndex]));
            
            JLabel shipCountLabel = new JLabel("Anzahl: " + shipCounts[i]);
            shipSelectionPanel.add(shipCountLabel);
        }
    }


    //Aktion: Auswahl Schiff, speicher die Länge
    private void onShipTypeButtonClicked(String shipType) {
    	switch(shipType) {
    	case "submarine":
    		shipLength= 1;
    		System.out.println(shipLength);
    		break;
    	case "frigate":
    		shipLength=2;
    		System.out.println(shipLength); //Problem: shipLength verändert sich nicht
    		break;
    	case "cruiser":
    		shipLength=3;
    		System.out.println(shipLength);
    		break;
    	case "battleship":
    		shipLength=4;
    		System.out.println(shipLength);
    		break;
    	default:
    		shipLength=0;
    		System.out.println(shipLength);
    	break;
    	}
    }
    
    
////!!
    //Anweisung für  die jeweilige Phasen Ausgabe !!
    private String WritePhaseInstruction() 
    {
    	return "blahblah";
    }
////!!
    
    
    private void onPlayerButtonClicked(int row, int col) {
    markPossibleEndPositions(row, col);
    
    }

///Aktionen    
//    //Aktionen in der Schiffe setzen Phase, Spieler darf nur eigenes Feld manipulieren
//    private void onPlayerButtonClicked(int row, int col) {
//    	//Schiffe setzen, Phase 1
//    	if(model.isPhaseOne() && !model.alleSpielerSchiffePlatziert()) 
//    	{
//    		// Überprüfe, ob die Auswahl gültig ist (z. B. nicht bereits belegt)
//            // ...
//
//            // Zeige mögliche Endpositionen grün an oder ungültige Positionen rot
//            // ...
//
//            // Rufe model.placeShips auf und verarbeite das Ergebnis
//            // ...
//
//            // Passe den Button-Status an (deaktiviere oder ändere Farbe)
//    		  if (shipPlaced) {
//                   // Aktualisiere den Button-Status in der GUI
//                   playerButtons[row][col].setBackground(Color.YELLOW); // Zeige platziertes Schiff an
//                   //playerButtons[][].setEnabled(false); // Deaktiviere Startposition
//               } 
//        	
//        	
//    		
//            
//         
//        	
//        	model.placeShips(row, col, Endrow, Endcol);
//        	//Startposition 
//        	playerButtons[row][col].setEnabled(false); //Button nicht funktionstüchtig
//        	//Kreuze grün,Buttons setEnable(false) bei Auswahl, Möglichkeiten grün anzeigen , je nachdem Schiff vorhanden?
//    		
//    		
//    		
//    	}else {
//    		
//    	}
//   }
//    
//    /////////////
//    private void onPlayerButtonClicked(int row, int col) {
//        if (model.isPhaseOne() && !model.alleSpielerSchiffePlatziert()) {
//            // Überprüfe, ob die Auswahl gültig ist (z. B. nicht bereits belegt)
//            if (model.isValidPlacement(row, col, shipLength)) {
//                // Berechne die möglichen Endpositionen für das Schiff
//                List<int[]> possibleEndPositions = model.calculatePossibleEndPositions(row, col, shipLength);
//
//                // Markiere die möglichen Endpositionen auf der GUI
//                markPossibleEndPositions(possibleEndPositions);
//
//                // Warte auf Auswahl einer Endposition durch den Spieler
//                addEndButtonListener((endRow, endCol) -> {
//                    // Deaktiviere Buttons für platziertes Schiff
//                    disablePlacedShipButtons(row, col, endRow, endCol);
//
//                    // Platziere das Schiff im DataModel
//                    boolean shipPlaced = model.placeShips(row, col, endRow, endCol);
//
//                    if (shipPlaced) {
//                        // Aktualisiere den Button-Status in der GUI
//                        gui.updatePlayerButtonStatus(row, col, endRow, endCol, shipLength);
//                        // Setze die Phase auf 2 (Angriffsphase)
//                        model.setPhase(2);
//                        // Wechsle zur Angriffsphase in der GUI (falls erforderlich)
//                        // ...
//                    } else {
//                        // Zeige Fehlermeldung oder ändere Button-Farbe, falls das Schiff nicht platziert werden kann
//                        // ...
//                    }
//
//                    // Entferne den Listener für die Endpositionsauswahl
//                    gui.removeEndButtonListener();
//                    // Setze mögliche Endpositionen zurück
//                    gui.resetPossibleEndPositions();
//                });
//                
//            } else {
//                // Zeige Fehlermeldung oder ändere Button-Farbe für ungültige Auswahl
//                // ...
//            }
//        }
//    }

    
    ////////////
    
    //Markierung der möglichen Endpositionen
    private void markPossibleEndPositions(int row, int col) {
    	if(shipLength != 0) {
    		switch(shipLength) {
    		case '1':
    			markPossiblePosition(row, col);
    			break;
    		case '2':
	    		markPossiblePosition(row+1, col);
	    		markPossiblePosition(row-1, col);
	    		markPossiblePosition(row, col+1);
	    		markPossiblePosition(row, col-1);
    			break;
    		case '3':
    			markPossiblePosition(row+2, col);
	    		markPossiblePosition(row-2, col);
	    		markPossiblePosition(row, col+2);
	    		markPossiblePosition(row, col+2);
    			break;
    		case '4':
    			markPossiblePosition(row+3, col);
	    		markPossiblePosition(row-3, col);
	    		markPossiblePosition(row, col+3);
	    		markPossiblePosition(row, col-3);
    			break;
    		default:
    			
    			break;
    		}
            
    	}else {
    		instructionshipLabel.setText("Wähle zuerst ein Schiffstyp aus!");
    	}
    }
    
    //nur markieren wenn auch im Feld vorhanden und nicht bereits besetzt
    private void markPossiblePosition(int row, int col) {
        if (row >= 0 && row < 10 && col >= 0 && col < 10) {
        	if (playerButtons[row][col].isEnabled()) {
        		
        		playerButtons[row][col].setBackground(Color.GREEN);
        	}
        }
    } 


    
    
    
    
    //Nachdem Setzen eines Schiffes: Felder nicht mehr auswählbar
    private void disablePlacedShipButtons(int startRow, int startCol, int endRow, int endCol) {
        //Schleife durch die betroffenen Buttons und deaktiviere sie
        for (int row = startRow; row <= endRow; row++) {
            for (int col = startCol; col <= endCol; col++) {
                playerButtons[row][col].setEnabled(false);
            }
        }
        shipLength=0;
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
    
    
    
    //ersetzt durch setShipsSelectionButtons
//  //Schiff Typen und Anzahl Ausgabe
//  private String WriteShipInfo() {
//      StringBuilder shipInfo = new StringBuilder(); // Verwende StringBuilder, um Text zusammenzustellen
//      int[] shipCounts = model.getShipCounts();
//      String[] shipNames = model.getShipTypes();
//      
//      for (int i = 0; i < shipNames.length; i++) {
//          shipInfo.append(shipNames[i]).append(": ").append(shipCounts[i]);
//          
//          // Füge eine Trennzeichen nach jedem Eintrag hinzu, außer beim letzten Eintrag
//          if (i < shipNames.length - 1) {
//              shipInfo.append(", ");
//          }
//      }
//
//      return shipInfo.toString(); // Gib den zusammengestellten Text zurück
//  }
    
}
