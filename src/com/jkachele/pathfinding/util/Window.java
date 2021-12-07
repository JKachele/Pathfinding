package com.jkachele.pathfinding.util;

import java.awt.*;
import javax.swing.*;

public class Window {
    private final JFrame frame;
    public Window(JPanel panel, Dimension size){
        frame = new JFrame("Pathfinding");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        frame.setSize(size);
        frame.setLocationRelativeTo(null);
        frame.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }
}
