package de.linkl.GameObjects;

import de.linkl.Handler.AnimationHandler;
import de.linkl.State.ObjectID;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Bunny extends BasicEnemy{

    BufferedImage[] runRight;
    BufferedImage[] runLeft;

    AnimationHandler animationHandler;

    public Bunny(int x, int y, ObjectID id) {
        super(x, y, id);
        this.id = ObjectID.ENEMY;
        this.width = 34;
        this.height = 44;
        loadSprites();
        animationHandler = new AnimationHandler();
    }

    public void loadSprites() {
        try {
            BufferedImage[] fullImage = new BufferedImage[2];
            fullImage[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/entity/bunny/bunny_runRight.png")));
            fullImage[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/entity/bunny/bunny_runLeft.png")));

            runRight = new BufferedImage[12];
            runLeft = new BufferedImage[12];

            for (int i=0; i<6; i++) {
                runRight[i] = fullImage[0].getSubimage(i*34, 0, 34, 44);
                runLeft[i] = fullImage[1].getSubimage(374-i*34, 0, 34, 44);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
