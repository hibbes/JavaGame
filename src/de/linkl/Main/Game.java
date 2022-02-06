package de.linkl.Main;

import de.linkl.GameObjects.Player;
import de.linkl.GameObjects.Tile;
import de.linkl.Handler.KeyHandler;
import de.linkl.Handler.LevelLoader;
import de.linkl.Handler.ObjectHandler;
import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Game extends Canvas implements Runnable{

    private boolean running = false;
    private double ticksPerSecond = 60;
    public static int width, height;

    Thread thread;
    ObjectHandler objectHandler;
    KeyHandler keyHandler;
    LevelLoader levelLoader;
    BufferedImage background;
    BufferedImage background2;

    public void init() {
        width = this.getWidth();
        height = this.getHeight();
        try {
            background = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/background5.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        keyHandler = new KeyHandler();
        objectHandler = new ObjectHandler();
        levelLoader = new LevelLoader(objectHandler, keyHandler);
        levelLoader.load("rsc/Level/Level1.txt");

        this.addKeyListener(keyHandler);
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        run();
    }

    @Override
    public void run() {                                                                             // wird ausgeführt wenn der Thread gestartet wird
        init();                                                                                     // beinhaltet die Game-Loop, dass das Spiel auf einer
        this.requestFocus();                                                                        // bestimmten Geschwindigkeit läuft (hier: 60 ticks pro Sekunde)

        long lastTime = System.nanoTime();
        double ns = 1000000000 / ticksPerSecond;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int updates = 0;
        int frames = 0;
        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                tick();
                updates++;
                delta--;
            }
            render();
            frames++;

            if (System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                System.out.println("| FPS: " + frames + " || TICKS: " + updates + " |");
                frames = 0;
                updates = 0;
            }
        }
    }

    public void tick() {                                                // "updatet" die Informationen bei jedem Tick
        objectHandler.tick();

        if (keyHandler.num2Pressed) {
            levelLoader.load("rsc/Level/Level2.txt");
        }
        if (keyHandler.num3Pressed) {
            levelLoader.load("rsc/Level/Level3.txt");
        }
    }

    public void render() {                                              // stellt die anzuzeigenden Objects dar
        BufferStrategy bs = this.getBufferStrategy();                   // BufferStrategy verwaltet was auf dem Bildschirm angezeigt wird
                                                                        // am Anfang ist diese null, weshalb hier erst eine erstellt wird und dann die Methode nochmal aufruft
        if (bs == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();


        g.drawImage(background, 0, 0, Game.width, Game.height, null);
        objectHandler.render(g);

        g.dispose();                                                    // dispose() ist eine Methode, die die benötigten Systemressourcen,
        bs.show();                                                      // welche für das Objekt benötigt, freigibt
    }

    public static void main(String[] args) {
        new Window(1280, 720, "Java Game", new Game());
    }
}
