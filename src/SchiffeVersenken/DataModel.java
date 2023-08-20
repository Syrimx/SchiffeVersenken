package SchiffeVersenken;
import java.util.*;

/**
 * Die DataModel Klasse verwaltet die Spielinformationen für das Spiel Schiffe versenken.
 * Sie speichert den Zustand, die Spieler- und Gegner-Matrizen,den jeweiligen Status der Felder 
 * und den Siegstatus.
 * Methoden ermöglichen Datenänderungen, Platzierung und Schüsse von Spielern und Bots.
 */
public class DataModel {
    private char[][] playerMatrix = new char[10][10]; 
    private char[][] enemyMatrix = new char[10][10];


    //verschiedene Schiffsklassen und deren Anzahl 
    int submarine = 4;
    int frigate = 3;
    int cruiser = 2;
    int battleship = 1;
    
    // Phase 1: Schiffe setzen
    private boolean phaseOne = true;

    // Liste, um getroffene Felder zu speichern, gedacht für Modus: Freund
    private List<int[]> hits = new ArrayList<>();
    
    //Schiffe setzen Variablen
    private int shipLength =0;
    private int tmpshipLength = 0;
    private int currentShipType;
    private int startX = -1;
    private int startY = -1;
    private int endX = -1;
    private int endY = -1;
    private boolean PickStartPosition = false;
    private boolean PickEndPosition = false;

    //Spielmodus
    private int gameModi; // Bot: 0, Friend: 1
    
    //Gamemodus: Gewonnen/Verloren
    public boolean playerWon = false;
    public boolean enemyWon = false;
    
    //Bot schießen
    public int randomRow;
    public int randomCol;
    
    /**
     * Konstruktor der Datamodel Klasse.
     * Initialisiert die Matrix und für das Spielerfeld und Gegenerfeld.
     * Bei Spielmodus 0 (Bot) wird die Bot Matrix (Gegnerfeld) zufällig initialisiert.
     */
    public DataModel() {
        createInitialMatrix();
        
        // Initialisierung abhängig vom Spielmodus
        if (gameModi == 0) {
            initBotMatrixRandomly();
        }
    }

