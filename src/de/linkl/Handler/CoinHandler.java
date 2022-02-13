package de.linkl.Handler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class CoinHandler {

    BufferedImage[] firstNum;
    BufferedImage[] secondNum;
    BufferedImage allNumbers;

    public static int collectedCoins = 0;
    private int firstValue = 0;
    private int secondValue = 0;

    public CoinHandler() {
        loadSprites();
    }

    public void tick() {
        firstValue = collectedCoins;
        if (firstValue > 9) {
            secondValue++;
            firstValue = 0;
        }
    }

    public void render(Graphics g) {
        g.drawImage(firstNum[firstValue], 1200, 100, 8, 10, null);
        g.drawImage(secondNum[secondValue], 1208, 100, 8, 10, null);
    }

    public void loadSprites() {
        try {
            allNumbers = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/textGray.png")));

            firstNum = new BufferedImage[10];
            secondNum = new BufferedImage[10];

            for (int i=0; i<10; i++) {
                firstNum[i] = allNumbers.getSubimage(i*8, 30, 8, 10);
                secondNum[i] = firstNum[i];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
