package SchiffeVersenken;
import java.util.*;


public class DataModel {
    private char[][] playerMatrix = new char[10][10]; //Wie groß darf sie werden ? //int durch char getauscht, um mehr möglichkeiten zu haben, wie Nutzung von X
    private char[][] enemyMatrix = new char[10][10];


    //verschiedene Schiffsklassen und deren Anzahl 
    int submarine = 4;
    int frigate = 3;
    int cruiser = 2;
    int battleship = 1;
    
    // Phase 1: Schiffe setzen
    private boolean phaseOne = true;

    // Liste, um getroffene Felder zu speichern
    private List<int[]> hits = new ArrayList<>();
    
    //Schiffe setzen
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
    
    //Bot Gamemodus: Gewonnen/Verloren
    public boolean playerWon = false;
    public boolean enemyWon = false;
    
    //Bot schießen
    public int randomRow;
    public int randomCol;
    
    
    public DataModel() {
    	createInitialMatrix();
    	if(gameModi==0) {
    		initBotMatrixRandomly();
    	}
    } 

    // -> Intiale Matrix erstellen // also einmal beim Initialisierendes Objektes
    public void createInitialMatrix() {
        //playerMatrix und enemyMatrix zuerst Grundstatus Wasser geben
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                playerMatrix[i][j] = 'w';
                enemyMatrix[i][j] = 'w';
            }
        }
    }
    
    //Methode um zu prüfen, ob alle Spieler Schiffe gesetzt wurden
    public boolean alleSpielerSchiffePlatziert() {
        if(submarine == 0 && frigate == 0 && cruiser == 0 && battleship == 0) {
        	return true;
        }else {
        	return false;
        }
        
    }
    
    // Methode, um die Phase zu setzen
    public void setPhase(int phase) {
        if (phase == 1) {
            phaseOne = true;
        } else if (phase == 2) {
            phaseOne = false;
        }
    }
    
    // Methode, um die aktuelle Phase abzurufen
    public boolean isPhaseOne() {
        return phaseOne;
    }
    
    
 // Methode zum Setzen von Schiffen in Phase 1
    public void placeShips() {
        int shipLengthX = Math.abs(endX - startX) + 1;
        int shipLengthY = Math.abs(endY - startY) + 1;
        int shipLength = Math.max(shipLengthX, shipLengthY);

        //Schiffsanzahl abziehen
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
        
        //Status der Felder anpassen
        setShipStatus(startX, startY, endX, endY);

        //Kommentare
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

    
    //Status beim Schiffe setzen aktualisieren
    public void setShipStatus(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX); //Sucht von beiden Werten das kleinere aus
        int maxX = Math.max(startX, endX); //Sucht von beiden Werten das größere aus
        int minY = Math.min(startY, endY);
        int maxY = Math.max(startY, endY);

        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                playerMatrix[i][j] = 's';
            }
        }
    }

    //Auf Kollisionen überprüfen
    public boolean checkCollision(int startX, int startY, int endX, int endY) {
        int minX = Math.min(startX, endX); //Sucht von beiden Werten das kleinere aus
        int maxX = Math.max(startX, endX); //Sucht von beiden Werten das größere aus
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

	  
	//Schaut ob um die Position ein Schiff liegt, in diagonalen und geraden Richtungen
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

    
    //Prüft ob die angegebene Position im Feld liegt
	public boolean isValidPosition(int row, int col) {
		return row >= 0 && row < 10 && col >= 0 && col < 10; //Position im Feld?
	}
	
	//Berechne die möglichen Endpositionen
	public int[][] calculatePossibleEndPositions(int startRow, int startCol) {
	    int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}}; // alle 4 Richtungen
	    int[][] possiblePositions = new int[directions.length][2];

	    for (int i = 0; i < directions.length; i++) {
	        possiblePositions[i][0] = startRow + directions[i][0] * (shipLength - 1);
	        possiblePositions[i][1] = startCol + directions[i][1] * (shipLength - 1);
	    }
	    return possiblePositions;
	}


	// BOT SCHIFFE SETZEN
	// Methode für das Platzieren von Schiffen für den Bot
	private void initBotMatrixRandomly() {
	    Random random = new Random();

	    // Platziere 4 Schiffe der Größe 1 zufällig
	    for (int i = 0; i < 4; i++) {
	        placeRandomShip(1, random);
	    }

	    // Platziere 3 Boote der Größe 2 zufällig
	    for (int i = 0; i < 3; i++) {
	        placeRandomShip(2, random);
	    }

	    // Platziere 2 Boote der Größe 3 zufällig
	    for (int i = 0; i < 2; i++) {
	        placeRandomShip(3, random);
	    }

	    // Platziere 1 Boot der Größe 4 zufällig
	    placeRandomShip(4, random);
	}

	 
	// Diese Funktion überprüft, ob es Kollisionen zwischen Schiffen in der Gegnermatrix gibt,
	// basierend auf den angegebenen Start- und Endkoordinaten eines Schiffs.

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

	//Platziert random ein Schiff horizontal oder vertikal
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

	  //Überprüft ob die mögliche Positionen für die Schiffe Kollisionen machen könnten
	  private boolean isValidPosition(int row, int col, int size, boolean isHorizontal) {
		    if (isHorizontal) {
		        if (col + size > 10) {
		            return false;
		        }

		        for (int i = 0; i < size; i++) {
		            if (enemyMatrix[row][col + i] == 's' || checkCollisionEnemy(row, col + i, row, col + i)) {
		                return false;
		            }
		        }
		    } else {
		        if (row + size > 10) {
		            return false;
		        }

		        for (int i = 0; i < size; i++) {
		            if (enemyMatrix[row + i][col] == 's' || checkCollisionEnemy(row + i, col, row + i, col)) {
		                return false;
		            }
		        }
		    }

		    return true;
		}


	  // BOT SCHIESSEN
	  public void BOTShoot() {
	        Random random = new Random();
	        int row, col;

	        do {
	            row = random.nextInt(10);
	            col = random.nextInt(10);
	        } while (playerMatrix[row][col] == 'b' || playerMatrix[row][col] == 'x');

	        BOTshootPlayerField(row, col);
	    }

	    private void BOTshootPlayerField(int row, int col) {
	        char playerCellStatus = getPlayerCellStatus(row, col);

	        switch (playerCellStatus) {
	            case 'w':
	                playerMatrix[row][col] = 'b';
	                Controller.gamePhase=2;
	                break;
	            case 's':
	                playerMatrix[row][col] = 'x'; // Markiere das getroffene Schiff

	                // Überprüfe den Gewinnstatus nach dem Treffer
	                updatePlayerCellStatus(row, col);
	                // Bot darf erneut schießen
	                //Delay
	                try {
	                     Thread.sleep(500);
	                 } catch (InterruptedException e) {
	                     e.printStackTrace();
	                 }
	                BOTShoot();
	                break;
	        }
	    }

	  // GEWONNEN STATUS ÜBERPRÜFEN
	  public void updatePlayerCellStatus(int row, int col) {
	      int status = playerMatrix[row][col];
	      if (status == 'x' && checkWin(playerMatrix)) {
	          enemyWon = true;
	          System.out.println("Der Gegner hat gewonnen!!");
	      }
	  }

	  public void updateEnemyCellStatus(int row, int col) {
	      int status = enemyMatrix[row][col];
	      if (status == 'x' && checkWin(enemyMatrix)) {
	          playerWon = true;
	          System.out.println("Der Spieler hat gewonnen!!");
	      }
	  }

	  private boolean checkWin(char[][] matrix) {
	      int xCount = 0;
	      for (int i = 0; i < matrix.length; i++) {
	          for (int j = 0; j < matrix[i].length; j++) {
	              if (matrix[i][j] == 'x') {
	                  xCount++;
	              }
	          }
	      }
	      return xCount >= 20; // true -> gleich oder mehr als 20 'x' in der Matrix -> verloren
	  }


	    // Methode zum Schießen auf gegnerisches Feld in Phase 2
	    public void shootEnemyField(int row, int col) {
	        if (phaseOne) {
	            System.out.println("Du kannst in dieser Phase nicht auf gegnerische Schiffe schießen.");
	            return;
	        }else {
	        char enemyStatus = getEnemyCellStatus(row, col);
	        switch (enemyStatus) {
	            case 'w': //wasser getroffen
	                enemyMatrix[row][col]='b'; // Feld verfehlt, bekanntes Feld
	                break;
	            case 's': // Shoot on ship
	                enemyMatrix[row][col]='x'; // Schiff getroffen
	                updateEnemyCellStatus(row,col);
	                // Koordinaten des getroffenen Feldes zur Liste hinzufügen
	                hits.add(new int[]{row, col});
	                break;
	            case 'x': //schon ein Schiff getroffen
	                // Nichts passiert
	                break;
	            case 'b': // Wasser getroffen
	            	// Nichts passiert
	                break;
	            default:
	                break;
	        	}
	        }
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
	

    //setzt das neue datenFeld -> wird von spieler aufgerufen
    public void setData(char[][] data) {
        this.playerMatrix = data;
    }

    // Getter and setter fuer playgroundMatrix
    public char[][] getPlayerMatrix() {
        return playerMatrix;
    }

    public void setPlayerMatrix(char[][] matrix) {
        this.playerMatrix = matrix;
    }

    // Getter and setter fuer enemyMatrix
    public char[][] getEnemyMatrix() {
        return enemyMatrix;
    }

    public void setEnemyMatrix(char[][] matrix) {
        this.enemyMatrix = matrix;
    }

    // Getter fuer Schiffs Typ
    public String[] getShipTypes() {
        return new String[]{"U-Boot", "Fregatte", "Kreuzer", "Zerstörer"}; //Englisch: {"Submarine", "Frigate", "Cruiser","Battleship"(nicht übersetzt))}
    }

    // Getter fuer Anzahl der verbleibenden Schiffe
    public int[] getShipCounts() {
        return new int[]{submarine, frigate, cruiser, battleship};
    }

    // Getter fuer Zellen Status in playgroundMatrix
    public char getPlayerCellStatus(int row, int col) {
        return playerMatrix[row][col];
    }

    // Getter fuer Zellen Status in enemyMatrix
    public char getEnemyCellStatus(int row, int col) {
        return enemyMatrix[row][col];
    }

    // Methode zum Abrufen der Liste der getroffenen Felder
    public List<int[]> getHits() {
        return hits;
    }
}
