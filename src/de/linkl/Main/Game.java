package de.linkl.Main;

import de.linkl.GameObjects.GameObject;
import de.linkl.Handler.*;
import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * Game.java  –  JavaGame / de.linkl.Main
 *
 * Herzstück des Spiels: enthält die Game-Loop und koordiniert alle Subsysteme.
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Architektur-Überblick:
 *
 *   Window  →  erzeugt  →  Game (Canvas + Runnable)
 *                                │
 *                ┌───────────────┼───────────────────┐
 *                │               │                   │
 *          ObjectHandler   BackgroundHandler    CoinHandler
 *          (Spielobjekte)  (Hintergrundobj.)   (Münzen)
 *                │
 *          KeyHandler   Camera   LevelLoader
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Game-Loop (Fixed Timestep):
 *   Das Spiel läuft mit genau 60 "Ticks" pro Sekunde, unabhängig von der
 *   tatsächlichen Framerate. Das wird mit dem "Delta-Zeit"-Prinzip erreicht:
 *
 *   ns = Nanosekunden pro Tick = 1_000_000_000 / 60
 *   delta += (jetzt - letzterZeitpunkt) / ns
 *   solange delta >= 1:  tick(); delta--
 *   render()
 *
 *   Vorteil: Auf langsameren Computern werden Ticks nachgeholt, ohne dass
 *   das Spiel sich schneller oder langsamer anfühlt.
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Vererbung: Game extends Canvas implements Runnable
 *   Canvas:   AWT-Komponente zum direkten Zeichnen, in Window eingebettet.
 *   Runnable: ermöglicht Ausführung in einem separaten Thread.
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Steuerung (Tastenkürzel im Spiel):
 *   A / D      Spieler links/rechts
 *   SPACE      Springen
 *   R          Level neu laden
 *   1 / 2 / 3  Level 1 / 2 / 3 laden
 *   ESC        Pause (maximal 1× pro Sekunde umschaltbar)
 */
public class Game extends Canvas implements Runnable {

    // ── Spielzustand ──────────────────────────────────────────────────────────
    private boolean running = false;   // true, wenn die Spielschleife läuft
    private boolean paused  = false;   // true, wenn das Spiel pausiert ist

    // ── Timing ────────────────────────────────────────────────────────────────
    private double ticksPerSecond = 60; // Ziel: 60 Logik-Updates pro Sekunde
    private int timer = 0;             // Zähler für ESC-Entprellung (1 Sekunde)

    // ── Fenstergröße (statisch → alle Klassen können zugreifen) ──────────────
    public static int width, height;
    public static int totalWidth = 2560; // gesamte Level-Breite in Pixeln

    // ── Hauptthread ──────────────────────────────────────────────────────────
    Thread thread;

    // ── Subsysteme ────────────────────────────────────────────────────────────
    ObjectHandler     objectHandler;      // verwaltet alle Spielobjekte (Spieler, Gegner, Tiles)
    ObjectHandler     backgroundHandler;  // verwaltet Hintergrundobjekte (Clouds, Islands)
    CoinHandler       coinHandler;        // verwaltet Münzen + Punkte-Anzeige
    KeyHandler        keyHandler;         // liest Tastatureingaben
    LevelLoader       levelLoader;        // lädt Level aus Textdateien
    Camera            camera;             // scrollende Kamera, folgt dem Spieler

    // ── Hintergrundbild ───────────────────────────────────────────────────────
    BufferedImage background;

    /**
     * Initialisiert alle Subsysteme und lädt Level 1.
     * Wird beim Start des Game-Threads (run()) aufgerufen.
     */
    public void init() {
        // Fenstergröße speichern (nach dem Layout durch AWT verfügbar)
        width  = this.getWidth();
        height = this.getHeight();

        // Hintergrundbild aus dem Classpath laden
        try {
            background = ImageIO.read(
                Objects.requireNonNull(
                    getClass().getResourceAsStream("/de/linkl/Graphics/map/sky.png")
                )
            );
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Subsysteme initialisieren
        keyHandler        = new KeyHandler();
        objectHandler     = new ObjectHandler();
        backgroundHandler = new ObjectHandler();
        coinHandler       = new CoinHandler();
        camera            = new Camera(0, 0);
        levelLoader       = new LevelLoader(objectHandler, backgroundHandler, keyHandler);

        // Level 1 laden (liest rsc/Level/Level1.txt, erzeugt Tiles + Player)
        levelLoader.load("rsc/Level/Level1.txt");

        // KeyHandler als KeyListener registrieren
        this.addKeyListener(keyHandler);
        this.setFocusable(true);  // Komponente muss fokussierbar sein, um Keys zu empfangen
    }

    /**
     * Startet den Game-Thread.
     * {@code synchronized}: verhindert, dass start() mehrfach aufgerufen wird.
     */
    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        run();  // direkt aufrufen statt thread.start() – läuft im selben Thread
    }

