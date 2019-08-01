package main.java;

import javax.swing.*;
import java.awt.*;

class Cell extends JLabel {
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

    private int coorX;
    private int coorY;
    private int bombsAround;
    private boolean mined;
    private boolean flagged;
    private boolean released;

    Cell(int coorX, int coorY) {
        super();
        released = false;
        setPreferredSize(new Dimension(50, 50));
        setBackground(UNRELEASED_CELL_COLOR);
        setBorder(Gui.BORDER);
        setOpaque(true);
        this.coorX = coorX;
        this.coorY = coorY;
    }

    void release() {
            released = true;
            if (mined) {
                setBackground(MINED_CELL_COLOR);
                setScaledIcon(Gui.BOMB_PATH);
            } else {
                setBackground(RELEASED_CELL_COLOR);
                showText();
            }
    }

    private void showText() {
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

    private void setText(String text, Color color) {
        setText(text);
        setForeground(color);
        setFont(new Font("Times Roman", Font.BOLD, 30));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    // sets scaled icon to the cell with coordinates x and y
    void setScaledIcon(String path) {
        setIcon(Util.scaleIcon(path, 40));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
    }

    int getCoorX() {
        return coorX;
    }

    int getCoorY() {
        return coorY;
    }

    int getBombsAround() {
        return bombsAround;
    }

    boolean isMined() {
        return mined;
    }

    boolean isFlagged() {
        return flagged;
    }

    boolean isReleased() {
        return released;
    }

    void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    void setMined(boolean mined) {
        this.mined = mined;
    }

    void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

}
