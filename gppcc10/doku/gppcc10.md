# #gppcc10

Die 10. Challenge hat das Thema "Gefrorene Welten". Es ist also die Aufgabe, ein beliebiges Spiel mit folgenden Bedingungen zu erstellen:

* das Spiel spielt in einer gefrorenen Welt
* alle Grafiken sind selbst erstellt
* alle Sounds sind selbst erstellt
* alle Musik ist selbst erstellt

## Die Idee

Ein vertikaler Runner mit mehr Spaß als Ernst. Der Spieler rennt auf Eiskacheln, welche von oben nach unten durch das Bild 'fliegen'. Auf diesen Kacheln tauchen Hindernisse (Brocken) und Items (Bonuspunkte) auf. Das Ziel ist es das Ziel zu erreichen.

Besonderheiten:

* Tritt der Spieler auf eine freie Fläche, so fällt er von der Bahn und verliert ein Leben / wird zurück gesetzt.
* Bleibt der Spieler an einem Brocken hängen, so
	* [ ] verliert dieser ein Leben / wird zurück gesetzt.
	* [ ] wird er erst zurück geschoben und wenn er auch den unteren Bildschirmrand berührt, so verliert er ein Leben / wird zurück gesetzt.
* Der Spieler bewegt sich rechts/links
	* [ ] völlig frei von den Kachelgrößen.
	* [ ] an die Kacheln gebunden und kann sich nur zwischen den Spalten hin und her bewegen.
* Die Bahn kann sich auch auftrennen in zwei einzelne Wege, welche durch eine freie Fläche getrennt sind.

## Grafisches

Das Spiel spielt auf einer Eisbahn im Himmel.

* Ebene 'oben': Die Eisbahn und der Spieler. (100% scroll tempo)
* Ebene 'mitte': Eine lockere Wolkendecke (50% **?** scroll tempo)
* Ebene 'unten': Die Welt am Boden. Leicht verschleiert/vernebelt. (20% **?** scroll tempo)