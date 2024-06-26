package view;

import game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GameScreen extends JPanel {
    private GameState gameState;
    private Player currentPlayer;
    private Car car;
    private JTextArea typingArea;
    private JLabel wpmLabel;
    private JLabel accuracyLabel;
    private JLabel providedTextLabel;
    private String providedText;
    private Timer timer;
    private JLabel timeLabel;
    private ArrayList<Car> cars;
    private CarShape carShape;
    private JPanel carPanel;


    public GameScreen(GameState gameState, Player currentPlayer) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.providedText = Text.getRandomText();
        this.cars = new ArrayList();
        this.car=new Car(currentPlayer);
        this.carShape= new CarShape(car,0,50,50,30 );


        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        // creating a Car Panel
        JPanel carPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                carShape.draw(g);
            }
        };
        carPanel.setPreferredSize(new Dimension(1000, 400));

        addCar(currentPlayer);
        
        timeLabel = new JLabel("TIME");
        timeLabel.setFont(new Font("Serif", Font.BOLD, 18));
        timeLabel.setOpaque(true);
        timeLabel.setBackground(Color.GREEN);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel timePanel = new JPanel(new BorderLayout());
        timePanel.add(timeLabel, BorderLayout.NORTH);


        // Provided text label
        providedTextLabel = new JLabel("<html><p style=\"width: 600px;\">" + providedText + "</p></html>");
        providedTextLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        providedTextLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        providedTextLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        providedTextLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JPanel providedTextPanel = new JPanel(new BorderLayout());
        providedTextPanel.add(providedTextLabel, BorderLayout.WEST);

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

        JPanel typingAreaPanel = new JPanel(new BorderLayout());
        typingAreaPanel.add(scrollPane, BorderLayout.CENTER);

        //This is the basic info panel, we shall change it later.
        // We shall stick to our Mockup Plan and make it look cooler. ;)
        wpmLabel = new JLabel("WPM: 0");
        accuracyLabel = new JLabel("Accuracy: 100%");
        JPanel infoPanel = new JPanel(new GridLayout(1, 2));
        infoPanel.add(wpmLabel);
        infoPanel.add(accuracyLabel);

        JPanel infoPanelWrapper = new JPanel(new BorderLayout());
        infoPanelWrapper.add(infoPanel, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(providedTextPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(scrollPane);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(infoPanel);

        // Positioning; The basic implementation!
        add(timePanel, BorderLayout.EAST);
        add(carPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.SOUTH);

        SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());

        startTimer();
    }

    public void addCar(Player newPlayer) {
        Car newCar = new Car(newPlayer);
        cars.add(newCar);
        CarShape newCarShape = new CarShape(newCar, 0, cars.size() * 40, 50, 30);
        gameState.addPlayer(newPlayer);
        repaint();
    }

    private void updateProgress(String typedText) {
        int progress = calculateProgress(typedText);
        gameState.getCurrentRace().updatePlayerProgress(currentPlayer, progress);

        int wpm = currentPlayer.getWpm();
        double accuracy = calculateAccuracy(typedText);
        wpmLabel.setText("WPM: " + wpm);
        accuracyLabel.setText("Accuracy: " + accuracy + "%");

        updateCarPositions();
    }


    private void updateCarPositions() {
        // Update the car positions based on the players' progress
        for (int i = 0; i < gameState.getPlayers().size(); i++) {
            Player player = gameState.getPlayers().get(i);
            int progress = player.getProgress();
            carShape.setX(progress * 5);
        }
        repaint();
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
        int wpm = currentPlayer.getWpm();
        double accuracy = calculateAccuracy(typingArea.getText());

        // Switch to the result screen
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.setContentPane(new ResultScreen(gameState, currentPlayer, wpm, accuracy));
        frame.revalidate();
        frame.repaint();
    }

}