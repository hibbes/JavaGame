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
                    if (nr == 80) {                                                                         // eine Reihe hat hier genau 80 Zeichen
                        nr = 0;
                        row++;
                    }

                    if (object == 0) {

                    } else if (object == 1) {
                        objectHandler.addObject(new Player(nr*32, row*32, ObjectID.PLAYER, keyHandler));
                    } else if (object == 2) {
                        objectHandler.addObject(new Tile(nr*32,row*32, 2, ObjectID.TILE));
                    } else if (object == 3) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 3, ObjectID.TILE));
                    } else if (object == 4) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 4, ObjectID.TILE));
                    } else if (object == 5) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 5, ObjectID.TILE));
                    } else if (object == 6) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 6, ObjectID.TILE));
                    } else if (object == 7) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 7, ObjectID.TILE));
                    } else if (object == 8) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 8, ObjectID.TILE));
                    } else if (object == 9) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 9, ObjectID.TILE));
                    } else if (object == 10) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 10, ObjectID.TILE));
                    } else if (object == 11) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 11, ObjectID.TILE));
                    } else if (object == 12) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 12, ObjectID.TILE));
                    } else if (object == 13) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 13, ObjectID.TILE));
                    } else if (object == 14) {
                        objectHandler.addObject(new Tile(nr * 32, row * 32, 14, ObjectID.TILE));
                    }
                }
                levelloaded = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
