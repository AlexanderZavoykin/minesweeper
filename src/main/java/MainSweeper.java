package main.java;

import main.java.graphic.Gui;
import main.java.graphic.GuiCell;
import main.java.model.MineField;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainSweeper {
    private Gui gui;
    private MineField field;

    private static final Color FRAME_BACKGROUND_COLOR = new Color(105, 105, 105);
    static final Color INFO_PANEL_COLOR = new Color(169, 169, 169);
    static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);
    static final Font FONT = new Font("Times Roman", Font.BOLD, 30);

    static final String BOMB_PATH = "src/main/resources/bomb.png";
    private static final String CHAMP_PATH = "src/main/resources/champagne.png";
    private static final String CLOCK_PATH = "src/main/resources/clock.png";
    private static final String FLAG_PATH = "src/main/resources/flag.png";
    private static final String HAPPY_PATH = "src/main/resources/happy.png";
    private static final String SAD_PATH = "src/main/resources/sad.png";

    //private final int BOMB_CHANCE = 15; // chance to meet the bomb in a cell (per 100)

    private GameDifficulty difficulty;
    private int bombs;
    private int flags;
    private int height;
    private int width;
    private boolean firstClick;
    private boolean inPlay;
    private GuiCell[][] cells;
    private int seconds;
    private Timer timer;

    private JFrame frame;
    private JPanel fieldPanel;
    private JPanel infoPanel;
    private JLabel timerLabel;
    private JLabel counterLabel;
    private JLabel smileLabel;

    Gui(GameDifficulty gameDifficulty) {
        difficulty = gameDifficulty;
        this.height = difficulty.getHeigth();
        this.width = difficulty.getWidth();
        this.bombs = difficulty.getBombs();
        initVars();
        createFrame();
        createFieldPanel();
        createInfoPanel();
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(fieldPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
        frame.setVisible(true);
    }

    private void initVars() {
        flags = 0;
        firstClick = true;
        inPlay = false;
        cells = new GuiCell[height][width];
        seconds = 0;
        timer = new Timer(1000, e -> {
            seconds++;
            String zero = "";
            if (seconds < 10) {
                zero = "0";
            }
            timerLabel.setText(zero + seconds);
        });
    }

    private void createFieldPanel() {
        fieldPanel = new JPanel(new GridLayout(height, width));
        fieldPanel.setBackground(FRAME_BACKGROUND_COLOR);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                GuiCell cell = new GuiCell();
                cell.setName(i + "-" + k);
                cells[i][k] = cell;
                fieldPanel.add(cell);
                cell.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        // get coordinates of the pressed cell
                        GuiCell pressedCell = (GuiCell) e.getSource();
                        String[] xy = pressedCell.getName().split("-");
                        int x = Integer.parseInt(xy[0]);
                        int y = Integer.parseInt(xy[1]);
                        // left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            // initiate new field with bombs if this is the first user`s click
                            if (firstClick) {
                                putBombs(x, y);
                                countBombsAroundEverywhere();
                                firstClick = false;
                                inPlay = true;
                                timer.start();
                            }
                            releaseCell(x, y);
                            if (pressedCell.isReleased()) {
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

    private void createFrame() {
        frame = new JFrame("Minesweeper");
        frame.setIconImage((new ImageIcon(BOMB_PATH)).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(FRAME_BACKGROUND_COLOR);
        frame.setLayout(new BorderLayout());
        // to start or stop timer when frame window is activated or deactivated
        frame.addWindowListener(new WindowAdapter() {
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
    }

    private void createInfoPanel() {
        infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(FRAME_BACKGROUND_COLOR);
        infoPanel.setBorder(BORDER);
        // create timer label
        timerLabel = new JLabel(Util.scaleIcon(CLOCK_PATH, 50));
        timerLabel.setBackground(INFO_PANEL_COLOR);
        timerLabel.setText("00");
        timerLabel.setFont(FONT);
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timerLabel.setVerticalAlignment(SwingConstants.CENTER);
        timerLabel.setPreferredSize(new Dimension(150, 60));
        timerLabel.setOpaque(true);
        // create smile label
        smileLabel = new JLabel(Util.scaleIcon(HAPPY_PATH, 50));
        smileLabel.setBackground(INFO_PANEL_COLOR);
        // to restart game when smileLabel has left button click
        smileLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    restartGame();
                }
            }
        });
        smileLabel.setOpaque(true);
        // create flag counter label
        counterLabel = new JLabel(Util.scaleIcon("flag.png", 50));
        counterLabel.setBackground(INFO_PANEL_COLOR);
        counterLabel.setText(Integer.toString(bombs));
        counterLabel.setFont(FONT);
        counterLabel.setHorizontalAlignment(SwingConstants.CENTER);
        counterLabel.setVerticalAlignment(SwingConstants.CENTER);
        counterLabel.setPreferredSize(new Dimension(100, 60));
        counterLabel.setOpaque(true);

        infoPanel.add(timerLabel, BorderLayout.WEST);
        infoPanel.add(smileLabel, BorderLayout.CENTER);
        infoPanel.add(counterLabel, BorderLayout.EAST);
    }

    private void putPickFlag(int x, int y) {
        GuiCell cell = cells[x][y];
        if (!cell.isReleased()) {
            if (!cell.isFlagged()) {
                // put flag
                if (flags < bombs) {
                    cell.setFlagged(true);
                    flags++;
                    cell.setScaledIcon(FLAG_PATH);
                }
            } else {
                // or pick flag
                cell.setFlagged(false);
                flags--;
                cell.setScaledIcon(null);
            }
            counterLabel.setText(Integer.toString(bombs - flags));
        }
        initYouWin();
    }

    // releases a cell with with coordinates x and y
    private void releaseCell(int x, int y) {
        GuiCell cell = cells[x][y];
        if (!cell.isReleased() && !cell.isFlagged()) {
            cell.release();
            if (cell.isMined()) {
                initGameOver();
                return;
            }
            if (cell.getBombsAround() == 0) {
                releaseCellsAround(x, y);
            }
        }
        initYouWin();
    }

    // releases cells around a cell with with coordinates x and y
    private void releaseCellsAround(int x, int y) {
        if (countFlagsAroundCell(x, y) == cells[x][y].getBombsAround()) {
            for (int i = correctCoor(x - 1, height); i <= correctCoor(x + 1, height); i++) {
                for (int k = correctCoor(y - 1, width); k <= correctCoor(y + 1, width); k++) {
                    releaseCell(i, k);
                }
            }
        }
    }

    void releaseAllCells() {
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                GuiCell cell = cells[i][k];
                cell.setFlagged(false);
                cell.release();
            }
        }
    }



    private void initGameOver() {
        smileLabel.setIcon(Util.scaleIcon(SAD_PATH, 50));
        timer.stop();
        releaseAllCells();
    }

    private void initYouWin() {
        smileLabel.setIcon(Util.scaleIcon(CHAMP_PATH, 50));
        timer.stop();
    }

    private void restartGame() {
        initVars();
        fieldPanel.removeAll();
        createFieldPanel();
        frame.add(fieldPanel);
        timerLabel.setText("00");
    }








}
