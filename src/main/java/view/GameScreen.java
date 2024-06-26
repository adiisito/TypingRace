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

    public GameScreen(GameState gameState, Player currentPlayer) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = Text.getRandomText();
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
        typingArea.setEditable(true); //it's basically used, so that the typing area could be edited(means you can see what you are typing)

        typingArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                String typedText = typingArea.getText();
                updateProgress(typedText);

                if (typedText.equals(providedText)) {
                    timer.stop();
                    showResults();
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(typingArea);

        // Panel for provided text and typing area
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.add(providedTextLabel);
        textPanel.add(scrollPane);

        //This is the basic info panel, we shall change it later.
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

        int wpm = currentPlayer.getWpm();
        double accuracy = calculateAccuracy(typedText);
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + accuracy + "%");
    }

    //This method is actually not very useful, I wanted to play with the calculations and see, that's why added it here.
    //Feel free to modify/Change and Remove it.
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

    private void startTimer() {
        timer = new Timer(1000, e -> {
            long elapsedTime = System.currentTimeMillis() - gameState.getStartTime();
            if (elapsedTime >= 60000) {
                gameState.endCurrentRace();
                showResults();
                timer.stop();
            }
        });
        timer.start();
    }

    private void showResults() {
        int wpm = currentPlayer.getWpm();
        double accuracy = calculateAccuracy(typingArea.getText());

        // Switch to the result screen
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy));
        frame.revalidate();
        frame.repaint();
    }
}