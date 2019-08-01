package main.java.model;

public class Cell {
    private int coorX;
    private int coorY;
    private int bombsAround;
    private boolean mined;
    private boolean flagged;
    private boolean released;

    Cell(int x, int y) {
        coorX = x;
        coorY = y;
    }

    public int getCoorX() {
        return coorX;
    }

    public int getCoorY() {
        return coorY;
    }

    public int getBombsAround() {
        return bombsAround;
    }

    public boolean isMined() {
        return mined;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public boolean isReleased() {
        return released;
    }

    void setBombsAround(int bombsAround) {
        this.bombsAround = bombsAround;
    }

    void setMined(boolean mined) {
        this.mined = mined;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    void setReleased(boolean released) {
        this.released = released;
    }
}
