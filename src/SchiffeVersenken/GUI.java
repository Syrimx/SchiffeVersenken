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
import javax.swing.JPanel;
import javax.swing.SwingConstants;


/**
 * Die Klasse GUI repräsentiert die Benutzeroberfläche für das Schiffe versenken Spiel.
 * Sie verwaltet die Anzeige des Menüs, der Spielfelder, die Ausgabe von Informationen von
 * dem Datamodel und die Interaktionen des Benutzers.
 */
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
    private JPanel mainPanel;
    private JMenuBar menubar;
    private JMenu menuGame;
    private JMenuItem backtoMenu;
    private JButton[][] playerButtons;
    private JButton[][] enemyButtons;
    private JButton[] shipTypeButtons; 
    private JPanel shipSelectionPanel;
    private JLabel[] shipCountLabels;

    private JButton playButtonBot;
    private JButton playButtonFriend;
    private JButton exitButton;
    
   
    /**
     * Konstruktor der GUI Klasse.
     * Initialisiert das Datenmodell und den Controller für die Benutzeroberfläche.
     */
    public GUI() {
        this.datamodel = new DataModel();
        this.controller = new Controller(datamodel);
    }
 
	/**
	 * Richtet das Menü ein und macht es sichtbar.
	 */
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
    
    /**
     * Setzt den Spielmodus auf Bot-Modus mithilfe der Controller-Funktion.
     * Setzt die Benutzeroberfläche zurück und zeichnet die Spielfläche neu.
     */
    private void openGameWindowBot(){
        controller.openGameWindowBot();
        resetGUI();
        drawMap();
    }

    /**
     * Setzt den Spielmodus auf Freund-Modus mithilfe der Controller-Funktion.
     * Setzt die Benutzeroberfläche zurück und zeichnet die Spielfläche neu.
     */
    private void openGameWindowFriend() {
        controller.openGameWindowFriend();
        resetGUI();
        drawMap();
    }

    /**
     * Setzt die Benutzeroberfläche zurück, indem sie unsichtbar geschaltet wird
     * und alle Komponenten aus dem Hauptinhalt entfernt werden.
     */
    public void resetGUI(){
        setVisible(false);
        this.getContentPane().removeAll();
    }
    
    /**
     * Zeichnet die Spielfläche. 
     */
    public void drawMap() {
        this.setLayout(new BorderLayout());
        this.setTitle("Schiffe versenken");
        this.getContentPane().setBackground(Color.LIGHT_GRAY);
        this.setSize(900, 700);
        this.setLocation(300, 100);

        // Hauptpanel für das Rasterlayout erstellen
        mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.LIGHT_GRAY);
        add(mainPanel, BorderLayout.CENTER);

        // Panel für Anweisungen erstellen und hinzufügen
        instructionPanel = new JPanel();
        instructionPanel.setPreferredSize(new Dimension(900, 200));
        instructionPanel.setBackground(Color.LIGHT_GRAY);
        instructionPanel.setLayout(new GridLayout(2, 1));
        add(instructionPanel, BorderLayout.NORTH);

        // Spieler- und Gegnerpanels erstellen
        playerPanel = createGridPanel(Color.WHITE);
        enemyPanel = createGridPanel(Color.BLACK);

        // x- und y-Achsenbeschriftungspanels erstellen
        JPanel playerXAxisLabelPanel = new JPanel(new GridLayout(1, 10));
        JPanel playerYAxisLabelPanel = new JPanel(new GridLayout(10, 1));
        JPanel labeledPlayerPanel = createLabeledPanel(playerPanel, playerXAxisLabelPanel, playerYAxisLabelPanel, Color.WHITE);

        JPanel enemyXAxisLabelPanel = new JPanel(new GridLayout(1, 10));
        JPanel enemyYAxisLabelPanel = new JPanel(new GridLayout(10, 1));
        JPanel labeledEnemyPanel = createLabeledPanel(enemyPanel, enemyXAxisLabelPanel, enemyYAxisLabelPanel, Color.BLACK);

        // Panels in das Hauptpanel einfügen
        addLabeledPanelToMainPanel(labeledEnemyPanel, 0, 0);
        addLabeledPanelToMainPanel(labeledPlayerPanel, 1, 0);

        // Schaltflächen und Achsenbeschriftungen erstellen
        addButtonstoPanel();
        addAxisLabels(playerYAxisLabelPanel, playerXAxisLabelPanel, Color.BLACK);
        addAxisLabels(enemyYAxisLabelPanel, enemyXAxisLabelPanel, Color.WHITE);

        // Menüleiste erstellen und hinzufügen
        menubar = new JMenuBar();
        menuGame = new JMenu("Menü");
        backtoMenu = new JMenuItem("Spiel beenden");
        menubar.add(menuGame);
        menuGame.add(backtoMenu);
        this.setJMenuBar(menubar);
        backtoMenu.addActionListener(e ->{System.exit(0);});

        // Hauptanweisungslabel für das Spiel erstellen und hinzufügen
        instructionLabel = createInstructionLabel(SwingConstants.CENTER, SwingConstants.CENTER);
        instructionLabel.setText("Platziere die Schiffe!");
        instructionPanel.add(instructionLabel);

        // Unteresanweisungslabel für genauere Informationen erstellen und hinzufügen
        instructionshipLabel =createInstructionLabel(SwingConstants.CENTER,SwingConstants.TOP);
        instructionshipLabel.setLayout(new GridBagLayout());
        instructionPanel.add(instructionshipLabel);

        setupShipSelectionButtons();

        setVisible(true);
    }
    
    //Hilfsfunktionen für drawMap:
    /**
     * Erstellt ein JLabel für Anweisungen mit den angegebenen Ausrichtungen.
     * 
     * @param horizontalAlignment Die horizontale Ausrichtung des Labels
     * @param verticalAlignment Die vertikale Ausrichtung des Labels
     * @return Das erstellte JLabel für Anweisungen
     */
    private JLabel createInstructionLabel(int horizontalAlignment, int verticalAlignment) {
        JLabel label = new JLabel(); 
        label.setHorizontalAlignment(horizontalAlignment); // Horizontale Ausrichtung festlegen
        label.setVerticalAlignment(verticalAlignment); // Vertikale Ausrichtung festlegen
        return label; 
    }
	
    /**
     * Erstellt ein Panel mit einem Rasterlayout (10x10) und der angegebenen Hintergrundfarbe.
     * 
     * @param background Die Hintergrundfarbe des Panels
     * @return Das erstellte Panel mit Rasterlayout
     */
    private JPanel createGridPanel(Color background) {
        JPanel panel = new JPanel(new GridLayout(10, 10));
        panel.setBackground(background); 
        return panel; 
    }

    /**
     * Fügt ein beschriftetes Panel an den angegebenen Gitterpositionen im Hauptpanel hinzu.
     * 
     * @param labeledPanel Das Panel mit Beschriftungen, das dem Hauptpanel hinzugefügt werden soll
     * @param gridX Die horizontale Gitterposition, an der das Panel platziert werden soll
     * @param gridY Die vertikale Gitterposition, an der das Panel platziert werden soll
     */
    private void addLabeledPanelToMainPanel(JPanel labeledPanel, int gridX, int gridY) {
        // Konfigurierung der Platzierung und Ausdehnung des Panels innerhalb des Hauptpanels
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH; // Panel füllt verfügbaren Platz
        gbc.weightx = 1.0; // Horizontale Ausdehnung
        gbc.weighty = 1.0; // Vertikale Ausdehnung
        gbc.gridx = gridX; // Horizontale Gitterposition
        gbc.gridy = gridY; // Vertikale Gitterposition
        gbc.insets = new Insets(30, 30, 30, 30); // Randabstand
        mainPanel.add(labeledPanel, gbc); 
    }

    /**
     * Erstellt ein beschriftetes Panel mit Achsenbeschriftungen um das gegebene Panel.
     * 
     * @param panel Das zu umgebende Panel
     * @param xAxisLabelPanel Das Panel für x-Achsenbeschriftungen
     * @param yAxisLabelPanel Das Panel für y-Achsenbeschriftungen
     * @param background Die Hintergrundfarbe des Panels
     * @return labeledPanel Das erstellte beschriftete Panel
     */
    private JPanel createLabeledPanel(JPanel panel, JPanel xAxisLabelPanel, JPanel yAxisLabelPanel, Color background) {
        JPanel labeledPanel = new JPanel(new BorderLayout());
        labeledPanel.add(xAxisLabelPanel, BorderLayout.NORTH); 
        labeledPanel.add(yAxisLabelPanel, BorderLayout.WEST);
        labeledPanel.add(panel, BorderLayout.CENTER);

        xAxisLabelPanel.setBackground(background);
        yAxisLabelPanel.setBackground(background);
        return labeledPanel;
    }
    
    /**
     * Fügt Schaltflächen für Spieler- und Gegnerfelder sowie die entsprechenden Aktionen hinzu.
     * 
     */
    private void addButtonstoPanel() {
        // Schaltflächen für Spieler- und Gegnerfelder erstellen
        playerButtons = new JButton[10][10];
        enemyButtons = new JButton[10][10];

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                // Spieler-Schaltfläche erstellen und konfigurieren
                playerButtons[row][col] = new JButton();
                playerButtons[row][col].setPreferredSize(new Dimension(40, 40)); // Größe festlegen
                playerButtons[row][col].setBackground(Color.BLUE);

                final int finalRow = row; // temporäre final Variablen
                final int finalCol = col;

                // Aktion für Spieler Schaltfläche hinzufügen
                playerButtons[row][col].addActionListener(e -> onPlayerButtonClicked(finalRow, finalCol));

                playerPanel.add(playerButtons[row][col]);
                
                // Gegner-Schaltfläche erstellen und konfigurieren
                enemyButtons[row][col] = new JButton();
                enemyButtons[row][col].setPreferredSize(new Dimension(40, 40)); // Größe festlegen
                enemyButtons[row][col].setBackground(Color.BLUE);

                // Aktion für Gegner Schaltfläche hinzufügen
                enemyButtons[row][col].addActionListener(e -> onEnemyButtonClicked(finalRow, finalCol));

                enemyPanel.add(enemyButtons[row][col]);
            }
        }
    }

    /**
     * Fügt Achsenbeschriftungen zu den gegebenen Panels hinzu.
     * 
     * @param yAxisLabelPanel Das Panel für die y-Achsenbeschriftungen
     * @param xAxisLabelPanel Das Panel für die x-Achsenbeschriftungen
     * @param foreground Die Farbe der Beschriftungen
     */
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

    /**
     * Einrichtung der Schaltflächen für die Auswahl der Schiffstypen sowie der dazugehörigen Anzeigelabel,
     * die die aktuelle Anzahl der verbleibenden Schiffe anzeigen.
     */
    private void setupShipSelectionButtons() {
        // Schiffstypen (hier auf Deutsch) und Schiffszählungen aus dem Datamodel 
        String[] shipTypes = datamodel.getShipTypes(); 
        int[] shipCounts = datamodel.getShipCounts();
        
        // Anweisung des Benutzers zur Auswahl eines Schiffstyps 
        instructionshipLabel.setText("Wähle einen Schiffstyp aus!");
        
        // Ein Panel für die Schaltflächen der Schiffstypen erstellen
        shipSelectionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        instructionshipLabel.add(shipSelectionPanel);

        // Arrays für Schaltflächen und Anzeigelabel der Schiffstypen initialisieren
        shipTypeButtons = new JButton[shipTypes.length];
        shipCountLabels = new JLabel[shipTypes.length]; 

        // Schleife durch alle Schiffstypen
        for (int i = 0; i < shipTypes.length; i++) {
            final int shipIndex = i;
            
            // Schaltfläche für den aktuellen Schiffstyp erstellen und konfigurieren
            shipTypeButtons[i] = new JButton(shipTypes[i]);
            shipTypeButtons[i].setPreferredSize(new Dimension(100, 40));
            shipSelectionPanel.add(shipTypeButtons[i]);
            
            // Aktionlistener für die Schaltfläche hinzufügen
            shipTypeButtons[i].addActionListener(e -> onShipTypeButtonClicked(shipTypes[shipIndex]));
            
            // Anzeigelabel für die Anzahl von Schiffen dieses Typs erstellen und hinzufügen
            shipCountLabels[i] = new JLabel("Anzahl: " + shipCounts[i]); 
            shipSelectionPanel.add(shipCountLabels[i]);
        }
    }

    /**
     * Aktualisiert die Anzeigelabel für die Anzahl der verbleibenden Schiffe.
     * 
     */
    private void updateShipCountLabels() {
        String[] shipTypes = datamodel.getShipTypes();
        int[] shipCounts = datamodel.getShipCounts();
        for (int i = 0; i < shipTypes.length; i++) {
            shipCountLabels[i].setText("Anzahl: " + shipCounts[i]);  // Aktualisiere den Text des Labels
        }
    }

    /**
     * Deaktiviert die Auswahlbuttons, falls für einen Schiffstyp keine Schiffe
     * mehr verfügbar sind.
     * 
     */
    private void SelectionButtonsEnabled() {
        int[] shipCounts = datamodel.getShipCounts();
        for (int i = 0; i < 4; i++) {
            if (shipCounts[i] == 0) {
                shipTypeButtons[i].setEnabled(false);
            }
        }
    }

    /**
     * Reaktion auf das Klicken eines Schiffstyp-Auswahlbuttons.
     * Sendet den ausgewählten Schiffstyp an den Controller weiter.
     * 
     * @param shipType Der ausgewählte Schiffstyp
     */
    private void onShipTypeButtonClicked(String shipType) {
        controller.onShipTypeButtonClicked(shipType);
    }



    
    
    
    
    
    //Schiffe setzen
    /**
     * 
     * @param row
     * @param col
     */
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
   
 
    /**
     * 
     * @param startRow
     * @param startCol
     */
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


    /**
     * 
     * @param row
     * @param col
     */
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

    /**
     * Aktualisiert das angegriffene Feld im gegenerischen Feld.
     * @param row
     * @param col
     */
    private void refreshEnemyCell(int row,int col) {
    	char enemyStatus = datamodel.getEnemyCellStatus(row, col);
    	setButtonStatus(enemyButtons[row][col],enemyStatus);
    }
    
    /**
     * Aktualisiert die Anzeige des Spielerfelds.
     */
    private void refreshPlayerMap() 
    {
    	for(int row = 0; row < 10; row++) {
			for(int col = 0; col < 10; col++) {
				char playerStatus = datamodel.getPlayerCellStatus(row, col); //hole Status von Datamodel
				setButtonStatus(playerButtons[row][col],playerStatus);
			}
		}
    }

    /**
     * Aktualisiert den Status eines Buttons in der Spielfeldansicht.
     *
     * @param button Der Button, dessen Status aktualisiert wird
     * @param status Der neue Status des Buttons 
     * ('w' für Wasser, 's' für Schiff, 'x' für getroffenes Schiff, 'b' für bekanntes Wasserfeld)
     */
    private void setButtonStatus(JButton button, char status) {
        switch (status) {
            case 'w': 
                button.setBackground(Color.BLUE);
                break;
            case 's': 
                button.setBackground(Color.YELLOW);
                break;
            case 'x': 
                button.setBackground(Color.RED);
                break;
            case 'b': 
                button.setBackground(Color.LIGHT_GRAY);
                break;
            default: // unbekannter Status
                button.setBackground(Color.WHITE);
                break;
        }
    }


}
