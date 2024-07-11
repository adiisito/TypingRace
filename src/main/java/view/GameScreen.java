package view;

import controller.client.ClientController;
import game.*;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
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
    private String providedText;
    private Timer timer;
    private JLabel timeLabel;
    private java.util.List<TypingPlayer> racers;
    private JPanel carPanel;
    private boolean timerStarted = false;
    private java.util.List<ResultScreen> resultScreens = new ArrayList<>();
    private double trackMultiplier = 0.7;

    private long startTime;
    private int keyPressCount;
    private ClientController clientController;
    private ImageIcon backgroundImage;
    private Font customFont;
    private SoundPlayer soundPlayer;

    public GameScreen(GameState gameState, Player currentPlayer, ClientController clientController, String providedText) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = providedText;
        this.keyPressCount = 0;
        this.carShapes = new ArrayList<>();
        this.clientController = clientController;
        this.racers = gameState.getPlayers();
        clientController.setView(this);

        // to use the background image
        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("2screen.gif"));

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


        soundPlayer = new SoundPlayer();
        soundPlayer.playSound("sound1.wav");

        initComponents();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Image image = backgroundImage.getImage();
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }


    private void initComponents() {
        setLayout(new BorderLayout());

        // Creating a Car Panel
        createCarPanel();
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
                /* Doesn't really make sense in our multiplayer game
                if (!timerStarted) {
                    startTimer();
                    timerStarted = true;
                }
                 */
                if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                    keyPressCount++; // Increment key press count on each key release
                }
                String typedText = typingArea.getText();
                // updateProgress(typedText); This function should be done by the client
                updateTextColor(typedText);

                // Calculate the time elapsed since the start of typing
                int timeElapsed = (int) ((System.currentTimeMillis() - startTime) / 1000); // Time in seconds

                int wpm = calculateWpm();
                double accuracy = calculateAccuracy(typedText);
                int progress = calculateProgress(typedText);
                clientController.updateProgress(currentPlayer.getName(), wpm, progress, accuracy, timeElapsed);
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

        startTimer();
        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

    }

    public void addCars() {
        for (Player player : racers) {
            Car newCar = new Car(player);
            //gameState.addPlayer(player);
            CarShape newCarShape = new CarShape(newCar, player,0, carShapes.size() * 50, 60, 50, customFont);
            carShapes.add(newCarShape);
            repaint();
        }
    }


    public void updateProgress(String typedText) {
        int progress = calculateProgress(typedText);
        if (gameState.getCurrentRace() != null) {
            gameState.getCurrentRace().updatePlayerProgress(currentPlayer, progress);
        }

        int wpm = calculateWpm();
        double accuracy = calculateAccuracy(typedText);
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + String.format("%.1f", accuracy) + "%");

        updateCarPositions(currentPlayer.getName(), progress, wpm);
    }

    public void updateCarPositions(String playerName, int progress, int wpm) {
        int totalLength = providedText.length();
        int roadLength = (int) (carPanel.getWidth() * trackMultiplier); // 70% of the panel width
        for (CarShape carShape : carShapes) {

            if (carShape.getPlayer().getName().equals(playerName)) {
                int newProgress = (progress * roadLength) / totalLength;
                carShape.setX(newProgress);
                carShape.setWpm(wpm);
                System.out.println("Updating car position for player " + playerName + " to " + newProgress);
                break;
            }
        }
        carPanel.repaint();
    }

    private int calculateProgress(String typedText) {
        int maxLength = Math.min(typedText.length(), providedText.length());
        int correctChars = 0;
        for (int i = 0; i < maxLength; i++) {
            if (typedText.charAt(i) == providedText.charAt(i)) {
                correctChars++;
            } else {
                break; // Stop at the first incorrect character
            }
        }
        System.out.println("Calculated progress: " + correctChars + " characters");
        return correctChars;
    }

    private double calculateAccuracy(String typedText) {
        int maxLength = Math.min(typedText.length(), providedText.length());
        int correctChars = 0;
        for (int i = 0; i < maxLength; i++) {
            if (typedText.charAt(i) == providedText.charAt(i)) {
                correctChars++;
            }
        }
        return keyPressCount == 0 ? 100 : (correctChars * 100.0) / keyPressCount;
    }

    private int calculateWpm() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedMinutes = elapsedTime / 60000.0;
        int totalWords = keyPressCount / 5;
        return (int) (totalWords / elapsedMinutes);
    }

    private void startTimer() {

        if (timer != null) {
            System.out.println("Stopping existing timer.");
            timer.stop();
        }
        System.out.println("Starting new game timer.");

        startTime = System.currentTimeMillis();
        gameState.setStartTime(startTime);

        timer = new Timer(1000, e -> {


            long elapsedTime = ((System.currentTimeMillis() - gameState.getStartTime()) / 1000);
            long remainingTime = 60 - elapsedTime;
            timeLabel.setText("TIME: " + (int) remainingTime);
            String typedText = typingArea.getText();

            boolean isCompleted = typedText.equals(providedText);
            if (elapsedTime >= 60 || isCompleted) {
                gameState.endCurrentRace();
                showResults((int) elapsedTime);
            }

            int wpm = calculateWpm();
            int progress = calculateProgress(typedText);
            double accuracy = calculateAccuracy(typedText);
            clientController.updateProgress(currentPlayer.getName(), wpm, progress, accuracy, (int) elapsedTime);


        });
        timer.start();
    }

    private void showResults(int elapsedTime) {
        if (timer != null) {
            timer.stop();
            timer = null;
            System.out.println("Timer stopped after game end.");
        }


        soundPlayer.stopSound();

        int wpm = calculateWpm();
        double accuracy = calculateAccuracy(typingArea.getText());

        currentPlayer.setWpm(wpm);
        currentPlayer.setAccuracy(accuracy);
        clientController.endGame(currentPlayer.getName(), elapsedTime, wpm, accuracy);

        trackMultiplier = 0.9;
        createCarPanel();

        // use clientcontroller to transition to the result screen
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy, elapsedTime, carPanel, clientController));
//        frame.revalidate();
//        frame.repaint();
    }

    private void createCarPanel() {
        carPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int roadLength = (int) (getWidth() * trackMultiplier); // 70% of the panel width
                for (CarShape carShape : carShapes) {
                    carShape.draw(g, roadLength);
                }
            }
        };
        carPanel.setOpaque(false);
        carPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        carPanel.setPreferredSize(new Dimension(600, 300));
    }

    public JPanel getCarPanel() {
        return this.carPanel;
    }

    /**
     * Gets the list of car entities that move according to progress.
     * @return the car shapes
     */
    public ArrayList<CarShape> getCarShapes() {
        return this.carShapes;
    }

    /**
     * Gets the length of the initial provided text.
     * @return the length of the provided text
     */
    public int getTextLength() {
        return providedText.length();
    }

    private void updateTextColor(String typedText) {
        StyledDocument doc = typingArea.getStyledDocument();
        StyleContext context = new StyleContext();
        AttributeSet correctStyle = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
        AttributeSet incorrectStyle = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.RED);
        AttributeSet defaultStyle = context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.WHITE);

        doc.setCharacterAttributes(0, typedText.length(), defaultStyle, true);

        boolean mistakeFound = false;
        for (int i = 0; i < typedText.length(); i++) {
            if (!mistakeFound && i < providedText.length() && typedText.charAt(i) == providedText.charAt(i)) {
                doc.setCharacterAttributes(i, 1, correctStyle, true);
            } else {
                doc.setCharacterAttributes(i, 1, incorrectStyle, true);
                mistakeFound = true;
            }

            // Check if a space character is encountered to reset the mistake flag
            if (typedText.charAt(i) == ' ') {
                mistakeFound = false;
            }
        }
    }

    /**
     * Updates the display with the current WPM and accuracy.
     *
     */
    public void updateProgressDisplay() {
        wpmLabel.setText("WPM: " + currentPlayer.getWpm());
        accuracyLabel.setText("Accuracy: " + String.format("%.1f", currentPlayer.getAccuracy()) + "%");
    }

    public Timer getTimer() {
        return this.timer;
    }
}
