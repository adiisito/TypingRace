package view;

import controller.client.ClientController;
import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameScreen extends JPanel {
    private final ArrayList<CarShape> carShapes;
    private GameState gameState;
    private Player currentPlayer;
    private JTextArea typingArea;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel providedTextLabel;
    private String providedText;
    private Timer timer;
    private JLabel timeLabel;
    private java.util.List<Player> racers;


    private long startTime;
    private int keyPressCount;

    private ClientController clientController;

    public GameScreen(GameState gameState, Player currentPlayer, ClientController clientController) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = Text.getRandomText();
        this.keyPressCount = 0;
        this.carShapes = new ArrayList<>();
        this.clientController = clientController;
        this.racers=gameState.getPlayers();

        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // Creating a Car Panel
        JPanel carPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (CarShape carShape : carShapes) {
                    carShape.draw(g);
                }
            }
        };
        carPanel.setPreferredSize(new Dimension(600, 400));
        add(carPanel, BorderLayout.NORTH);



        //addCar(currentPlayer);

        timeLabel = new JLabel("TIME");
        timeLabel.setFont(new Font("Serif", Font.BOLD, 18));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.GREEN);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.NORTH);
        add(timePanel, BorderLayout.EAST);

        // Provided text label
        providedTextLabel = new JLabel("<html><p style=\"width: 600px;\">" + providedText + "</p></html>");
        providedTextLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        providedTextLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        providedTextLabel.setHorizontalAlignment(SwingConstants.LEFT);

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
                 updateProgress(typedText);

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
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(providedTextLabel, BorderLayout.NORTH);
        textPanel.add(scrollPane, BorderLayout.CENTER);
        add(textPanel, BorderLayout.CENTER);

        // This is the basic info panel, we shall change it later.
        // We shall stick to our Mockup Plan and make it look cooler. ;)
        wpmLabel = new JLabel("WPM: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.add(wpmLabel);
        infoPanel.add(accuracyLabel);

        JPanel infoPanelWrapper = new JPanel(new BorderLayout());
        infoPanelWrapper.add(infoPanel, BorderLayout.SOUTH);
        add(infoPanelWrapper, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

        startTimer();
        addCars();
    }

    public void addCars() {
        for (Player player : racers){
            Car newCar = new Car(player);
            //gameState.addPlayer(player);
            CarShape newCarShape = new CarShape(newCar, 0, carShapes.size() * 40, 50, 30);
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

         updateCarPositions(calculateProgress(typedText));
    }

    /**
     * Updates the display. This method should be called to reflect changes in the player's typing performance on UI.
     *
     * @param wpm the current wpm
     * @param accuracy the current accuracy
     */
    public void updateProgressDisplay (int wpm, double accuracy) {
        // Directly update UI components based on received data
        SwingUtilities.invokeLater(() -> {
            wpmLabel.setText("WPM: " + wpm);
            accuracyLabel.setText("Accuracy: " + String.format("%.1f", accuracy) + "%");
            // Optionally, update a progress bar or similar component if it exists
        });
    }


    public void updateCarPositions(int progress) {
        // Update the car positions based on the players' progress
        int playerCount = gameState.getPlayers().size();
        for (int i = 0; i < playerCount; i++) {
            if (i < carShapes.size()) {
                Player player = gameState.getPlayers().get(i);
                // int progress = player.getProgress();
                int newXPosition = progress * 5;
                System.out.println("Updating car position for player " + player.getName() + " to " + newXPosition);
                carShapes.get(i).setX(newXPosition); // Update the x position based on progress
            }
        }
        repaint();
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
            long elapsedTime = System.currentTimeMillis() - gameState.getStartTime();
            int remainingTime = (int) (60 - (elapsedTime / 1000));
            timeLabel.setText("TIME: " + remainingTime); // /1000 to convert it into seconds
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

        // Transition to the result screen
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy, clientController));
        frame.revalidate();
        frame.repaint();
    }
}
