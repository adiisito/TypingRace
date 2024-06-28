package view;

import controller.client.ClientController;
import game.GameState;
import game.Player;
import game.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GameScreen extends JPanel {
    private GameState gameState;
    private Player currentPlayer;
    private JTextArea typingArea;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel providedTextLabel;
    private String providedText;
    private Timer timer;
    private long startTime;
    private int keyPressCount;

    private ClientController clientController;

    public GameScreen(GameState gameState, Player currentPlayer, ClientController clientController) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = Text.getRandomText();
        this.keyPressCount = 0;
        this.clientController = clientController;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Provided text label
        providedTextLabel = new JLabel("<html><p style=\"width: 600px;\">" + providedText + "</p></html>");
        providedTextLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        providedTextLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Typing area
        typingArea = new JTextArea();
        typingArea.setLineWrap(true);
        typingArea.setWrapStyleWord(true);
        typingArea.setFont(new Font("Serif", Font.PLAIN, 18));
        typingArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        typingArea.setEditable(true);

        typingArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                keyPressCount++; // Increment key press count on each key release
                String typedText = typingArea.getText();
                // updateProgress(typedText);

                // Calculate the time elapsed since the start of typing
                int timeElapsed = (int) ((System.currentTimeMillis() - startTime) / 1000); // Time in seconds

                // If the ending conditions are not met, it sends the current wpm, accuracy and progress to the server.
                if (typedText.equals(providedText) || timeElapsed >= 60) {
                    timer.stop();
                    showResults();
                } else {
                    int wpm = calculateWpm();
                    double accuracy = calculateAccuracy(typedText);
                    int progress = calculateProgress(typedText);
                    clientController.updateProgress(currentPlayer.getName(), wpm, progress, accuracy, timeElapsed);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(typingArea);

        // Panel for provided text and typing area
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(providedTextLabel);
        textPanel.add(scrollPane);

        // Info panel for WPM and Accuracy
        wpmLabel = new JLabel("WPM: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.add(wpmLabel);
        infoPanel.add(accuracyLabel);

        add(textPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

        startTimer();
    }

    private void updateProgress(String typedText) {
        int progress = calculateProgress(typedText);
        if (gameState.getCurrentRace() != null) {
            gameState.getCurrentRace().updatePlayerProgress(currentPlayer, progress);
        }

        int wpm = calculateWpm();
        double accuracy = calculateAccuracy(typedText);

        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + accuracy + "%");
    }

    /**
     * Updates the display. This method should be called to reflect changes in the player's typing performance on UI.
     *
     * @param wpm the current wpm
     * @param progress the current progress
     * @param accuracy the current accuracy
     */
    public void updateProgressDisplay (int wpm, int progress, double accuracy) {
        // Directly update UI components based on received data
        SwingUtilities.invokeLater(() -> {
            wpmLabel.setText("WPM: " + wpm);
            accuracyLabel.setText("Accuracy: " + accuracy + "%");
            // Optionally, update a progress bar or similar component if it exists
        });
    }

    private int calculateProgress(String typedText) {
        int length = Math.min(typedText.length(), providedText.length());
        int correctChars = 0;

        for (int i = 0; i < length; i++) {
            if (typedText.charAt(i) == providedText.charAt(i)) {
                correctChars++;
            }
        }

        return correctChars;
    }

    private double calculateAccuracy(String typedText) {
        int correctChars = calculateProgress(typedText);
        int totalTypedChars = typedText.length();

        return totalTypedChars == 0 ? 100 : (correctChars * 100.0) / totalTypedChars;
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
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 60000) {
                gameState.endCurrentRace();
                showResults();
                timer.stop();
            }
        });
        timer.start();
    }

    private void showResults() {
        int wpm = calculateWpm();
        double accuracy = calculateAccuracy(typingArea.getText());
        int timeSpent = (int) ((System.currentTimeMillis() - startTime) / 1000); // Time in seconds

        clientController.endGame(currentPlayer.getName(), timeSpent, wpm, accuracy);

        // Transition to the result screen
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy, clientController));
        frame.revalidate();
        frame.repaint();
    }
}
