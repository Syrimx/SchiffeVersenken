
# AOP Brainstorming (Schiffe versenken)
-----
Ideen:
MVC -> Model View Controller

Source
https://www.programmierenlernenhq.de/die-spielflaeche-fuer-unser-java-spiel-programmieren/

### Model:
Datenstruktur -> HashMap mit 2 Spieler*in Objekten

![image](https://github.com/Syrimx/AOPProjekt/assets/85241515/721043e0-5418-47ff-8630-eec43fec8168)

Spielfeld durch mehrdimensionale Arrays darstellen lassen

belegung in der vorbereitungsphase
x -> wasser 
0 -> schiff

veränderung in der gamephase
y -> ausgewählt -> (kann nicht nochmal ausgewählt werden)
1 -> schiffteil getroffen -> (changebal state)

Spielfeld Ansatz1
[[y,x,x,x]				
 [x,x,1,0]
 [x,x,x,x]]

![image](https://github.com/Syrimx/AOPProjekt/assets/85241515/bb175cf9-e490-4489-aed6-cbf274931db0)


 100x100 swing window
 10x10 spielfeld

 jedes Jframe(pixel) 10x10
 wenn wir auf pixel links oben



## Controller: 
1 Vorbereitungsphase
	Spieler instanzieren
	Feldarray erstellen
	Feld zeichnen
	Spieler*in schiffe setzen lassen (!Computer schiffe setzen ?)
		* Schiffüberlagerung vermeiden -> wie?
	Computer Schiffe setzen lassen -> wie Spielregeln befolgen ?
	Übergang in zweite Phase

2 Spielphase
	* feld auswählen
	* getroffen/nicht getroffen ? ()

3 Endphase
	* Abschlussbedingung
		* Schiffe bei einer Spieler*in leer


## View:
Spielfeld zeichnen (GUI)
(Game Panel Klasse)
https://www.programmierenlernenhq.de/die-spielflaeche-fuer-unser-java-spiel-programmieren/ 
-> backend array interaktion frontend array ? 
	 -> backend array feld aufrufbar durch array[0][0] -> y
	 -> frontend array 
	 -> clickState in swing (Docs) 
	 	-> gibt ein tupel zurück

--

## Fragen:
Spielfeld zeichnen ?
Wie Spielfeld darstellen ?

* friendly fire vermeiden 
* Ein oder zwei spielfelder anzeigen 
	* eigene schiffaubau 

Spielregeln ?
* 10 * 10 
* max zwei spieler
* 10 schiffe
	* 1x schlachtschiff 5
	* 2x 4k
	* 3x 3k
	* 4x 2k
* schiffe durch nicht berühren
* nicht überlappen -> test
* nicht diagonal

GameMechanik
schießen? 
* if kästchen
	* belegt ? treffer ?
	* mit spielfeld array abgleichen

Spieler / Computer ?
* Wie computer aufbauen
	* random kästchen bis getroffen und dann präzisieren (wie präzisieren?)
* Spieler/Spieler
	* Abwechselnd spielen


--

## Outline:
Aufgabe "Schiffe Versenken"

Welche Funktionen ?
Spielfeld zeichnen
Spieler/Spieler
Spieler/Computer


Optionaler Teil:
Netzwerkanbindung (Client Server Model)
Geräusche bei Treffer, nicht Treffer, Sieg, Niederlagen
zusätzliche Gimmicks
	*Schießmodi (Trumpf Schuss)
	*Sonar 