package de.linkl.Handler;

import de.linkl.GameObjects.GameObject;
import de.linkl.Main.Game;

public class Camera {

    private float x;
    private float y;

    int offsetMaxX = Game.width;
    int offsetMinX = 0;


    public Camera(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void tick(GameObject object) {                                       // bei jedem tick wird die Position der Kamera auf die des Gameobjects bewegt
        x = object.getX() - Game.width / 2f + Game.width/6f;
        if (x > offsetMaxX) {
            x = offsetMaxX;
        }
        else if (x < offsetMinX) {
            x = offsetMinX;
        }
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
