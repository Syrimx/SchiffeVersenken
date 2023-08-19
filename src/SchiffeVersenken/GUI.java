package SchiffeVersenken;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class GUI extends JFrame
{
	private DataModel datamodel;
	private Controller controller;
    
    private JPanel instructionPanel;
    private JPanel enemyPanel;
    private JPanel playerPanel;
    private JLabel instructionLabel;
    private JLabel instructionshipLabel;
    private JLabel titelmenu;
    private JLabel playerTitelLabel; //muss noch hinzugefügt werden
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

    //Menu
    private JButton playButtonBot;
    private JButton playButtonFriend;
    private JButton exitButton;
    
    public GUI() {
    	//Datenmodel in View eingebunden, sodass Daten einfacher aufrufbar sind
        this.datamodel = new DataModel();
        this.controller = new Controller(datamodel);
        
    }
    
    //Menu
    public void drawMenu() {
        this.setLayout(null);
        this.setTitle("Menue Schiffe versenken");
        this.getContentPane().setBackground(new Color(70, 95, 180));
        this.setLocation(400, 400);
        this.setSize(500, 400);

        Font titelFont = new Font("Arial", Font.BOLD, 30);
        titelmenu = new JLabel("Schiffe versenken");
        titelmenu.setBounds(125, 50, 350, 60);
        titelmenu.setFont(titelFont);
        titelmenu.setForeground(Color.WHITE);
        this.add(titelmenu);

        Font buttonFont = new Font("Arial", Font.BOLD, 13);

        playButtonBot = new JButton("Spiel gegen Bot");
        playButtonBot.setBounds(60, 150, 160, 40);
        playButtonBot.setFont(buttonFont);
        this.add(playButtonBot);
        playButtonBot.addActionListener(e -> openGameWindowBot());

        playButtonFriend = new JButton("Spiel gegen Freund");
        playButtonFriend.setBounds(280, 150, 160, 40);
        playButtonFriend.setFont(buttonFont);
        this.add(playButtonFriend);
        playButtonFriend.addActionListener(e -> openGameWindowFriend());

        exitButton = new JButton("Beenden");
        exitButton.setBounds(175, 220, 160, 40);
        exitButton.setFont(buttonFont);
        this.add(exitButton);
        exitButton.addActionListener(e -> { System.exit(0); });

        setVisible(true);
    }
    
    //Auswahl Spiel-Modus:
    private void openGameWindowBot(){
    	controller.openGameWindowBot();
    	resetGUI();
		drawMap();
    }
    
    private void openGameWindowFriend() {
    	controller.openGameWindowFriend();
    	resetGUI();
		drawMap();
    }
    
    //GUI reseten
    public void resetGUI(){
    	setVisible(false);
    	this.getContentPane().removeAll();
    }
    
    
    //Buttonfelder und x-und y-Achsen Beschriftung erstellen, 
    	public void drawMap() {    
    		this.setLayout(new BorderLayout());
        	this.setTitle("Schiffe versenken");
    		this.getContentPane().setBackground(Color.LIGHT_GRAY);
    		this.setSize(900, 700); 
    		this.setLocation(300, 100);
    		
    		mainPanel = new JPanel(new GridBagLayout());
    		mainPanel.setBackground(Color.LIGHT_GRAY);
    		add(mainPanel,BorderLayout.CENTER); 
    		
    		instructionPanel = new JPanel();
    		instructionPanel.setPreferredSize(new Dimension(900,200));
    		instructionPanel.setBackground(Color.LIGHT_GRAY);
    		instructionPanel.setLayout(new GridLayout(2, 1));
    		add(instructionPanel,BorderLayout.NORTH);
    		
    		playerPanel = createGridPanel(Color.WHITE);
    		enemyPanel = createGridPanel(Color.BLACK);

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
	
		    menubar = new JMenuBar();
			menuGame = new JMenu("Menü");
			backtoMenu = new JMenuItem("Spiel beenden");
			menubar.add(menuGame);
			menuGame.add(backtoMenu);
			this.setJMenuBar(menubar);
			backtoMenu.addActionListener(e ->{System.exit(0);});
			 
			instructionLabel = createInstructionLabel(SwingConstants.CENTER, SwingConstants.CENTER);
			instructionLabel.setText("Platziere die Schiffe!");
			instructionPanel.add(instructionLabel);
			
			instructionshipLabel =createInstructionLabel(SwingConstants.CENTER,SwingConstants.TOP);
			instructionshipLabel.setLayout(new GridBagLayout());
			instructionPanel.add(instructionshipLabel);
		
			//Auswahl Buttons für Schifftyp
			setupShipSelectionButtons();
	    	
		
	    	setVisible(true);
    }
    
    
    //Hilfsfunktionen für drawMap:
    private JLabel createInstructionLabel(int horizontalAlignment, int verticalAlignment) {
    	JLabel label = new JLabel();
    	label.setHorizontalAlignment(horizontalAlignment);
    	label.setVerticalAlignment(verticalAlignment);
    	return label;
    	}	
    	
    private JPanel createGridPanel(Color background) {
    	JPanel panel = new JPanel(new GridLayout(10, 10));
    	panel.setBackground(background);
    	return panel;
    }	
    	
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
        String[] shipTypes = datamodel.getShipTypes();
        int[] shipCounts = datamodel.getShipCounts();
        
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
   
    private void SelectionButtonsEnabled() {
    	int[] shipCounts = datamodel.getShipCounts();
    	for(int i = 0; i < 4; i++) {
    		if(shipCounts[i]== 0) {
    			shipTypeButtons[i].setEnabled(false);
    		}
    	}
    }
    
    //Aktualisiere die Anzeige Anzahl Schiffe
    private void updateShipCountLabels() {
        String[] shipTypes = datamodel.getShipTypes();
        int[] shipCounts = datamodel.getShipCounts();
        for (int i = 0; i < shipTypes.length; i++) {
            shipCountLabels[i].setText("Anzahl: " + shipCounts[i]);  // Aktualisiere den Text des Labels
        }
    }

    //Aktion: Auswahl Schiff, speicher die Länge
    private void onShipTypeButtonClicked(String shipType) {
    	controller.onShipTypeButtonClicked(shipType);
    }
    
    //Schiffe setzen
    private void onPlayerButtonClicked(int row, int col) {
    	if(datamodel.isPhaseOne()) {
    		if(datamodel.getShipLength()== 0) {
        		instructionshipLabel.setText("Wähle einen Schiffstyp aus!");
        	}else if (datamodel.getPlayerCellStatus(row, col)== 's') {
            	instructionshipLabel.setText("Auf diesem Feld steht bereits ein Schiff! Wähle ein anderes aus!");	
        	}else if(datamodel.checkCollisionCurrentPosition(row, col)) {
        		instructionshipLabel.setText("Die Schiffe dürfen sich nicht berühren! Wähle ein anderes Feld aus.");
        	}
        	else {
        		controller.onPlayerButtonClicked(row, col);	
        		
        		if(datamodel.isPickStartPosition() && !datamodel.isPickEndPosition()) {
        			refreshPlayerMap(); 
        			playerButtons[datamodel.getStartRow()][datamodel.getStartCol()].setBackground(Color.CYAN); //Startposition markieren
        			markPossibleEndPositions(datamodel.getStartRow(),datamodel.getStartCol());
        			instructionshipLabel.setText("Wähle die Endposition aus!");
        		}else if( !datamodel.isPickStartPosition() && datamodel.isPickEndPosition()&& playerButtons[row][col].getBackground() == Color.GREEN){
        			 updateShipCountLabels();
        			 refreshPlayerMap(); 
        			 instructionshipLabel.setText("");
        			 SelectionButtonsEnabled();
        		}
        	}
        	if(datamodel.alleSpielerSchiffePlatziert()) {
        		instructionLabel.setText("Schießen Sie auf das gegnerische Feld!");
        	}
    	}
    }
   
 
    //Markierung der möglichen Endpositionen
    private void markPossibleEndPositions(int startRow, int startCol) {
            int[][] possibleEndPositions = datamodel.calculatePossibleEndPositions(startRow, startCol);

            for (int i = 0; i < 4; i++) {
                int endRow = possibleEndPositions[i][0];
                int endCol = possibleEndPositions[i][1];

                if (datamodel.isValidPosition(endRow, endCol) && !datamodel.checkCollision(startRow, startCol, endRow, endCol)) {
                    markPossiblePosition(endRow, endCol);
                }
            }
    }

    //Hilfsfunktion für das Markieren der Endpositionen
    private void markPossiblePosition(int row, int col) {
        playerButtons[row][col].setBackground(Color.GREEN);
    }


    //Aktionen in der Schießen Phase, Spieler darf nur Gegner Feld manipulieren
    private void onEnemyButtonClicked(int row, int col) {
    	if(Controller.gamePhase==2) {
        	if(datamodel.getEnemyCellStatus(row, col)=='w') {
        		controller.onEnemyButtonClicked(row,col);
        		refreshEnemyCell(row,col);
        		//Delay einbauen
        		 try {
                     Thread.sleep(200);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
        		refreshPlayerMap();
        		instructionshipLabel.setText("Der Bot hat geschossen. Du bist dran!");
        		if(datamodel.enemyWon) {
            		instructionLabel.setText("Der Bot hat gewonnen!!");
            		instructionshipLabel.setText("Das Spiel ist zu Ende!");
            	}
        	}else if(datamodel.getEnemyCellStatus(row, col)=='s') {
        		controller.onEnemyButtonClicked(row,col);
        		refreshEnemyCell(row,col);
        		instructionshipLabel.setText("Sie dürfen erneut schießen!");
        		if(datamodel.playerWon) {
            		instructionLabel.setText("Du hast gewonnen!!");
            		instructionshipLabel.setText("Das Spiel ist zu Ende!");
            	}
        	}else if(datamodel.getEnemyCellStatus(row, col)=='b' || datamodel.getEnemyCellStatus(row, col)=='x') {
        		instructionshipLabel.setText("Dieses Feld haben Sie schon getroffen! Wähle ein anderes aus.");
        	}
    	}else if(Controller.gamePhase ==1) {
    		instructionshipLabel.setText("Hier können Sie kein Schiff platzieren! Das ist das gegnerische Feld.");
    	}
    }

    //Aktualisierung der angegriffenen Zelle
    private void refreshEnemyCell(int row,int col) {
    	char enemyStatus = datamodel.getEnemyCellStatus(row, col);
    	setButtonStatus(enemyButtons[row][col],enemyStatus);
    }
    
    private void refreshPlayerMap() 
    {
    	for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				char playerStatus = datamodel.getPlayerCellStatus(row, col); //hole Status von Datamodel
				setButtonStatus(playerButtons[row][col],playerStatus);
			}
		}
    }

    //Hilfsfunktion für refreshMap:
    //Aktuallisierung des Status der enemy Map
    //Aktuallisierung des Status der player Map
    private void setButtonStatus(JButton Button, char status) {
        switch (status) {
            case 'w': // Wasser
                        Button.setBackground(Color.BLUE);
                break;
            case 's': // Schiff
                        Button.setBackground(Color.YELLOW);
                break;
            case 'x': // Schiff getroffen
                        Button.setBackground(Color.RED);
                break;
            case 'b': // Wasser getroffen, jetzt bekannt
		                Button.setBackground(Color.LIGHT_GRAY);
                break;
            default: // unbekannter Status
                        Button.setBackground(Color.WHITE);
                break;
        }
    }

}
