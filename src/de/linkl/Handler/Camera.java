package de.linkl.Handler;

import de.linkl.GameObjects.GameObject;
import de.linkl.Main.Game;

public class Camera {

    private float x;
    private float y;

    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject object) {                                       // bei jedem tick wird die Position der Kamera auf die des Gameobjects bewegt
        this.x = -object.getX() + Game.width/2;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
