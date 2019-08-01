package main.java;

public enum GameDifficulty {
    EASY (10, 10, 15),
    MEDIUM (16, 16,40),
    HARD (16, 30, 75);

    private int heigth;
    private int width;
    private int bombs;

    GameDifficulty(int heigth, int width, int bombs) {
        this.heigth = heigth;
        this.width = width;
        this.bombs = bombs;
    }

    public int getHeigth() {
        return heigth;
    }

    public int getWidth() {
        return width;
    }

    public int getBombs() {
        return bombs;
    }
}
