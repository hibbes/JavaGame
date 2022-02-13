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
    private final int scale = 3;

    public CoinHandler() {
        loadSprites();
    }

    public void tick() {
        firstValue = collectedCoins - 10*secondValue;
        if (firstValue > 9) {
            secondValue++;
            firstValue = 0;
        }
    }

    public void render(Graphics g, int x, int y) {
        g.drawImage(firstNum[firstValue], x + 8*scale, y, 8*scale, 10*scale, null);
        g.drawImage(secondNum[secondValue], x, y, 8*scale, 10*scale, null);
    }

    public void loadSprites() {
        try {
            allNumbers = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/de/linkl/Graphics/textgray.png")));

            firstNum = new BufferedImage[11];
            secondNum = new BufferedImage[11];

            for (int i=0; i<10; i++) {
                firstNum[i] = allNumbers.getSubimage(i*8, 30, 8, 10);
                secondNum[i] = firstNum[i];
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
