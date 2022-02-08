package de.linkl.Handler;

import de.linkl.GameObjects.GameObject;
import de.linkl.GameObjects.Player;
import de.linkl.GameObjects.Tile;
import de.linkl.State.ObjectID;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LevelLoader {

    private int nr, row;                                                                                // Anzahl an Zeichen pro Reihe und Anzahl an Reihen
    private boolean levelloaded;

    Scanner scanner;                                                                                    // Klasse von Java, die einfache Zeichen lesen kann
    File file;
    ObjectHandler objectHandler;
    KeyHandler keyHandler;

    public LevelLoader(ObjectHandler objectHandler, KeyHandler keyHandler) {
        this.keyHandler = keyHandler;
        this.objectHandler = objectHandler;
        this.nr = -1;
        this.row = 0;
        this.levelloaded = false;
    }

    public void load(String path) {                                                                     // Methode um eine Txt Datei zu laden
        if (levelloaded) {
            objectHandler.removeAll();
            levelloaded = false;
            load(path);
        }
        else {
            file = new File(path);                                                                          // Festlegung des Pfades für die Datei

            try {
                scanner = new Scanner(file);                                                                // Scanner ließt die Datei
                row = 0;
                nr = -1;

                while (scanner.hasNextInt()) {                                                              // "während es ein nächstes Zeichen (int) gibt"
                    int object = scanner.nextInt();                                                         // int object meint die aktuelle Zahl die der Scanner gelesen hat
                    nr++;
                    if (nr == 40) {                                                                         // eine Reihe hat hier genau 20 Zeichen
                        nr = 0;
                        row++;
                    }

                    if (object == 0) {

                    } else if (object == 1) {
                        objectHandler.addObject(new Tile(nr*32,row*32, 0, ObjectID.TILE));
                    } else if (object == 2) {
                        objectHandler.addObject(new Player(nr*32, row*32, ObjectID.PLAYER, keyHandler));
                    }
                }
                levelloaded = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
