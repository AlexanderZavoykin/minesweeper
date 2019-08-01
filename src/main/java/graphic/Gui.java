package main.java.graphic;

import main.java.GameDifficulty;
import main.java.Util;
import main.java.graphic.GuiCell;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class Gui {
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

    private GameDifficulty difficulty;

    private JFrame frame;
    private JPanel fieldPanel;
    private JPanel infoPanel;
    private JLabel timerLabel;
    private JLabel counterLabel;
    private JLabel smileLabel;

    Gui(GameDifficulty gameDifficulty) {
        difficulty = gameDifficulty;
        createFrame();
        createFieldPanel();
        createInfoPanel(difficulty.getBombs());
        frame.add(infoPanel, BorderLayout.NORTH);
        frame.add(fieldPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setResizable(false);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        frame.setLocation(screenSize.width / 2 - frame.getWidth() / 2, screenSize.height / 2 - frame.getHeight() / 2);
        frame.setVisible(true);
    }

    private void createFrame() {
        frame = new JFrame("Minesweeper");
        frame.setIconImage((new ImageIcon(BOMB_PATH)).getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(FRAME_BACKGROUND_COLOR);
        frame.setLayout(new BorderLayout());
    }

    private void createFieldPanel() {
        int height = difficulty.getHeigth();
        int width = difficulty.getWidth();
        fieldPanel = new JPanel(new GridLayout(height, width));
        fieldPanel.setBackground(FRAME_BACKGROUND_COLOR);
        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                GuiCell cell = new GuiCell();
                cell.setName(i + "-" + k);
                fieldPanel.add(cell);
            }
        }
    }

    private void createInfoPanel(int bombs) {
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

    public JPanel getFieldPanel() {
        return fieldPanel;
    }

    public JLabel getTimerLabel() {
        return timerLabel;
    }

    public JLabel getCounterLabel() {
        return counterLabel;
    }

    public JLabel getSmileLabel() {
        return smileLabel;
    }
}
