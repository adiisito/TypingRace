package view;

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

    public GameScreen(GameState gameState, Player currentPlayer) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = Text.getRandomText();
        this.gameState.startNewRace(); // Ensure a new race is started
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
        typingArea.setEditable(true); // it's basically used, so that the typing area could be edited (means you can see what you are typing)

        typingArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                updateProgress(typingArea.getText());
            }
        });

        JScrollPane scrollPane = new JScrollPane(typingArea);

        // Panel for provided text and typing area
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(providedTextLabel);
        textPanel.add(scrollPane);

        // This is the basic info panel, we shall change it later.
        // We shall stick to our Mockup Plan and make it look cooler. ;)
        wpmLabel = new JLabel("WPM: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.add(wpmLabel);
        infoPanel.add(accuracyLabel);

        // Positioning; The basic implementation!
        add(textPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

        startTimer();
    }

    private void updateProgress(String typedText) {
        int progress = calculateProgress(typedText);
        gameState.getCurrentRace().updatePlayerProgress(currentPlayer, progress);

        int wpm = calculateWPM(typedText);
        double accuracy = calculateAccuracy(typedText);
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + String.format("%.2f", accuracy) + "%");
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

    private int calculateWPM(String typedText) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedMinutes = elapsedTime / 60000.0;
        int wordCount = typedText.split("\\s+").length;

        return (int) (wordCount / elapsedMinutes);
    }

    private void startTimer() {
        startTime = System.currentTimeMillis();
        gameState.setStartTime(startTime); // Ensure the game state has the correct start time

        timer = new Timer(1000, e -> {
            long elapsedTime = System.currentTimeMillis() - startTime;
            if (elapsedTime >= 60000) { // Check if 60 seconds have passed
                gameState.endCurrentRace();
                showResults();
                timer.stop();
            } else {
                // Update WPM and accuracy periodically
                updateProgress(typingArea.getText());
            }
        });
        timer.start();
    }

    private void showResults() {
        int wpm = calculateWPM(typingArea.getText());
        double accuracy = calculateAccuracy(typingArea.getText());

        // Switch to the result screen
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy));
        frame.revalidate();
        frame.repaint();
    }
}
