package de.linkl.Main;

import javax.swing.*;
import java.awt.*;

public class Window {

    public JFrame frame;
    private Dimension dimension;

    public Window(int width, int height, String title, Game game) {

        frame = new JFrame(title);
        dimension = new Dimension(width, height);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.setPreferredSize(dimension);
        frame.setMaximumSize(dimension);
        frame.setMinimumSize(dimension);
        frame.setLocationRelativeTo(null);

        frame.add(game);
        frame.setVisible(true);

        game.start();

    }

}
