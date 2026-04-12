package de.linkl.GameObjects;

import de.linkl.State.ObjectID;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

/**
 * GameObject.java  –  JavaGame / de.linkl.GameObjects
 *
 * Abstrakte Basisklasse für alle Spielobjekte:
 * Spieler, Gegner (Bee, Bunny, Mushroom…), Tiles (Boden, Plattformen), Münzen.
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Vererbungshierarchie (Beispiel):
 *
 *   GameObject (abstrakt)
 *     ├── Player
 *     ├── Bee, Bunny, Mushroom (Gegner)
 *     ├── Tile (Kacheln)
 *     └── Coin
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Template-Method-Pattern:
 * Die abstrakte Klasse definiert den Rahmen (tick/render/getBounds),
 * konkrete Unterklassen füllen ihn mit spezifischem Verhalten.
 * Der ObjectHandler ruft tick() und render() für jedes Objekt auf –
 * ohne wissen zu müssen, welche konkrete Klasse dahintersteckt (Polymorphismus).
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Physik:
 *   Gravitation: speedY += g (0.6f) pro Tick
 *   Maximale Fallgeschwindigkeit: 20 Pixel/Tick
 *   Kacheln-Kollision: getTotalBounds() / getBoundsTop() geben Hitbox zurück
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Koordinatensystem:
 *   (0,0) ist oben links. x wächst nach rechts, y wächst nach unten.
 *   speedX > 0 = Bewegung nach rechts, speedY > 0 = Bewegung nach unten (Fallen).
 */
public abstract class GameObject {

    // ── Position und Größe ────────────────────────────────────────────────────
    protected int x, y;            // Position oben links des Bounding Box
    protected int width, height;   // Sprite-/Hitbox-Größe in Pixeln

    // ── Physik ────────────────────────────────────────────────────────────────
    protected double scale = 1;             // Skalierungsfaktor (für Sprites)
    protected float speedX, speedY;         // Geschwindigkeit in Pixeln/Tick
    protected final float g = 0.6f;         // Gravitationsbeschleunigung (Pixel/Tick²)
    protected final int maximumFallSpeed = 20; // Maximale Fallgeschwindigkeit (Pixel/Tick)

    // ── Zustand ───────────────────────────────────────────────────────────────
    protected boolean alive       = true;   // false → wird aus der Objektliste entfernt
    protected boolean facingRight = true;   // true = Sprite wird normal gezeichnet
                                            // false = Sprite horizontal gespiegelt
    protected boolean falling     = true;   // true, wenn das Objekt sich im freien Fall befindet
    protected boolean jumping     = false;  // true während eines Sprungbogens

    // ── Debug ──────────────────────────────────────────────────────────────────
    protected boolean showHitbox = false;   // true → Hitbox sichtbar zeichnen (Debugging)

    // ── Identifikation ────────────────────────────────────────────────────────
    /** Typ des Objekts – z.B. PLAYER, BEE, TILE (aus ObjectID-Enum) */
    protected ObjectID id;

    /**
     * Vollständiger Konstruktor: Position, Größe und Typ.
     *
     * @param x       X-Position (Pixel)
     * @param y       Y-Position (Pixel)
     * @param width   Breite in Pixeln
     * @param height  Höhe in Pixeln
     * @param id      Objekttyp (aus {@link de.linkl.State.ObjectID})
     */
    public GameObject(int x, int y, int width, int height, ObjectID id) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.id = id;
    }

    /**
     * Minimalkonstruktor: nur Position (für Objekte ohne feste Größe).
     *
     * @param x   X-Position
     * @param y   Y-Position
     * @param id  Objekttyp
     */
    public GameObject(int x, int y, ObjectID id) {
        this.x = x;
        this.y = y;
    }

    // ── Abstrakte Methoden (Template-Method-Pattern) ──────────────────────────

    /**
     * Zeichnet das Objekt auf das Graphics-Objekt.
     * Wird 1× pro Frame durch den ObjectHandler aufgerufen.
     *
     * Typische Implementierung:
     * <pre>
     *   g.drawImage(sprite, x, y, width, height, null);
     *   if (showHitbox) g.drawRect(x, y, width, height);
     * </pre>
     *
     * @param g AWT-Graphics-Kontext des Spielfensters
     */
    public abstract void render(Graphics g);

    /**
     * Logik-Update: wird 60× pro Sekunde aufgerufen.
     * Hier werden Bewegung, Physik, KI und Kollisionen berechnet.
     *
     * @param objects Liste aller Spielobjekte (für Kollisionsprüfung)
     */
    public abstract void tick(LinkedList<GameObject> objects);

    /**
     * Gibt die vollständige Hitbox (Bounding Rectangle) zurück.
     * Wird für Kollisionserkennung mit Tiles und anderen Objekten genutzt.
     *
     * @return Rechteck, das das Objekt vollständig umschließt
     */
    public abstract Rectangle getTotalBounds();

    /**
     * Gibt die obere Teilhitbox zurück.
     * Wird für Kollision von oben (z.B. Spieler landet auf Plattform) genutzt.
     * Ermöglicht, dass der Spieler durch Plattformen von unten springen kann.
     *
     * @return Rechteck des oberen Teils des Objekts
     */
    public abstract Rectangle getBoundsTop();

    // ── Default-Teilhitboxen ─────────────────────────────────────────────────
    //
    // Viele Objekte (Tiles, Coins, Mushrooms, Background) brauchen keine
    // richtungs­abhängige Kollisions­erkennung – für sie genügt die volle
    // Hitbox. Damit `Player` und `Bunny` aber in ihrer Kollisionsschleife
    // uneingeschränkt `tempObject.getBoundsBottom()` usw. aufrufen können,
    // liefert GameObject hier sinnvolle Defaults zurück (das volle
    // Bounding Rectangle). Player und Bunny überschreiben diese Methoden
    // mit schmalen Teilhitboxen, um von oben/unten/links/rechts getroffene
    // Seiten unterscheiden zu können.

    /** @return untere Teilhitbox (Default: volle Hitbox) */
    public Rectangle getBoundsBottom() {
        return getTotalBounds();
    }

    /** @return rechte Teilhitbox (Default: volle Hitbox) */
    public Rectangle getBoundsRight() {
        return getTotalBounds();
    }

    /** @return linke Teilhitbox (Default: volle Hitbox) */
    public Rectangle getBoundsLeft() {
        return getTotalBounds();
    }

    // ── Getter und Setter ────────────────────────────────────────────────────

    /** @return X-Position des Objekts */
    public int getX()           { return x; }

    /** @return Y-Position des Objekts */
    public int getY()           { return y; }

    /** @return Breite der Hitbox in Pixeln */
    public int getWidth()       { return width; }

    /** @return Höhe der Hitbox in Pixeln */
    public int getHeight()      { return height; }

    /** @return Typ des Objekts */
    public ObjectID getId()     { return id; }

    /** @return true, wenn das Objekt noch aktiv ist */
    public boolean isAlive()    { return alive; }

    /** @param x neue X-Position */
    public void setX(int x)     { this.x = x; }

    /** @param y neue Y-Position */
    public void setY(int y)     { this.y = y; }

    /** @param alive false → Objekt wird beim nächsten Tick entfernt */
    public void setAlive(boolean alive) { this.alive = alive; }
}
