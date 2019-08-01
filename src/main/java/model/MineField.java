package main.java.model;

import main.java.GameDifficulty;
import main.java.model.Cell;

public class MineField {
    private Cell[][] cells;
    private int bombs;
    private int height;
    private int width;
    private final int BOMB_CHANCE = 15; // chance to meet the bomb in a cell (per 100)

    MineField(GameDifficulty gf) {
        height = gf.getHeigth();
        width = gf.getWidth();
        bombs = gf.getBombs();
        cells = new Cell[height][width];
    }

    Cell getCell(int x, int y) {
        return cells[x][y];
    }

    // puts bombs to all field`s cells except one with coordinates x and y
    private void putBombs(int x, int y) {
        int counter = 0;
        while (counter <= bombs) {
            for (int i = 0; i < height; i++) {
                for (int k = 0; k < width; k++) {
                    int temp = (int) (Math.random() * (width * height));
                    if ((temp <= BOMB_CHANCE) && (!cells[i][k].isMined()) && (i != x) && (k != y)) {
                        cells[i][k].setMined(true);
                        counter++;
                        if (counter == bombs)
                            break;
                    }
                }
                if (counter == bombs)
                    break;
            }
            if (counter == bombs)
                break;
        }
    }

    // counts number of bombs around each cell and writes it to each cell`s variable 'bombsAround'
    private void countBombsAroundEverywhere() {
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                cells[i][k].setBombsAround(countBombsAroundCell(i, k));
            }
        }
    }

    // counts number of bombs around a cell with coordinates x and y
    private int countBombsAroundCell(int x, int y) {
        int bombsAround = 0;
        for (int i = correctCoor(x - 1, height); i <= correctCoor(x + 1, height); i++) {
            for (int j = correctCoor(y - 1, width); j <= correctCoor(y + 1, width); j++) {
                if (cells[i][j].isMined())
                    bombsAround++;
            }
        }
        if (cells[x][y].isMined())
            bombsAround--;

        return bombsAround;
    }

    // counts number of flags around a cell with coordinates x and y
    private int countFlagsAroundCell(int x, int y) {
        int counter = 0; // counter of flags around
        for (int i = correctCoor(x - 1, height); i <= correctCoor(x + 1, height); i++) {
            for (int j = correctCoor(y - 1, width); j <= correctCoor(y + 1, width); j++) {
                if (cells[i][j].isFlagged()) {
                    counter++;
                }
            }
        }
        if (cells[x][y].isFlagged()) {
            counter--;
        }
        return counter;
    }

    boolean isWon() {
        boolean check = true;
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                if ((cells[i][k].isMined()) && (!cells[i][k].isFlagged())) {
                    check = false;
                    break;
                }
                if ((!cells[i][k].isMined()) && (!cells[i][k].isReleased())) {
                    check = false;
                    break;
                }
            }
        }
        return check;
    }

    // corrects a coordinate if it goes out of cells` height or width
    private static int correctCoor(int coor, int bound) {
        if (coor < 0) {
            coor++;
        }
        if (coor > bound - 1) {
            coor--;
        }
        return coor;
    }

}
