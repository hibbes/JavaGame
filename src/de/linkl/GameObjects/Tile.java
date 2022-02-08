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
    int type;

    public Tile(int x, int y, int type ,ObjectID id) {
        super(x, y, id);
        this.id = id;
        this.type = type;
        width = 32;
        height = 32;

        try {
            fullimage = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/ground_tileset.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        switch (type) {
            case 0:                                                                                                 // einzelner Block
                image = fullimage.getSubimage(64, 256, 32, 32);
                break;
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
