package de.linkl.GameObjects;
import de.linkl.Handler.AnimationHandler;
import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class Coin extends GameObject {

    AnimationHandler animationHandler;
    BufferedImage[] spinCoin;

    public Coin(int x, int y, ObjectID id) {
        super(x, y, id);
        this.id = ObjectID.COIN;
        this.width = 25;
        this.height = 25;
        this.alive = true;
        loadSprites();
        animationHandler = new AnimationHandler();
        animationHandler.setAnimation(spinCoin);
        animationHandler.setDelay(120);
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
        return new Rectangle(x, y, width, height);
    }


    @Override
    public void render(Graphics g) {

        g.drawImage(animationHandler.getImage(), x, y, width, height, null);

        /*if (showHitbox) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.RED);
            g2d.draw(getBoundsBottom());
            g2d.draw(getBoundsTop());
            g2d.draw(getBoundsRight());
            g2d.draw(getBoundsLeft());
        }*/
    }

    @Override
    public void tick(LinkedList<GameObject> objects) {
        if (!alive) {
            objects.remove(this);
        }
        animationHandler.tick();
    }
        public void loadSprites () {
            try {
                BufferedImage fullImage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/entity/coin/coin.png")));
                spinCoin = new BufferedImage[6];

                for (int i=0; i<6; i++) {
                    spinCoin[i] = fullImage.getSubimage(i * 32, 0, 32, 32);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}

