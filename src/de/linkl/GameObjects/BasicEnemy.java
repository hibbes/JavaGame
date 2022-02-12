package de.linkl.GameObjects;

import de.linkl.State.ObjectID;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public abstract class BasicEnemy extends GameObject {
    private final float g = 0.6f;                                          // Gravitationskonstante
    private final int maximumFallSpeed = 20;
    private boolean showHitbox = false;

    public BasicEnemy(int x, int y, ObjectID id) {
        super(x, y, id);
        this.id = id;
        this.speedX = 2;
        this.scale = 0.5;
        this.alive = true;

        try {
            image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/entity/player/player.png")));
            width = (int) (image.getWidth() * scale);
            height = (int) (image.getHeight() * scale);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics g) {
        g.drawImage(image, x, y, width, height, null);

        /*if (showHitbox) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.draw(getBounds());
            g2d.draw(getBoundsTop());
            g2d.draw(getBoundsRight());
            g2d.draw(getBoundsLeft());
        }*/
    }

    @Override
    public void tick(LinkedList<GameObject> objects) {
        if (!alive){
            objects.remove(this);
        } else {
            if (falling) {                                           // lässt den Gegner fallen, bzw. ändert die y-Geschwindigkeit um g (Gravitationskonstante)
                this.speedY += g;
                if (this.speedY > maximumFallSpeed) {
                    speedY = maximumFallSpeed;
                }
            }
            this.x += this.speedX;                                              // ändert die x und y Position um die jeweilige Geschwindigkeit
            this.y += this.speedY;
            collisions(objects);
        }
    }

    public Rectangle getBoundsBottom() {
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
    @Override
    public Rectangle getTotalBounds() {
        return new Rectangle(x,y,width,height);
    }

    public void collisions(LinkedList<GameObject> objects) {
        for (GameObject tempObject : objects) {
            if (tempObject.getId() == ObjectID.TILE) {

                if (getBoundsBottom().intersects(tempObject.getTotalBounds())) {               // wenn die Hitbox(unten) des Players sich mit der dieses Tiles überschneidet
                    y = tempObject.getY() - height;
                    speedY = 0;
                    falling = false;
                } else {
                    falling = true;
                }

                if (getBoundsRight().intersects(tempObject.getTotalBounds())) {          // wenn die Hitbox(rechts) des Players sich mit der dieses Tiles überschneidet
                    x = tempObject.getX() - width;
                    speedX = -speedX;

                }
                if (getBoundsLeft().intersects(tempObject.getTotalBounds())) {           // wenn die Hitbox(links) des Players sich mit der dieses Tiles überschneidet
                    x = tempObject.getX() + tempObject.getWidth();
                    speedX = -speedX;
                }
            }
        }
    }
}



