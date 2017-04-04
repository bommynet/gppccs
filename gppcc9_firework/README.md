# Fireworks
Ursprünglich sollte dieses Projekt eine kleine Spielerei werden, um die Ideen von [RainbowCoder](http://twitter.com/shiffman) aufzugreifen und in Java umzusetzen. Aktuell hat es sich zu meinem Beitrag zur [#gppcc9](http://www.youtube.com/watch?v=ivNN8qjooCQ) entwickelt.

## Das Spiel
Das Spielprinzip ist schnell und einfach erklärt.

1. per Leertaste kann man ein zufälliges Feuerwerk zünden
2. jedes Feuerwerk wird gezählt
3. die maximal erreichte Anzahl gleichzeitig auf dem Bild vorhandene Feuerwerke werden gezählt
4. man hat nur begrenzt Zeit, um möglichst viele Feuerwerke zu zünden

## Todo
* [ ] Sounds einfügen
* [x] Highscore speichern
* [ ] Highscore laden
* [ ] Feuerwerk: Rakete mit blitzenden Partikeln
* [x] Rakete steigt auf
* [x] Explosion am Scheitelpunkt
* [x] Verschiedene Farben je Rakete
* [x] Feuerwerk: Rakete Standard
* [x] Feuerwerk: Rakete mit zwei Farben
* [x] Feuerwerk: Rakete simpel und klein
* [x] Feuerwerk: Kanonenschlag
* [ ] ~~Feuerwerk: Rakete Gold-/Silberregen~~

## Ausblick
Ich werde weiter an diesem Spiel arbeiten und kleine Erweiterungen hinzufügen. So denke ich aktuell über einen weiteren Spielmodus nach, bei dem man z. B. im Takt Feuerwerke abfeuern muss.

Ebenfalls geplant ist eine Umsetzung für Android. Da das Spiel eh nur eine Taste benutzen darf (siehe [#gppcc9](http://www.youtube.com/watch?v=ivNN8qjooCQ)), kann die Eingabe leicht geändert werden auf einen Screen-touch.

Eventuell werden die Highscores in Zukunft nicht nur gespeichert, sondern zentral auf einem Server gesichert. Auch eine "Teilen"-Funktion in Richtung Twitter oder FB fände ich interessant.

## Fehler beheben
* [x] ~~Beim Verändern der Bildgröße sollen die Raketen dennoch nur bis zum oberen Rand und nicht darüber hinaus fliegen.~~ *Derzeit feste Spielfeldgröße.*
* [x] ~~Der Partikel-Trail-Effekt wird durch transparentes Löschen des Bildschirms erschaffen, was jedoch dazu führt, dass das Bild "Reste" der Raketen behält.~~ *Scheint nur im Vollbild aufzutauchen und wird aktuell durch blitzende Übergänge (Bild einen Frame lang weiß zeichnen) verhindert.*
