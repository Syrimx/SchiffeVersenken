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
    private char[][] playerMatrix    = null; //Wie groß darf sie werden ? //int durch char getauscht, um mehr möglichkeiten zu haben, wie Nutzung von X
    private char[][] enemyMatrix     = null;


    //verschiedene Schiffsklassen und deren Anzahl 
    int submarine = 4;
    int frigate = 3;
    int cruiser = 2;
    int battleship = 1;
    
    // Phase 1: Schiffe setzen
    private boolean phaseOne = true;

    // Liste, um getroffene Felder zu speichern
    private List<int[]> hits = new ArrayList<>();
    

    public void DataModel() {
        // -> Intiale Matrix erstellen // also einmal beim Initialisierendes Objektes
        // Groeße des Feldes nach klassischen Spielregeln 10 x 10 
        this.playerMatrix = new char [10][10];
        this.enemyMatrix = new char[10][10];

            //Erzeugen eines 10 x 10 Feldes gefüllt mit Nullen zum testen des Arrays
            for(int i = 0; i < playerMatrix.length; i++){
                for(int j = 0; j < playerMatrix[i].length; j++){
                	playerMatrix[i][j] = '0';
                    enemyMatrix[i][j] = '0';
        
                    System.out.print(playerMatrix[i][j] + " "); 
                }
                System.out.println();           //geht eine Zeile nach unten
            }
            
     
           //vorher char[][] Datentyp: return this.playgroundMatrix;
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
    public void placeShips(int startX, int startY, int endX, int endY){

    	if (!phaseOne) {
            System.out.println("Du kannst in dieser Phase keine Schiffe setzen.");
            return;
        }
    	
    	
        //prüfen, ob Start- und Endfeld bereits belegt sind
        if(playerMatrix[startX][startY] != 0 || playerMatrix[endX][endY] != 0){
            System.out.println("Feld bereits durch ein anderes Schiff belegt. Bitte ein anderes Feld auswählen");
            return;
        }

        //Diagonalität vermeiden
        while(startX == endX || startY == endY){
            System.out.println("Dieses Feld ist grün");
            setShipStatus(startX,startY,endX,endY);
        }

        while(startX != endX || startY != endY){ 
            System.out.println("Dieses Feld ist rot");
        }


        //Bedingungen für setzen der Schiffe 
        if(endX - startX == 1 || startX - endX == -1 || endY - startY == 1 || startY - endY == -1){
            submarine--;
            setShipStatus(startX,startY,endX,endY);
            if(submarine <= 0){
                System.out.print("Du hast keine U-Boote mehr");
            }else{}

        }else if(endX - startX == 2 || startX - endX == -2 || endY - startY == 2 || startY - endY == -2){
            frigate--;
            setShipStatus(startX,startY,endX,endY);
            if(frigate <= 0){
                System.out.print("Du hast keine Frigatten mehr");
            }else{}

        }else if (endX - startX == 3 || startX - endX == -3 || endY - startY == 3 || startY - endY == -3){
            cruiser--;
            setShipStatus(startX,startY,endX,endY);
            if(cruiser <= 0){
                System.out.print("Du hast keine Kreuzer mehr");
            }else{}

        }else if(endX - startX == 4 || startX - endX == -4 || endY - startY == 4 || startY - endY == -4){
            battleship--;
            setShipStatus(startX,startY,endX,endY);
            if(battleship <= 0){
                System.out.print("Du hast keine Schlachtschiffe mehr");
            }else{}

        }else{
            System.out.println("Kein zulaessiger Schiffstyp");
        }


     }
    
    
    //Status beim Schiffe setzen aktualisieren
    private void setShipStatus(int startX,int startY,int endX,int endY) {
	 // Hier wird mit der .compare Methode die Differenzen zwischen endX und startX (auch für y) berechnet (wichtig für die Richtung der Schleife)
	    int dx = Integer.compare(endX, startX);
	    int dy = Integer.compare(endY, startY);


	    // verschachtelte schleifen durchlaufen Reihe (x) und Spalten (y) und setzt zwischen StartX und Endx (auch y) sowie jeweils den zwischenraum dx (bzw dy) ein "s"
	    for (int x = startX; x != endX + dx; x += dx) {
	        for (int y = startY; y != endY + dy; y += dy) {
	            playerMatrix[x][y] = 's'; 
	        }
	    } 
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
