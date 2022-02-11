package de.linkl.GameObjects;

import de.linkl.Handler.KeyHandler;
import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class Player extends GameObject {

    private final float g = 0.6f;                                         // Gravitationskonstante
    private final int maximumFallSpeed = 20;
    private boolean showHitbox = false;

    KeyHandler keyHandler;

    public Player(int x, int y, ObjectID id, KeyHandler keyHandler) {
        super(x, y, id);
        this.id = id;
        this.keyHandler = keyHandler;
        scale = 0.5;
        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/player.png")));
            width = (int) (image.getWidth() * scale);
            height = (int) (image.getHeight() * scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, width, height, null);

        if (showHitbox) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.draw(getBounds());
            g2d.draw(getBoundsTop());
            g2d.draw(getBoundsRight());
            g2d.draw(getBoundsLeft());
        }
    }

    @Override
    public void tick(LinkedList<GameObject> objects) {
        if (falling || jumping) {                                           // lässt den Player fallen, bzw. ändert die y-Geschwindigkeit um g (Gravitationskonstante)
            this.speedY += g;
            if (this.speedY > maximumFallSpeed) {
                speedY = maximumFallSpeed;
            }
        }
        this.x += this.speedX;                                              // ändert die x und y Position um die jeweilige Geschwindigkeit
        this.y += this.speedY;
        input();
        collisions(objects);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + (width / 6), y + (height / 2), width - (width / 3), height - (height / 2));
    }

    public Rectangle getBoundsTop() {
        return new Rectangle(x + (width / 6), y, width - (width / 3), height - (height / 2));
    }

    public Rectangle getBoundsRight() {
        return new Rectangle(x + (width - (width / 10)), y + (height / 4), width / 10, height - (height / 2));
    }

    public Rectangle getBoundsLeft() {
        return new Rectangle(x, y + (height / 4), width / 10, height - (height / 2));
    }

    public void collisions(LinkedList<GameObject> objects) {
        for (GameObject tempObject : objects) {
            if (tempObject.getId() == ObjectID.TILE) {
                if (getBounds().intersects(tempObject.getBounds())) {               // wenn die Hitbox(unten) des Players sich mit der dieses Tiles überschneidet
                    y = tempObject.getY() - height;
                    speedY = 0;
                    falling = false;
                    jumping = false;
                } else {
                    falling = true;
                }

                if (getBoundsTop().intersects(tempObject.getBounds())) {
                    y = tempObject.getY() + tempObject.getHeight();
                    speedY = 0;
                } else {
                    if (getBoundsRight().intersects(tempObject.getBounds())) {          // wenn die Hitbox(rechts) des Players sich mit der dieses Tiles überschneidet
                        x = tempObject.getX() - width;
                        speedX = 0;
                    }

                    if (getBoundsLeft().intersects(tempObject.getBounds())) {           // wenn die Hitbox(links) des Players sich mit der dieses Tiles überschneidet
                        x = tempObject.getX() + tempObject.getWidth();
                        speedX = 0;
                    }
                }
            }
        }
    }

    public void input() {                                                           // wandelt die Eingaben des KeyHandlers in Bewegung um
        if (keyHandler.dPressed) {
            setSpeedX(5);
        }
        if (keyHandler.aPressed) {
            setSpeedX(-5);
        }
        if (!(keyHandler.aPressed || keyHandler.dPressed)) {
            setSpeedX(0);
        }
        if (keyHandler.spacePressed && !jumping) {
            jumping = true;
            setSpeedY(-14);
        }

        if (keyHandler.altPressed && keyHandler.hPressed) {                         // nur Debugging: schaltet Hitboxen an und ausif (showHitbox) {
            showHitbox = false;
        } else {
            showHitbox = true;
        }

    }

}