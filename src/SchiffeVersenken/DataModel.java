package SchiffeVersenken;
import java.util.*;

/* Model -> Client
 * |-> Spieler sendet Daten an das Model
 * 
 * Methoden:
    public int[][] getData() 
    public void setData(int[][] data) 

//getter Schiffe für GUI
 *

 * 
*/

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
    

    public DataModel() {
    	createInitialMatrix();
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

    public void displayMatrix() {
                //////
        
        System.out.println("[DATAMODEL]");
        //Erzeugen eines 10 x 10 Feldes gefüllt mit Wasser zum testen des Arrays
        for(int i = 0; i < playerMatrix.length; i++){
            for(int j = 0; j < playerMatrix[i].length; j++){
                System.out.print(playerMatrix[i][j] + " "); 
            }
            System.out.println();           
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
    public void placeShips(int startX, int startY, int endX, int endY) {
        int shipLengthX = Math.abs(endX - startX) + 1;
        int shipLengthY = Math.abs(endY - startY) + 1;
        int shipLength = Math.max(shipLengthX, shipLengthY);
        
        if (shipLength == 2 && frigate > 0) {
            frigate--;
   
            setShipStatus(startX, startY, endX, endY);
            if (frigate <= 0) {
                System.out.print("Du hast keine Frigatten mehr");
            } else {
                
            }
        } else if (shipLength == 3 && cruiser > 0) {
            cruiser--;
            
            setShipStatus(startX, startY, endX, endY);
            if (cruiser <= 0) {
                System.out.print("Du hast keine Kreuzer mehr");
            } else {
                
            }
        } else if (shipLength == 4 && battleship > 0) {
            battleship--;
  
            setShipStatus(startX, startY, endX, endY);
            if (battleship <= 0) {
                System.out.print("Du hast keine Schlachtschiffe mehr");
            } else {
                
            }
        } else if (shipLength == 1 && submarine > 0) {

            submarine--;
            setShipStatus(startX, startY, endX, endY);
            if (submarine <= 0) {
                System.out.print("Du hast keine U-Boote mehr");
            } else {
               
            }
        } else {
            System.out.println("Kein zulaessiger Schiffstyp");
        }
    }
    
    //Status beim Schiffe setzen aktualisieren
    public void setShipStatus(int startX, int startY, int endX, int endY) {
    	// Den angegebenen Bereich auf "s" setzen
    	if (startX <= endX && startY <= endY) {
    		for (int i = startX; i <= endX; i++) {
                for (int j = startY; j <= endY; j++) {
                    playerMatrix[i][j] = 's';
                }
    		}
        } else if (startX >= endX && startY >= endY) {
        	for (int i = startX; i >= endX; i--) {
                for (int j = startY; j >= endY; j--) {
                    playerMatrix[i][j] = 's';
                }
    		}
        }	
    }
    
    
  //Kollisionen überprüfen, damit Schiffe nicht überlappen, für GUI
	  public boolean checkCollision(int startX, int startY, int endX, int endY) {
	  	// Prüfen, ob eine Position im angegebenen Bereich bereits "s" enthält
		  if (startX <= endX && startY <= endY) {
		        for (int i = startX; i <= endX; i++) {
		            for (int j = startY; j <= endY; j++) {
		                if (playerMatrix[i][j] == 's') {
		                    System.out.println("Kollision!");
		                    return true;  // Kollision
		                }
		            }
		        }
		        
		    } else if (startX >= endX && startY >= endY) {
		        for (int i = startX; i >= endX; i--) {
		            for (int j = startY; j >= endY; j--) {
		                if (playerMatrix[i][j] == 's') {
		                    System.out.println("Kollision!");
		                    return true;  // Kollision
		                }
		            }
		        }
		    }
	  	System.out.println("Keine Kollision!");
	      return false;  // keine Kollision
	  }
	  
	  //Prüft ob die angegebene Position im Feld ist
	  public boolean isValidPosition(int row, int col) {
	        return row >= 0 && row < 10 && col >= 0 && col < 10; //Position im Feld?
	    }
	  
	  //Berechne die möglichen Endpositionen
	  public int[][] calculatePossibleEndPositions(int startRow, int startCol, int shipLength) {
		  
	        int[][] offsets = {
	            {1, 0}, {-1, 0}, {0, 1}, {0, -1} //möglichen Richtungen
	        };
	        
	        int[][] possiblePositions = new int[offsets.length][2];

	        for (int i = 0; i < offsets.length; i++) {
	            int endRow = startRow + offsets[i][0] * (shipLength - 1);
	            int endCol = startCol + offsets[i][1] * (shipLength - 1);
	            possiblePositions[i][0] = endRow;
	            possiblePositions[i][1] = endCol;
	        }

	        return possiblePositions;
	    }

	    
    
    // Methode zum Schießen auf gegnerisches Feld in Phase 2
    public void shootEnemyField(int row, int col) {
        if (phaseOne) {
            System.out.println("Du kannst in dieser Phase nicht auf gegnerische Schiffe schießen.");
            return;
        }
    //schiessen auf schiffe
    char status = enemyMatrix[row][col];
    if (status == 'w') { //w für Wasser
        enemyMatrix[row][col] = 'b'; // Feld verfehlt, bekanntes Feld
    } else if (status == 's') {
        enemyMatrix[row][col] = 'x'; // Schiff getroffen
        // Koordinaten des getroffenen Feldes zur Liste hinzufügen
        hits.add(new int[]{row, col});
    }
}


//Uebergeben von Werten an GUI
    //setzt das neue datenFeld -> wird von spieler aufgerufen
    public void setData(char[][] data) {
        this.playerMatrix = data;
    }

    // Getter and setter fuer playgroundMatrix
    public char[][] getPlaygroundMatrix() {
        return playerMatrix;
    }

    public void setPlaygroundMatrix(char[][] matrix) {
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
        return new String[]{"Submarine", "Frigate", "Cruiser", "Battleship"};
    }

    // Getter fuer Anzahl der verbleibenden Schiffe
    public int[] getShipCounts() {
        return new int[]{submarine, frigate, cruiser, battleship};
    }

    // Getter fuer Zellen Status in playgroundMatrix
    public char getPlaygroundCellStatus(int row, int col) {
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

    

//    //setzt das neue datenFeld -> wird von spieler aufgerufen
//    public void setData(char[][] data) {
//        this.playgroundMatrix = data;
//    }


    /*
     * TO-DO
     * Mouse-Listener, um Array auswählen zu können oder doch lieber Tastatur Eingabe oder sollte das eher ins GUI
     * Kollisionsabfrage mit anderen Schiffen aber auch dem Spielfeldrand
     * tatsächlich schiff setzen
     * Frage: muss überprüft werden, ob Koordinaten innerhalb des Feldes liegen, oder wird das im GUI gemacht
     */
