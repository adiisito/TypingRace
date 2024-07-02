package view;

import controller.client.ClientController;
import game.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
    private java.util.List<Player> racers;
    private JPanel carPanel;
    private boolean timerStarted = false;


    private long startTime;
    private int keyPressCount;

    private ClientController clientController;

    public GameScreen(GameState gameState, Player currentPlayer, ClientController clientController, String providedText) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = providedText;
        this.keyPressCount = 0;
        this.carShapes = new ArrayList<>();
        this.clientController = clientController;
        this.racers = gameState.getPlayers();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        // Creating a Car Panel
        carPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (CarShape carShape : carShapes) {
                    carShape.draw(g);
                }
            }
        };
        carPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        carPanel.setPreferredSize(new Dimension(600, 100));
        carPanel.setBackground(Color.BLACK);
        add(carPanel, BorderLayout.NORTH);



        //addCar(currentPlayer);
        /*
        timeLabel = new JLabel("TIME");
        timeLabel.setFont(new Font("Serif", Font.BOLD, 18));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.GREEN);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.NORTH);
        add(timePanel, BorderLayout.EAST);
         */

        // Provided text label
        providedTextLabel = new JLabel("<html><p style=\"width: 350px; color: white;\">" + providedText + "</p></html>");
        providedTextLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        providedTextLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        providedTextLabel.setHorizontalAlignment(SwingConstants.LEFT);
        providedTextLabel.setPreferredSize(new Dimension(500, 150));
        providedTextLabel.setForeground(Color.WHITE);

        // Typing area
        typingArea = new JTextPane();
        typingArea.setFont(new Font("Serif", Font.PLAIN, 18));
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
                    startTimer();
                    timerStarted = true;
                }
                if(e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
                    keyPressCount++; // Increment key press count on each key release
                }
                String typedText = typingArea.getText();
                updateProgress(typedText);
                updateTextColor(typedText);

                // Calculate the time elapsed since the start of typing
                int timeElapsed = (int) ((System.currentTimeMillis() - startTime) / 1000); // Time in seconds

                // If the ending conditions are not met, it sends the current wpm, accuracy and progress to the server.
                if (typedText.equals(providedText) || timeElapsed >= 60) {
                    timer.stop();
                    showResults(timeElapsed);
                } else {
                    int wpm = calculateWpm();
                    double accuracy = calculateAccuracy(typedText);
                    int progress = calculateProgress(typedText);
                    clientController.updateProgress(currentPlayer.getName(), wpm, progress, accuracy, timeElapsed);
                }

            }
        });

        JScrollPane scrollPane = new JScrollPane(typingArea);

        // WPM label
        wpmLabel = new JLabel("WPM: 0");
        wpmLabel.setFont(new Font("Serif", Font.BOLD, 18));
        wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wpmLabel.setForeground(Color.WHITE);

        // Accuracy label
        accuracyLabel = new JLabel("Accuracy: 100%");
        accuracyLabel.setFont(new Font("Serif", Font.BOLD, 18));
        accuracyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accuracyLabel.setForeground(Color.WHITE);

        // Time label
        timeLabel = new JLabel("TIME");
        timeLabel.setFont(new Font("Serif", Font.BOLD, 18));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.GREEN);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Layout for provided text and typing area
        JPanel textTypingPanel = new JPanel(new BorderLayout());
        textTypingPanel.add(providedTextLabel, BorderLayout.NORTH);
        textTypingPanel.add(scrollPane, BorderLayout.CENTER);
        textTypingPanel.setBackground(Color.BLACK);

        // Panel for accuracy and timer
        JPanel accuracyTimePanel = new JPanel(new GridLayout(2, 1));
        accuracyTimePanel.add(accuracyLabel);
        accuracyTimePanel.add(timeLabel);
        accuracyTimePanel.setBackground(Color.BLACK);

        // Main bottom panel
        JPanel mainBottomPanel = new JPanel(new BorderLayout());
        mainBottomPanel.add(textTypingPanel, BorderLayout.CENTER);
        mainBottomPanel.add(accuracyTimePanel, BorderLayout.EAST);
        mainBottomPanel.setBackground(Color.BLACK);

        // Final layout
        add(wpmLabel, BorderLayout.EAST);
        add(carPanel, BorderLayout.NORTH);
        add(mainBottomPanel, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

        // Timer now starts when the first key is pressed
        // startTimer();
        // addCars();
    }

    public void addCars() {
        for (Player player : racers){
            Car newCar = new Car(player);
            //gameState.addPlayer(player);
            CarShape newCarShape = new CarShape(newCar, player,0, carShapes.size() * 50, 50, 30);
            carShapes.add(newCarShape);
            repaint();
        }
    }


    private void updateProgress(String typedText) {
        int progress = calculateProgress(typedText);
        if (gameState.getCurrentRace() != null) {
            gameState.getCurrentRace().updatePlayerProgress(currentPlayer, progress);
        }

        int wpm = calculateWpm();
        double accuracy = calculateAccuracy(typedText);
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + String.format("%.1f", accuracy) + "%");

        updateCarPositions(currentPlayer.getName(), progress);
    }

//    /**
//     * Updates the display. This method should be called to reflect changes in the player's typing performance on UI.
//     *
//     * @param wpm the current wpm
//     * @param accuracy the current accuracy
//     */
//    public void updateProgressDisplay (int wpm, double accuracy) {
//        // Directly update UI components based on received data
//        SwingUtilities.invokeLater(() -> {
//            wpmLabel.setText("WPM: " + wpm);
//            accuracyLabel.setText("Accuracy: " + String.format("%.1f", accuracy) + "%");
//            // Optionally, update a progress bar or similar component if it exists
//        });
//    }


    public void updateCarPositions(String playerName, int progress) {

        for (CarShape carShape : carShapes) {

            if (carShape.getPlayer().getName().equals(playerName)) {
                int newXposition = progress * 5;
                carShape.setX(newXposition);
                System.out.println("Updating car position for player " + playerName + " to " + newXposition);
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
        startTime = System.currentTimeMillis();
        gameState.setStartTime(startTime);

        timer = new Timer(1000, e -> {
            long elapsedTime = System.currentTimeMillis() - gameState.getStartTime();
            int remainingTime = (int) (60 - (elapsedTime / 1000));
            timeLabel.setText("TIME: " + remainingTime); // /1000 to convert it into seconds
            if (elapsedTime >= 60000) {
                gameState.endCurrentRace();
                showResults(elapsedTime);
                timer.stop();
            }
        });
        timer.start();
    }

    private void showResults(long elapsedTime) {
        int wpm = calculateWpm();
        double accuracy = calculateAccuracy(typingArea.getText());

        currentPlayer.setWpm(wpm);
        currentPlayer.setAccuracy(accuracy);

        clientController.endGame(currentPlayer.getName(), elapsedTime, wpm, accuracy);

        // use clientcontroller to transition to the result screen
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy, elapsedTime, carPanel, clientController));
//        frame.revalidate();
//        frame.repaint();
    }

    public JPanel getCarPanel() {
        return this.carPanel;
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
     * @param wpm the current words per minute
     * @param accuracy the current accuracy percentage
     */
    public void updateProgressDisplay(int wpm, double accuracy) {
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + String.format("%.1f", accuracy) + "%");
    }
}
