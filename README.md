# JavaGame

Vollständiges 2D-Platformer-Spiel in Java mit eigenem Game-Engine-Framework.

> **Entstehungskontext:** Entwickelt von einem Schüler (Package `de.linkl`) –
> zeigt, welche Komplexität in Schülerprojekten erreichbar ist.

## Spielprinzip

Klassischer Side-Scrolling-Plattformer: Spieler läuft, springt, sammelt Münzen
und weicht Gegnern aus. Eigene Engine ohne externe Spielebibliotheken.

## Architektur

```
de.linkl/
├── Main/
│   ├── Main.java        ← Einstiegspunkt, öffnet Menüfenster
│   ├── Game.java        ← Game-Loop (60 FPS), render + update
│   ├── Window.java      ← JFrame-Wrapper
│   ├── Button.java      ← UI-Button
│   └── MenuBackground.java ← Menü-Hintergrund
├── GameObjects/
│   ├── GameObject.java  ← Abstrakte Basisklasse: Position, Physik, ID
│   ├── Player.java      ← Spieler: Eingabe, Animation, Gravitation
│   ├── Bee.java         ← Gegner: Biene
│   ├── Bunny.java       ← Gegner: Hase
│   ├── Coin.java        ← Sammelgegenstand
│   ├── Mushroom.java    ← Plattform-Element
│   ├── Tile.java        ← Spielfeldkachel
│   └── BackgroundObjects/
│       ├── Cloud.java         ← Dekorations-Wolke
│       └── FloatingIsland.java ← Schwebende Insel
└── Handler/
    ├── ObjectHandler.java   ← Verwaltet alle aktiven GameObjects
    ├── AnimationHandler.java ← Sprite-Animationen
    ├── Camera.java          ← Side-Scrolling-Kamera
    ├── CoinHandler.java     ← Münz-Logik
    ├── KeyHandler.java      ← Tastatureingaben
    └── LevelLoader.java     ← Lädt Level aus Ressourcen
```

## Game-Loop (Game.java)

```java
// 60 FPS Game-Loop (Standard-Muster für Spiele ohne Bibliotheken)
while (running) {
    update();   // Physik, Kollision, KI
    render();   // Alle Objekte zeichnen
    // Warte bis 1/60 Sekunde vergangen ist
}
```

## Physik

Gravitation ist hardcodiert als Konstante `g = 0.6f`. Objekte "fallen" bei
jedem Frame um `speedY += g`, bis sie eine Plattform berühren.

## Lernziele / Besonders interessant

- **Abstrakte Klassen**: `GameObject` als Basis für alle Spielobjekte
- **Handler-Muster**: `ObjectHandler` als zentrale Liste aller aktiven Objekte
- **Kamera-Scrolling**: `Camera` verschiebt die Zeichenposition basierend auf der Spielerposition
- **Sprite-Animation**: `AnimationHandler` wechselt Frames basierend auf Zeit
- **Level aus Ressourcen laden**: `LevelLoader` liest Tile-Daten
