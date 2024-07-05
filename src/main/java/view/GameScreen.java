package view;

import controller.client.ClientController;
import game.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class GameScreen extends JPanel {
    private final ArrayList<CarShape> carShapes;
    private GameState gameState;
    private Player currentPlayer;
    private JTextPane typingArea;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel providedTextLabel;
    public static String providedText;
    private Timer timer;
    private JLabel timeLabel;
    private java.util.List<TypingPlayer> racers;
    private JPanel carPanel;
    private boolean timerStarted = false;
    private java.util.List<ResultScreen> resultScreens = new ArrayList<>();
    private typerace typerace;


    private long startTime;
    public static int keyPressCount;

    private ClientController clientController;
    private Image backgroundImage;
    private Font customFont;

    public GameScreen(GameState gameState, Player currentPlayer, ClientController clientController, String providedText, typerace typerace) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = providedText;
        this.keyPressCount = 0;
        this.carShapes = new ArrayList<>();
        this.clientController = clientController;
        this.racers = gameState.getPlayers();
        this.typerace= typerace;

        // to use the background image
        try {
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("GameScreenBG.jpeg");
            if (imageStream != null) {
                backgroundImage = ImageIO.read(imageStream);
            } else {
                System.err.println("Image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // to use custom font
        try {
            InputStream fontStream = getClass().getClassLoader().getResourceAsStream("nougat.ttf");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(18f); // Set default size
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(customFont);
            } else {
                System.err.println("Font not found");
            }
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }

        initComponents();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }


    private void initComponents() {
        setLayout(new BorderLayout());

        // Creating a Car Panel
        carPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int roadLength = (int) (getWidth() * 0.7); // 70% of the panel width
                for (CarShape carShape : carShapes) {
                    carShape.draw(g, roadLength);
                }
            }
        };
        carPanel.setOpaque(false);
        carPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        carPanel.setPreferredSize(new Dimension(600, 300));
        add(carPanel, BorderLayout.NORTH);

        // Provided text label
        providedTextLabel = new JLabel("<html><p style=\"width: 350px; color: white;\">" + providedText + "</p></html>");
        providedTextLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        providedTextLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        providedTextLabel.setHorizontalAlignment(SwingConstants.LEFT);
        providedTextLabel.setPreferredSize(new Dimension(500, 100));
        providedTextLabel.setForeground(Color.WHITE);

        // Typing area
        typingArea = new JTextPane();
        typingArea.setFont(customFont.deriveFont(Font.BOLD, 18f));
        typingArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        typingArea.setPreferredSize(providedTextLabel.getPreferredSize());
        typingArea.setEditable(true);
        typingArea.setBackground(Color.BLACK);
        typingArea.setForeground(Color.WHITE);

        typingArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                //Don't count the input if it's not a character
                if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
                    return;
                }
                if (!timerStarted) {
                    typerace.startTimer();
                    timerStarted = true;
                }
                if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                    keyPressCount++; // Increment key press count on each key release
                }
                String typedText = typingArea.getText();
                // updateProgress(typedText); This function should be done by the client
                typerace.updateTextColor(typedText);

                // Calculate the time elapsed since the start of typing
                int timeElapsed = (int) ((System.currentTimeMillis() - startTime) / 1000); // Time in seconds

                // If the ending conditions are not met, it sends the current wpm, accuracy and progress to the server.
                if (typedText.equals(providedText) || timeElapsed >= 60) {
                    timer.stop();
                    typerace.showResults(timeElapsed);
                } else {
                    int wpm = game.typerace.calculateWpm();
                    double accuracy = game.typerace.calculateAccuracy(typedText);
                    int progress = game.typerace.calculateProgress(typedText);
                    clientController.updateProgress(currentPlayer.getName(), wpm, progress, accuracy, timeElapsed);
                    typerace.updateProgressDisplay(wpm, accuracy);
                    typerace.updateCarPositions(currentPlayer.getName(), progress);
                }

            }
        });

        JScrollPane scrollPane = new JScrollPane(typingArea);
        scrollPane.setPreferredSize(new Dimension(500, 100));

        // WPM label
        wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wpmLabel.setForeground(Color.WHITE);

        // Accuracy label
        accuracyLabel = new JLabel("Accuracy: 100%");
        accuracyLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        accuracyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accuracyLabel.setForeground(Color.WHITE);

        // Time label
        timeLabel = new JLabel("TIME");
        timeLabel.setFont(customFont.deriveFont(Font.BOLD, 18f));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.GREEN);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout for provided text and typing area
        JPanel textTypingPanel = new JPanel(new BorderLayout());
        textTypingPanel.setOpaque(false);
        textTypingPanel.add(providedTextLabel, BorderLayout.NORTH);
        textTypingPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel for accuracy and timer
        JPanel accuracyTimePanel = new JPanel(new GridLayout(2, 1));
        accuracyTimePanel.setOpaque(false);
        accuracyTimePanel.add(accuracyLabel);
        accuracyTimePanel.add(timeLabel);

        // Main bottom panel
        JPanel mainBottomPanel = new JPanel(new BorderLayout());
        mainBottomPanel.setOpaque(false);
        mainBottomPanel.add(textTypingPanel, BorderLayout.CENTER);
        mainBottomPanel.add(accuracyTimePanel, BorderLayout.EAST);

        // Final layout
        add(wpmLabel, BorderLayout.EAST);
        add(carPanel, BorderLayout.NORTH);
        add(mainBottomPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

    }

    public JPanel getCarPanel() {
        return this.carPanel;
    }




}
