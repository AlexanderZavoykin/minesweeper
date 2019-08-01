package main.java;

import javax.swing.*;
import java.awt.*;

class GuiCell extends JLabel {
    private static final Color MINED_CELL_COLOR = new Color(255, 0, 0);
    private static final Color UNRELEASED_CELL_COLOR = new Color(70, 130, 180);
    private static final Color RELEASED_CELL_COLOR = new Color(192, 192, 192);
    private static final Color ONE_COLOR = new Color(30, 90, 250);
    private static final Color TWO_COLOR = new Color(0, 150, 0);
    private static final Color THREE_COLOR = new Color(250, 0, 20);
    private static final Color FOUR_COLOR = new Color(30, 30, 200);
    private static final Color FIVE_COLOR = new Color(100, 0, 20);
    private static final Color SIX_COLOR = Color.GRAY;
    private static final Color SEVEN_COLOR = new Color(0, 250, 200);
    private static final Color EIGHT_COLOR = Color.BLACK;

    GuiCell() {
        super();
        setPreferredSize(new Dimension(50, 50));
        setBackground(UNRELEASED_CELL_COLOR);
        setBorder(Gui.BORDER);
        setOpaque(true);
    }

    void showBomb() {
        setBackground(MINED_CELL_COLOR);
        setScaledIcon(Gui.BOMB_PATH);
    }

    void showText(int bombsAround) {
        setBackground(RELEASED_CELL_COLOR);
        switch (bombsAround) {
            case 1:
                setText("1", ONE_COLOR);
                break;
            case 2:
                setText("2", TWO_COLOR);
                break;
            case 3:
                setText("3", THREE_COLOR);
                break;
            case 4:
                setText("4", FOUR_COLOR);
                break;
            case 5:
                setText("5", FIVE_COLOR);
                break;
            case 6:
                setText("6", SIX_COLOR);
                break;
            case 7:
                setText("7", SEVEN_COLOR);
                break;
            case 8:
                setText("8", EIGHT_COLOR);
                break;
        }
    }

    // sets scaled icon to the cell
    void setScaledIcon(String path) {
        setIcon(Util.scaleIcon(path, 40));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    private void setText(String text, Color color) {
        setText(text);
        setForeground(color);
        setFont(new Font("Times Roman", Font.BOLD, 30));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }
}
