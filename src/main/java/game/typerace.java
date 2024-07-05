package game;

import controller.client.ClientController;
import view.ResultScreen;
import view.GameScreen;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;

public class typerace {

    private int keyPressCount;
    private ArrayList<CarShape> carShapes;
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
    private typerace typerace;


    private static long startTime;
    private ClientController clientController;


    public typerace(){



    }
    public void addCars() {
        for (Player player : racers) {
            Car newCar = new Car(player);
            //gameState.addPlayer(player);
            CarShape newCarShape = new CarShape(newCar, player,0, carShapes.size() * 50, 50, 30);
            carShapes.add(newCarShape);
            carPanel.repaint();
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

    //The updated method is at the end to find!

    /**
     * Updates the display. This method should be called to reflect changes in the player's typing performance on UI.
     *
         * @param wpm the current wpm
         * @param accuracy the current accuracy
    */
    public void updateProgressDisplay (int wpm, double accuracy) {
       //  Directly update UI components based on received data
       SwingUtilities.invokeLater(() -> {
            wpmLabel.setText("WPM: " + wpm);
          accuracyLabel.setText("Accuracy: " + String.format("%.1f", accuracy) + "%");
          // Optionally, update a progress bar or similar component if it exists
         });
       }


    public void updateCarPositions(String playerName, int progress) {
        int totalLength = providedText.length();
        int roadLength = (int) (carPanel.getWidth() * 0.7); // 70% of the panel width
        for (CarShape carShape : carShapes) {

            if (carShape.getPlayer().getName().equals(playerName)) {
                int newProgress = (progress * roadLength) / totalLength;
                carShape.setX(newProgress);
                System.out.println("Updating car position for player " + playerName + " to " + newProgress);
                break;
            }
        }
        carPanel.repaint();
    }

    public static int calculateProgress(String typedText) {
        int maxLength = Math.min(typedText.length(), GameScreen.providedText.length());
        int correctChars = 0;
        for (int i = 0; i < maxLength; i++) {
            if (typedText.charAt(i) == GameScreen.providedText.charAt(i)) {
                correctChars++;
            } else {
                break; // Stop at the first incorrect character
            }
        }
        System.out.println("Calculated progress: " + correctChars + " characters");
        return correctChars;
    }

    public static double calculateAccuracy(String typedText) {
        int maxLength = Math.min(typedText.length(), GameScreen.providedText.length());
        int correctChars = 0;
        for (int i = 0; i < maxLength; i++) {
            if (typedText.charAt(i) == GameScreen.providedText.charAt(i)) {
                correctChars++;
            }
        }
        return GameScreen.keyPressCount == 0 ? 100 : (correctChars * 100.0) / GameScreen.keyPressCount;
    }

    public static int calculateWpm() {
        long elapsedTime = System.currentTimeMillis() - startTime;
        double elapsedMinutes = elapsedTime / 60000.0;
        int totalWords = GameScreen.keyPressCount / 5;
        return (int) (totalWords / elapsedMinutes);
    }

    public void startTimer() {
        startTime = System.currentTimeMillis();
        gameState.setStartTime(startTime);

        timer = new Timer(1000, e -> {
            int elapsedTime = (int) ((System.currentTimeMillis() - gameState.getStartTime()) / 1000);
            int remainingTime = 60 - elapsedTime;
            timeLabel.setText("TIME: " + remainingTime);
            if (elapsedTime >= 60000) {
                gameState.endCurrentRace();
                showResults(elapsedTime);
                timer.stop();
            }
        });
        timer.start();
    }

    public void showResults(int elapsedTime) {
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



    public void updateTextColor(String typedText) {
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



}