    /**
     * Initialisiert das Spielerfeld und Gegnerfeld mit dem Grundstatus "Wasser".
     */
    public void createInitialMatrix() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerMatrix[i][j] = 'w'; 
                enemyMatrix[i][j] = 'w';  
            }
        }
    }
    
    /**
     * Überprüft, ob alle Schiffe des Spielers erfolgreich platziert wurden.
     * 
     * @return true, wenn alle Schiffe platziert wurden; false, wenn noch Schiffe zu platzieren sind.
     */
    public boolean alleSpielerSchiffePlatziert() {
        if (submarine == 0 && frigate == 0 && cruiser == 0 && battleship == 0) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Setzt die aktuelle Spielphase entsprechend des übergebenen Phasenwerts.
     * 
     * @param phase Der Phasenwert, der die Spielphase angibt (1 für Platzierungsphase, 2 für Schussphase)
     */
    public void setPhase(int phase) {
        if (phase == 1) {
            phaseOne = true;
        } else if (phase == 2) {
            phaseOne = false;
        }
    }
    
    /**
     * Gibt zurück, ob sich das Spiel in der Platzierungsphase befindet.
     * 
     * @return true, wenn sich das Spiel in der Platzierungsphase befindet, sonst false.
     */
    public boolean isPhaseOne() {
        return phaseOne;
    }
    
    /**
     * Platzierung der ausgewählten Schiffe auf dem Spielfeld in Phase 1.
     * Es wird die Schiffslänge berechnet und die Verfügbarkeit der Schiffe überprüft.
     * Anschließend werden die Status der betreffenden Spielfelder angepasst.
     */
    public void placeShips() {
        int shipLengthX = Math.abs(endX - startX) + 1;
        int shipLengthY = Math.abs(endY - startY) + 1;
        int shipLength = Math.max(shipLengthX, shipLengthY);

        // Schiffsanzahl reduzieren
        if (shipLength == 2 && frigate > 0) {
            frigate--;
        } else if (shipLength == 3 && cruiser > 0) {
            cruiser--;
        } else if (shipLength == 4 && battleship > 0) {
            battleship--;
        } else if (shipLength == 1 && submarine > 0) {
            submarine--;
        } else {
            System.out.println("Kein zulaessiger Schiffstyp");
            return;
        }
        
        // Status der Spielfelder anpassen
        setShipStatus(startX, startY, endX, endY);

        // Testausgaben für verbleibende Schiffe
        if (frigate <= 0) {
            System.out.print("Du hast keine Fregatten mehr"); 
        } else if (cruiser <= 0) {
            System.out.print("Du hast keine Kreuzer mehr");
        } else if (battleship <= 0) {
            System.out.print("Du hast keine Schlachtschiffe mehr");
        } else if (submarine <= 0) {
            System.out.print("Du hast keine U-Boote mehr");
        }
    }
    
    /**
     * Aktualisiert die Status der Spielfelder beim Platzieren von Schiffen.
     * Die Methode markiert die Felder zwischen den Start- und Endpositionen als belegt (Schiff).
     * 
     * @param startX Die Zeilenposition der Startposition
     * @param startY Die Spaltenposition der Startposition
     * @param endX Die Zeilenposition der Endposition
     * @param endY Die Spaltenposition der Endposition
     */
    public void setShipStatus(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX); // Wählt den kleineren Wert aus beiden aus
        int maxX = Math.max(startX, endX); // Wählt den größeren Wert aus beiden aus
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        // Markiert die Felder zwischen den Start- und Endpositionen als belegt (Schiff)
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                playerMatrix[i][j] = 's';
            }
        }
    }
    
    /**
     * Überprüft, ob es Kollisionen bei der Platzierung von Schiffen zwischen
     * den angegebenen Start- und Endpositionen gibt.
     * 
     * @param startX Die Zeilenposition der Startposition
     * @param startY Die Spaltenposition der Startposition
     * @param endX Die Zeilenposition der Endposition
     * @param endY Die Spaltenposition der Endposition
     * @return true, wenn es eine Kollision gibt, ansonsten false
     */
    public boolean checkCollision(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX); // Wählt den kleineren Wert von beiden aus
        int maxX = Math.max(startX, endX); // Wählt den größeren Wert von beiden aus
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                if (playerMatrix[i][j] == 's' || checkCollisionCurrentPosition(i, j)) {
                    System.out.println("Kollision!");
                    return true;
                }
            }
        }

        System.out.println("Keine Kollision!");
        return false;
    }
    
    /**
     * Überprüft, ob sich um die angegebene Position herum ein Schiff befindet,
     * sowohl in geraden als auch diagonalen Richtungen.
     * 
     * @param row Die Zeilenposition der zu überprüfenden Position
     * @param col Die Spaltenposition der zu überprüfenden Position
     * @return true, wenn ein Schiff um die Position liegt, ansonsten false
     */
    public boolean checkCollisionCurrentPosition(int row, int col) {
        int[][] surrounding = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1},  // Gerade Richtungen
            {1, 1}, {-1, 1}, {1, -1}, {-1, -1} // Diagonale Richtungen
        };

        for (int[] direction : surrounding) {
            int checkRow = row + direction[0];
            int checkCol = col + direction[1];

            if (isValidPosition(checkRow, checkCol) && getPlayerCellStatus(checkRow, checkCol) == 's') {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Überprüft, ob die angegebene Position sich innerhalb des Spielfelds befindet.
     * 
     * @param row Die Zeilenposition der zu überprüfenden Position
     * @param col Die Spaltenposition der zu überprüfenden Position
     * @return true, wenn die Position innerhalb des Spielfelds liegt, ansonsten false
     */
    public boolean isValidPosition(int row, int col) {
        return row >= 0 && row < 10 && col >= 0 && col < 10; 
    }
    
    /**
     * Berechnet die möglichen Endpositionen eines Schiffs ausgehend von der Startposition.
     * 
     * @param startRow Die Zeilenposition der Startposition
     * @param startCol Die Spaltenposition der Startposition
     * @return Ein 2D-Array mit den möglichen Endpositionen für jede Richtung
     */
    public int[][] calculatePossibleEndPositions(int startRow, int startCol) {
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // alle 4 Richtungen
        int[][] possiblePositions = new int[directions.length][2];

        for (int i = 0; i < directions.length; i++) {
            possiblePositions[i][0] = startRow + directions[i][0] * (shipLength - 1);
            possiblePositions[i][1] = startCol + directions[i][1] * (shipLength - 1);
        }
        return possiblePositions;
    }
    
    /**
     * Schießt auf das gegnerische Feld während Phase 2 des Spiels.
     * 
     * @param row Die Zeilenposition des Feldes
     * @param col Die Spaltenposition des Feldes
     */
    public void shootEnemyField(int row, int col) {
        if (phaseOne) {
            System.out.println("Du kannst in dieser Phase nicht auf gegnerische Schiffe schießen.");
            return;
        } else {
            char enemyStatus = getEnemyCellStatus(row, col);
            switch (enemyStatus) {
                case 'w': // Wasser getroffen
                    enemyMatrix[row][col] = 'b'; // Wasser getroffen, bekanntes Feld
                    break;
                case 's': // Auf Schiff geschossen
                    enemyMatrix[row][col] = 'x'; // Schiff getroffen
                    updateEnemyCellStatus(row, col);
                    // Koordinaten des getroffenen Feldes zur Liste hinzufügen
                    hits.add(new int[]{row, col});
                    break;
                case 'x': // Bereits ein Schiff getroffen
                    // Nichts passiert
                    break;
                case 'b': // Bereits verfehlt
                    // Nichts passiert
                    break;
                default:
                    break;
            }
        }
    }
 
	// BOT SCHIFFE SETZEN
    /**
     * Initialisiert die Spielfeldmatrix des Bots mit zufällig platzierten Schiffen.
     */
	private void initBotMatrixRandomly() {
	    Random random = new Random();

	    // Platziere 4 Schiffe der Größe 1 zufällig
	    for (int i = 0; i < 4; i++) {
	        placeRandomShip(1, random);
	    }

	    // Platziere 3 Schiffe der Größe 2 zufällig
	    for (int i = 0; i < 3; i++) {
	        placeRandomShip(2, random);
	    }

	    // Platziere 2 Schiffe der Größe 3 zufällig
	    for (int i = 0; i < 2; i++) {
	        placeRandomShip(3, random);
	    }

	    // Platziere 1 Schiff der Größe 4 zufällig
	    placeRandomShip(4, random);
	}
	
	/**
	 * Überprüft, ob es Kollisionen zwischen Schiffen in der Gegnermatrix gibt.
	 * 
	 * @param startX Die Start-Zeilenposition des Schiffs
	 * @param startY Die Start-Spaltenposition des Schiffs
	 * @param endX Die End-Zeilenposition des Schiffs
	 * @param endY Die End-Spaltenposition des Schiffs
	 * @return true, wenn eine Kollision gefunden wurde, sonst false
	 */
	private boolean checkCollisionEnemy(int startX, int startY, int endX, int endY) {
	    // Schleife über Zeilen in einem Bereich um das Schiff herum
	    for (int i = Math.max(0, Math.min(startX, endX) - 1); i <= Math.min(9, Math.max(startX, endX) + 1); i++) {
	        // Schleife über Spalten in einem Bereich um das Schiff herum
	        for (int j = Math.max(0, Math.min(startY, endY) - 1); j <= Math.min(9, Math.max(startY, endY) + 1); j++) {
	            // Überprüfe, ob das aktuelle Feld in der Gegnermatrix ein Schiff enthält
	            if (enemyMatrix[i][j] == 's') {
	                return true; // Kollision gefunden
	            }
	        }
	    }
	    return false; // Keine Kollision gefunden
	}
	
	/**
	 * Platziert zufällig ein Schiff in horizontaler oder vertikaler Ausrichtung.
	 * 
	 * @param size Die Größe des zu platzierenden Schiffs
	 * @param random Eine zufällige Zahl
	 */
	private void placeRandomShip(int size, Random random) {
	    int row, col;
	    boolean isHorizontal = random.nextBoolean(); // Zufällig wählen, ob horizontal oder vertikal

	    do {
	        row = random.nextInt(10);
	        col = random.nextInt(10);
	    } while (!isValidPosition(row, col, size, isHorizontal));

	    if (isHorizontal) {
	        for (int i = 0; i < size; i++) {
	            enemyMatrix[row][col + i] = 's';
	        }
	    } else {
	        for (int i = 0; i < size; i++) {
	            enemyMatrix[row + i][col] = 's';
	        }
	    }
	}
	
	/**
	 * Überprüft, ob die möglichen Positionen für Schiffe Kollisionen verursachen könnten.
	 * 
	 * @param row Die Zeilenposition des Schiffsbereichs
	 * @param col Die Spaltenposition des Schiffsbereichs
	 * @param size Die Größe des zu platzierenden Schiffs
	 * @param isHorizontal Gibt an, ob das Schiff horizontal platziert wird
	 * @return true, wenn die Position gültig ist und keine Kollisionen verursacht, sonst false
	 */
	private boolean isValidPosition(int row, int col, int size, boolean isHorizontal) {
	    if (isHorizontal) {
	        if (col + size > 10) {
	            return false; // Schiff über die rechte Spielfeldgrenze hinaus
	        }

	        for (int i = 0; i < size; i++) {
	            if (enemyMatrix[row][col + i] == 's' || checkCollisionEnemy(row, col + i, row, col + i)) {
	                return false; // Kollision mit einem anderen Schiff oder ungültige Position
	            }
	        }
	    } else {
	        if (row + size > 10) {
	            return false; // Schiff über die untere Spielfeldgrenze hinaus
	        }

	        for (int i = 0; i < size; i++) {
	            if (enemyMatrix[row + i][col] == 's' || checkCollisionEnemy(row + i, col, row + i, col)) {
	                return false; // Kollision mit einem anderen Schiff oder ungültige Position
	            }
	        }
	    }

	    return true; // Position ist gültig und verursacht keine Kollisionen
	}
	
	// BOT SCHIESSEN
	/**
	 * Der Bot schießt auf ein zufälliges Feld, das noch nicht getroffen wurde.
	 */
	public void BOTShoot() {
        Random random = new Random();
        int row, col;

        do {
            row = random.nextInt(10);
            col = random.nextInt(10);
        } while (playerMatrix[row][col] == 'b' || playerMatrix[row][col] == 'x');

        BOTshootPlayerField(row, col);
    }
	
	/**
	 * Bot schießt auf das angegebene Feld im Spielerfeld.
	 * Dabei wird der Status angepasst und die Spielphase (gegebenfalls).
	 * 
	 * @param row Die Zeilenposition des ausgewählten Felds
	 * @param col Die Spaltenposition des ausgewählten Felds
	 */
	private void BOTshootPlayerField(int row, int col) {
	    char playerCellStatus = getPlayerCellStatus(row, col);

	    switch (playerCellStatus) {
	        case 'w': // Wasser getroffen
	            playerMatrix[row][col] = 'b'; // Markiere als verfehlt
	            Controller.gamePhase = 2; // Wechsle zur Bot-Schussphase
	            break;
	        case 's': // Schiff getroffen
	            playerMatrix[row][col] = 'x'; // Markiere das getroffene Schiff

	            // Überprüfe den Gewinnstatus nach dem Treffer
	            updatePlayerCellStatus(row, col);

	            // Bot darf erneut schießen
	            try {
	                Thread.sleep(500); // Verzögerung von 500 Millisekunden
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	            BOTShoot(); 
	            break;
	    }
	}
	
	// GEWONNEN STATUS ÜBERPRÜFEN
	/**
	 * Überprüft, ob der Gegner (Bot) gewonnen hat.
	 * 
	 * @param row Zeilenposition der Zelle
	 * @param col Spaltenposition der Zelle
	 */
	public void updatePlayerCellStatus(int row, int col) {
      if (checkWin(playerMatrix)) {
          enemyWon = true;
          System.out.println("Der Gegner hat gewonnen!!");
      }
	}

	/**
	 * Überprüft, ob der Spieler gewonnen hat.
	 * 
	 * @param row Zeilenposition der Zelle
	 * @param col Spaltenposition der Zelle
	 */
	public void updateEnemyCellStatus(int row, int col) {
		if (checkWin(enemyMatrix)) {
			playerWon = true;
			System.out.println("Der Spieler hat gewonnen!!");
		}
	}

	/**
	 * Überprüft, ob der Spieler gewonnen hat, indem gezählt wird, 
	 * wie viele getroffene Schiffe im Spielfeld sind.
	 * Wenn die Anzahl der getroffenen Schiffe gleich 20 ist, wird true zurückgegeben (verloren).
	 *
	 * @param matrix Die Matrix, die überprüft werden soll
	 * @return true, wenn der Spieler verloren hat, sonst false
	 */
	private boolean checkWin(char[][] matrix) {
	    int xCount = 0;
	    for (int i = 0; i < matrix.length; i++) {
	        for (int j = 0; j < matrix[i].length; j++) {
	            if (matrix[i][j] == 'x') {
	                xCount++;
	            }
	        }
	    }
	    return xCount == 20; // true -> genau 20 getroffene Schiffe im Spielfeld -> verloren
	}


    //Getter und Setter
    public int getGameModi() {
		return gameModi;
	}

	public void setGameModi(int gameModi) {
		this.gameModi = gameModi;
	}
	
	public int getCurrentShipType() {
		return currentShipType;
	}

	public void setCurrentShipType(int currentShipType) {
		this.currentShipType = currentShipType;
	}

	public boolean isPickEndPosition() {
		return PickEndPosition;
	}

	public void setPickEndPosition(boolean pickEndPosition) {
		PickEndPosition = pickEndPosition;
	}

	public boolean isPickStartPosition() {
		return PickStartPosition;
	}

	public void setPickStartPosition(boolean pickStartPosition) {
		PickStartPosition = pickStartPosition;
	}

	public int getShipLength() {
		return shipLength;
	}

    //zum Schiffe setzen
	public void setShipLength(int shipLength) {
		this.shipLength = shipLength;
	}

	public void setStartCell(int row, int col) {
        startX = row;
        startY = col;
    }

    public int getStartRow() {
        return startX;
    }

    public int getStartCol() {
        return startY;
    }
    
    public void setEndCell(int row, int col) {
        endX = row;
        endY = col;
    }
    
    public int getEndRow() {
        return endX;
    }

    public int getEndCol() {
        return endY;
    }
    
    //temporäre Variable
	public int getTmpshipLength() {
		return tmpshipLength;
	}

	public void setTmpshipLength(int tmpshipLength) {
		this.tmpshipLength = tmpshipLength;
	}
	

    //setzt das neue Datenfeld -> wird von spieler aufgerufen (für Modus: Freund)
    public void setData(char[][] data) {
        this.playerMatrix = data;
    }

    //Getter für Spielermatrix
    public char[][] getPlayerMatrix() {
        return playerMatrix;
    }

    public void setPlayerMatrix(char[][] matrix) {
        this.playerMatrix = matrix;
    }

    //Getter für Gegnermatrix
    public char[][] getEnemyMatrix() {
        return enemyMatrix;
    }

    public void setEnemyMatrix(char[][] matrix) {
        this.enemyMatrix = matrix;
    }

    // Getter für Schiffs Typen
    public String[] getShipTypes() {
        return new String[]{"U-Boot", "Fregatte", "Kreuzer", "Zerstörer"}; //Englisch: {"Submarine", "Frigate", "Cruiser","Battleship"(nicht übersetzt))}
    }

    // Getter für Anzahl der verbleibenden Schiffe
    public int[] getShipCounts() {
        return new int[]{submarine, frigate, cruiser, battleship};
    }

    // Getter für Zellen Status in playgroundMatrix
    public char getPlayerCellStatus(int row, int col) {
        return playerMatrix[row][col];
    }

    // Getter für Zellen Status in enemyMatrix
    public char getEnemyCellStatus(int row, int col) {
        return enemyMatrix[row][col];
    }

    // Methode zum Abrufen der Liste der getroffenen Felder (Modus: gegen Freund)
    public List<int[]> getHits() {
        return hits;
    }
}
