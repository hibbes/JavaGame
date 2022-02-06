package de.linkl.GameObjects;

import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Objects;

public class Tile extends GameObject {

    BufferedImage fullimage;
    BufferedImage image;

    public Tile(int x, int y, ObjectID id) {
        super(x, y, id);
        this.id = id;
        width = 32;
        height = 32;
        try {
            fullimage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/tileset.png")));
            image = fullimage.getSubimage(32, 0, 32, 32);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(Graphics g) {

        if (!(image == null)) {
            g.drawImage(image, x, y, width, height, null);
        }
        else {
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
