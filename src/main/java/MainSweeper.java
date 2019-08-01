package main.java;

import main.java.graphic.Gui;
import main.java.graphic.GuiCell;
import main.java.model.Cell;
import main.java.model.MineField;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainSweeper {
    private Gui gui;
    private MineField field;

    private GameDifficulty difficulty;
    private int bombs;
    private int flags;
    private int height;
    private int width;
    private boolean firstClick;
    private boolean inPlay;
    private int seconds;
    private Timer timer;

    private MainSweeper(GameDifficulty gameDifficulty) {
        difficulty = gameDifficulty;
        this.height = difficulty.getHeigth();
        this.width = difficulty.getWidth();
        this.bombs = difficulty.getBombs();
        flags = 0;
        firstClick = true;
        inPlay = false;
        seconds = 0;
        gui = new Gui(gameDifficulty);
        field = new MineField(gameDifficulty);
        setListeners(gui, field);
        timer = createTimer();
    }

    private Timer createTimer() {
        Timer t = new Timer(1000, e -> {
            seconds++;
            String zero = "";
            if (seconds < 10) {
                zero = "0";
            }
            gui.getTimerLabel().setText(zero + seconds);
        });
        return t;
    }

    private void setListeners(Gui gui, MineField field) {
        // start or stop timer when activate or desactivate game window
        gui.getFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowDeactivated(WindowEvent e) {
                timer.stop();
            }

            @Override
            public void windowActivated(WindowEvent e) {
                if (inPlay)
                    timer.start();
            }
        });
        // restart game when click on smile label
        gui.getSmileLabel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    restartGame();
                }
            }
        });
        // add event for mouse clicks on graphic cells
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                gui.getCells()[i][k].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        // get coordinates of the pressed cell
                        GuiCell pressedCell = (GuiCell) e.getSource();
                        String[] xy = pressedCell.getName().split("-");
                        int x = Integer.parseInt(xy[0]);
                        int y = Integer.parseInt(xy[1]);
                        Cell cell = field.getCell(x, y);
                        // left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            // initiate new field with bombs if this is the first user`s click
                            if (firstClick) {
                                field.putBombs(x, y);
                                field.countBombsAroundEverywhere();
                                firstClick = false;
                                inPlay = true;
                                timer.start();
                            }
                            releaseCell(x, y);
                            if (cell.isReleased()) {
                                releaseCellsAround(x, y);
                            }
                        }
                        // right click
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            putPickFlag(x, y);
                        }
                    }
                });
            }
        }
    }

    private void putPickFlag(int x, int y) {
        GuiCell gCell = gui.getCells()[x][y];
        Cell cell = field.getCell(x, y);
        if (!cell.isReleased()) {
            if (!cell.isFlagged()) {
                // put flag
                if (flags < bombs) {
                    cell.setFlagged(true);
                    flags++;
                    gCell.setScaledIcon(Gui.FLAG_PATH);
                }
            } else {
                // or pick flag
                cell.setFlagged(false);
                flags--;
                gCell.setScaledIcon(null);
            }
            gui.getCounterLabel().setText(Integer.toString(bombs - flags));
        }
        initYouWin();
    }

    // releases a cell with with coordinates x and y
    private void releaseCell(int x, int y) {
        GuiCell gCell = gui.getCells()[x][y];
        Cell cell = field.getCell(x, y);
        if (!cell.isReleased() && !cell.isFlagged()) {
            cell.setReleased(true);
            if (cell.isMined()) {
                gCell.showBomb();
                initGameOver();
                return;
            } else {
                if (cell.getBombsAround() == 0) {
                    releaseCellsAround(x, y);
                }
                gCell.showText(cell.getBombsAround());
            }
        }
        initYouWin();
    }

    // releases cells around a cell with with coordinates x and y
    private void releaseCellsAround(int x, int y) {
        Cell cell = field.getCell(x, y);
        if (field.countFlagsAroundCell(x, y) == cell.getBombsAround()) {
            for (int i = MineField.correctCoor(x - 1, height); i <= MineField.correctCoor(x + 1, height); i++) {
                for (int k = MineField.correctCoor(y - 1, width); k <= MineField.correctCoor(y + 1, width); k++) {
                    releaseCell(i, k);
                }
            }
        }
    }

    private void releaseAllCells() {
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                GuiCell gCell = gui.getCells()[i][k];
                Cell cell = field.getCell(i, k);
                cell.setFlagged(false);
                if (cell.isMined()) {
                    gCell.showBomb();
                } else {
                    gCell.showText(cell.getBombsAround());
                }
            }
        }
    }

    private void initGameOver() {
        gui.getSmileLabel().setIcon(Util.scaleIcon(Gui.SAD_PATH, 50));
        timer.stop();
        releaseAllCells();
    }

    private void initYouWin() {
        if (field.isWon()) {
            gui.getSmileLabel().setIcon(Util.scaleIcon(Gui.CHAMP_PATH, 50));
            timer.stop();
            field.makeCellsInactive();
        }
    }

    private void restartGame() {

    }

    public static void main(String[] args) {
        MainSweeper ms = new MainSweeper(GameDifficulty.EASY);
    }

}
