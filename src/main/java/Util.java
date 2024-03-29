package main.java;

import javax.swing.*;
import java.awt.*;

public class Util {

    // returns icon scaled to the defined size
    public static ImageIcon scaleIcon(String path, int size) {
        ImageIcon icon = new ImageIcon(path);
        Image iconImage = icon.getImage();
        Image scaledIconImage = iconImage.getScaledInstance(size, size, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledIconImage);
        return icon;
    }



}