    /**
     * Einstiegspunkt des Game-Threads (Runnable-Interface).
     *
     * <h3>Game-Loop mit Delta-Zeit:</h3>
     * <pre>
     *   lastTime = jetzt
     *   ns = 1_000_000_000 / 60  (Nanosekunden pro Tick)
     *   delta = 0
     *
     *   Endlosschleife:
     *     delta += (jetzt - lastTime) / ns
     *     lastTime = jetzt
     *     solange delta >= 1: tick(); delta--   ← Logik-Updates aufholen
     *     render()                               ← immer genau 1× pro Frame
     *     FPS/TPS alle 1000 ms ausgeben
     * </pre>
     */
    @Override
    public void run() {
        init();               // Subsysteme initialisieren
        this.requestFocus();  // Tastatureingaben empfangen

        long lastTime = System.nanoTime();
        double ns    = 1_000_000_000.0 / ticksPerSecond;  // ns pro Tick
        double delta = 0;
        long   fpsTimer  = System.currentTimeMillis();
        int    updates   = 0;
        int    frames    = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;   // wie viele Ticks seit letztem Frame?
            lastTime = now;

            // Ticks aufholen (bei Rückstand mehr als 1 pro Frame)
            while (delta >= 1) {
                tick();    // Spiellogik 1× updaten
                updates++;
                delta--;
            }

            render();   // Grafik ausgeben (so oft wie möglich)
            frames++;

            // Jede Sekunde FPS und TPS auf der Konsole ausgeben
            if (System.currentTimeMillis() - fpsTimer > 1000) {
                fpsTimer += 1000;
                System.out.println("| FPS: " + frames + " || TICKS: " + updates + " |");
                frames  = 0;
                updates = 0;
            }
        }
    }

    /**
     * Logik-Update: wird 60× pro Sekunde aufgerufen.
     *
     * <ul>
     *   <li>Alle Handler werden aktualisiert (Bewegung, Physik, Kollision)</li>
     *   <li>Kamera folgt dem Spieler-Objekt</li>
     *   <li>Tastatureingaben für Level-Neuladen und Pause werden ausgewertet</li>
     * </ul>
     *
     * ESC-Entprellung: {@code timer} verhindert, dass ESC öfter als 1× pro
     * Sekunde (60 Ticks) schaltet.
     */
    public void tick() {
        if (!paused) {
            objectHandler.tick();      // Spieler, Gegner, Tiles updaten
            backgroundHandler.tick();  // Hintergrundeffekte updaten
            coinHandler.tick();        // Münzanimation updaten

            // Kamera: Spieler suchen und Camera.tick() mit dem Spieler aufrufen
            for (GameObject gameObject : objectHandler.objects) {
                if (gameObject.getId() == ObjectID.PLAYER) {
                    camera.tick(gameObject);  // Kamera folgt dem Spieler
                }
            }

            // Level-Steuerung via Tastatur
            if (keyHandler.rPressed)        levelLoader.load(levelLoader.loadedlevel); // Reload
            if (keyHandler.num1Pressed)     levelLoader.load("rsc/Level/Level1.txt");
            if (keyHandler.num2Pressed)     levelLoader.load("rsc/Level/Level2.txt");
            if (keyHandler.num3Pressed)     levelLoader.load("rsc/Level/Level3.txt");
        }

        // Pause: ESC toggelt paused, aber nur 1× pro 60 Ticks (Entprellung)
        if (keyHandler.escPressed && timer >= 60) {
            paused = !paused;
            timer = 0;
        }
        timer++;
    }

    /**
     * Render-Methode: zeichnet den aktuellen Spielzustand auf den Bildschirm.
     *
     * <h3>Double/Triple Buffering (BufferStrategy):</h3>
     * Ohne Buffering würde das Zeichnen sichtbar flimmern, weil der Bildschirm
     * während des Zeichnens aktualisiert wird.
     * {@link BufferStrategy} mit 3 Puffern: während Puffer A angezeigt wird,
     * wird in Puffer B gezeichnet. Beim nächsten Frame werden A und B getauscht.
     *
     * <h3>Kamera-Transformation:</h3>
     * {@code g2d.translate(-camera.getX(), -camera.getY())} verschiebt
     * den Koordinatenursprung so, dass die Welt korrekt gescrollt erscheint.
     * Nach dem Zeichnen der Spielwelt wird die Transformation rückgängig gemacht
     * (z.B. für HUD-Elemente wie die Münzen-Anzeige).
     *
     * <h3>{@link Graphics#dispose()}:</h3>
     * Gibt Systemressourcen des Graphics-Objekts frei.
     * Muss aufgerufen werden, sobald das Graphics-Objekt nicht mehr gebraucht wird.
     */
    public void render() {
        BufferStrategy bs = this.getBufferStrategy();

        // Beim ersten Aufruf: Triple-Buffer anlegen und sofort zurückgeben
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics   g   = bs.getDrawGraphics();
        Graphics2D g2d = (Graphics2D) g;

        // Hintergrund füllen + Hintergrundbild zeichnen
        g.fillRect(0, 0, width, height);
        g.drawImage(background, 0, 0, Game.width, Game.height, null);

        // ── Kamera-Transformation anwenden ────────────────────────────────
        g2d.translate(-camera.getX(), -camera.getY());

        backgroundHandler.render(g);   // Hintergrundobjekte (Wolken, Inseln)
        objectHandler.render(g);       // Spielobjekte (Spieler, Gegner, Tiles)

        // Münzen-Anzeige: kamerakorrigiert (immer oben rechts im Fenster)
        coinHandler.render(g, (int) camera.getX() + 1200, (int) camera.getY() + 20);

        // ── Kamera-Transformation zurücksetzen ────────────────────────────
        g2d.translate(camera.getX(), camera.getY());

        // Ressourcen freigeben und Puffer wechseln
        g.dispose();
        bs.show();
    }
}
