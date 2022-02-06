package de.linkl.GameObjects;

import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class Tile extends GameObject {

    BufferedImage image;

    public Tile(int x, int y, ObjectID id) {
        super(x, y, id);
        this.id = id;
        width = 64;                                                     // nur 64x64 Bilder verwenden!
        height = 64;
        if (id == ObjectID.BACKGROUND) {
            try {
                image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/spaceBackground.png")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void render(Graphics g) {
        if (id == ObjectID.BACKGROUND) {
            g.drawImage(image, x, y, width, height, null);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(x, y, width, height);
        }
    }

    @Override
    public void tick(LinkedList<GameObject> objects) {

    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, getWidth(), getHeight());
    }
}
