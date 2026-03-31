package de.linkl.Handler;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * KeyHandler.java  –  JavaGame / de.linkl.Handler
 *
 * Verwaltet den Tastaturzustand des Spiels.
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Designprinzip – "Pressed-Flag"-Muster:
 *   Statt eine Taste zu "verbrauchen" (einmalig reagieren), werden
 *   öffentliche boolean-Felder gesetzt/gelöscht.
 *   Die Spiellogik (Game.tick()) liest diese Felder 60× pro Sekunde.
 *
 *   keyPressed  → setzt Flag auf true
 *   keyReleased → setzt Flag auf false
 *
 *   Vorteil: Mehrere Klassen können gleichzeitig denselben Tastenzustand lesen.
 *   Nachteil: Keine automatische Entprellung – muss bei Bedarf manuell
 *             implementiert werden (z.B. ESC in Game.tick() mit timer).
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Tastenbelegung:
 *   A / D      → links / rechts (Spielerbewegung)
 *   SPACE      → Springen
 *   ALT        → (reserviert)
 *   ESC        → Pause (entprellt in Game.tick())
 *   R          → Level neu laden
 *   1–6        → Level- oder Modus-Auswahl
 *
 * ──────────────────────────────────────────────────────────────────────────────
 * Verwendung:
 *   KeyHandler kh = new KeyHandler();
 *   canvas.addKeyListener(kh);  // registrieren
 *
 *   // In der Game-Loop:
 *   if (kh.dPressed) spieler.bewegeRechts();
 */
public class KeyHandler implements KeyListener {

    // ── Bewegungstasten ────────────────────────────────────────────────────────
    /** true, solange D gedrückt gehalten wird → Bewegung nach rechts */
    public boolean dPressed;
    /** true, solange A gedrückt gehalten wird → Bewegung nach links */
    public boolean aPressed;
    /** true, solange SPACE gedrückt gehalten wird → Sprung */
    public boolean spacePressed;
    /** true, solange ALT gedrückt gehalten wird (reserviert) */
    public boolean altPressed;

    // ── Sondertasten ───────────────────────────────────────────────────────────
    /** true, sobald R gedrückt wird → Level neu laden */
    public boolean rPressed;
    /** true, sobald ESC gedrückt wird → Pause (Game.tick() entprellt) */
    public boolean escPressed;

    // ── Zahlentasten 1–6 ──────────────────────────────────────────────────────
    /** Zahlentasten 1–6 für Level-/Modus-Auswahl */
    public boolean num1Pressed, num2Pressed, num3Pressed,
                   num4Pressed, num5Pressed, num6Pressed;

    /**
     * Wird von AWT aufgerufen, wenn eine Taste eingetippt wird (pressed+released).
     * Hier nicht benötigt – Zustand wird in keyPressed/keyReleased verwaltet.
     */
    @Override
    public void keyTyped(KeyEvent e) {
        // nicht verwendet
    }

    /**
     * Wird aufgerufen, wenn eine Taste gedrückt wird.
     * Setzt das entsprechende Flag auf {@code true}.
     *
     * {@link KeyEvent#getKeyCode()} gibt den virtuellen Tastencode zurück,
     * unabhängig von der Tastaturbelegung (z.B. VK_D = immer "D").
     *
     * @param e das KeyEvent mit Tastencode und Modifier-Bits
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D)      dPressed      = true;
        if (e.getKeyCode() == KeyEvent.VK_A)      aPressed      = true;
        if (e.getKeyCode() == KeyEvent.VK_SPACE)  spacePressed  = true;
        if (e.getKeyCode() == KeyEvent.VK_ALT)    altPressed    = true;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) escPressed    = true;
        if (e.getKeyCode() == KeyEvent.VK_R)      rPressed      = true;
        if (e.getKeyCode() == KeyEvent.VK_1)      num1Pressed   = true;
        if (e.getKeyCode() == KeyEvent.VK_2)      num2Pressed   = true;
        if (e.getKeyCode() == KeyEvent.VK_3)      num3Pressed   = true;
        if (e.getKeyCode() == KeyEvent.VK_4)      num4Pressed   = true;
        if (e.getKeyCode() == KeyEvent.VK_5)      num5Pressed   = true;
        if (e.getKeyCode() == KeyEvent.VK_6)      num6Pressed   = true;
    }

    /**
     * Wird aufgerufen, wenn eine Taste losgelassen wird.
     * Setzt das entsprechende Flag auf {@code false}.
     *
     * Wichtig: Ohne keyReleased würde z.B. dPressed nie auf false gesetzt
     * und der Spieler liefe ewig nach rechts, auch wenn D losgelassen wird.
     *
     * @param e das KeyEvent
     */
    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D)      dPressed      = false;
        if (e.getKeyCode() == KeyEvent.VK_A)      aPressed      = false;
        if (e.getKeyCode() == KeyEvent.VK_SPACE)  spacePressed  = false;
        if (e.getKeyCode() == KeyEvent.VK_ALT)    altPressed    = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) escPressed    = false;
        if (e.getKeyCode() == KeyEvent.VK_R)      rPressed      = false;
        if (e.getKeyCode() == KeyEvent.VK_1)      num1Pressed   = false;
        if (e.getKeyCode() == KeyEvent.VK_2)      num2Pressed   = false;
        if (e.getKeyCode() == KeyEvent.VK_3)      num3Pressed   = false;
        if (e.getKeyCode() == KeyEvent.VK_4)      num4Pressed   = false;
        if (e.getKeyCode() == KeyEvent.VK_5)      num5Pressed   = false;
        if (e.getKeyCode() == KeyEvent.VK_6)      num6Pressed   = false;
    }
}
