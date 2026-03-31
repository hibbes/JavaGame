package de.linkl.Main;

/**
 * Main.java  –  JavaGame / de.linkl.Main
 *
 * Einstiegspunkt des Spiels.
 *
 * <h3>Was passiert beim Start?</h3>
 * Es wird ein {@link Window}-Objekt mit den Maßen 1280×710 Pixel erstellt.
 * Window öffnet das Menü-Fenster. Von dort kann der Spieler das Spiel starten,
 * was dann ein neues Game-Objekt in ein zweites Fenster lädt.
 *
 * <h3>Auskommentierte Variante:</h3>
 * Die zweite Zeile
 * <pre>
 *   new Window(1280, 710, "Java Game", new Game());
 * </pre>
 * würde direkt (ohne Menü) ins Spiel springen. Das ist nützlich beim Entwickeln,
 * um den Ladevorgang zu überspringen.
 */
public class Main {
    /**
     * Programmeinstiegspunkt: öffnet das Spielfenster mit dem Hauptmenü.
     *
     * @param args Kommandozeilenargumente (werden nicht verwendet)
     */
    public static void main(String[] args) {
        // Menü-Fenster öffnen (1280×710 Pixel, Titel "Java Game")
        Window menuWindow = new Window(1280, 710, "Java Game");

        // Direktstart ins Spiel (ohne Menü) – zum Debuggen nützlich:
        // new Window(1280, 710, "Java Game", new Game());
    }
}
