package view;

import controller.client.ClientController;
import game.Car;
import game.GameState;
import game.Player;
import game.TypingPlayer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.text.AttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

/** The window from which the players participate in the SpaceRally. */
public class GameScreen extends JPanel {
  /** Collection of graphical car shapes displayed on the screen, each representing a player. */
  private final ArrayList<CarShape> carShapes;

  /** Current state of the game, providing access to overall game data like players and scores. */
  private final GameState gameState;

  /** The current player using this instance of the game screen. */
  private final Player currentPlayer;

  /** The text that players need to type correctly to advance in the game. */
  private final String providedText;

  /** List of players participating in the game, used to manage race progress and scoring. */
  private final java.util.List<TypingPlayer> racers;

  /** Controller responsible for handling client-side logic and interactions with the server. */
  private final ClientController clientController;

  /** Background image for the game screen, enhancing the visual appeal. */
  private final ImageIcon backgroundImage;

  /** Component for playing game sounds, enhancing the interactive experience. */
  private final SoundPlayer soundPlayer;

  /** Boolean flag to control whether sounds are enabled or disabled during the game. */
  private final boolean soundOn;

  /** Sound player specifically for playing error sounds when players make typing mistakes. */
  private final SoundPlayer errorSoundPlayer;

  /** Text area where players input their typing responses. */
  private JTextPane typingArea;

  /** Label displaying the current words per minute (WPM) of the player. */
  private JLabel wpmLabel;

  /** Label showing the typing accuracy percentage of the player. */
  private JLabel accuracyLabel;

  /** Label that presents the text players need to type. */
  private JLabel providedTextLabel;

  /** Timer used for game timing and managing session progress. */
  private Timer timer;

  /** Label displaying the countdown or elapsed time during the game. */
  private JLabel timeLabel;

  /** Panel displaying graphical representations of cars corresponding to players. */
  private JPanel carPanel;

  /** Flag indicating whether the current game session has finished. */
  private boolean isFinished = false;

  /** Counter for the number of incorrect characters typed by the player. */
  private int wrongChars = 0;

  /** Timestamp marking the start of the current game session. */
  private long startTime;

  /** Count of key presses made by the player during the game, used for accuracy calculations. */
  private int keyPressCount;

  /** Custom font used throughout the game screen for consistent styling. */
  private Font customFont;

  /**
   * Instantiates a new Game screen.
   *
   * @param gameState the game state
   * @param currentPlayer the current player
   * @param clientController the client controller
   * @param providedText the provided text
   * @param soundOn the sound on
   */
  public GameScreen(
      GameState gameState,
      Player currentPlayer,
      ClientController clientController,
      String providedText,
      boolean soundOn) {
    this.gameState = gameState;
    this.currentPlayer = currentPlayer;
    this.providedText = providedText;
    this.keyPressCount = 0;
    this.carShapes = new ArrayList<>();
    this.clientController = clientController;
    this.racers = gameState.getPlayers();
    this.soundOn = soundOn;
    clientController.setView(this);

    // to use the background image
    backgroundImage = new ImageIcon(getClass().getClassLoader().getResource("2screen.gif"));

    // to use custom font
    try {
      InputStream fontStream = getClass().getClassLoader().getResourceAsStream("nougat.ttf");
      if (fontStream != null) {
        customFont =
            Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(18f); // Set default size
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(customFont);
      } else {
        System.err.println("Font not found");
      }
    } catch (IOException | FontFormatException e) {
      e.printStackTrace();
    }

    soundPlayer = new SoundPlayer();
    errorSoundPlayer = new SoundPlayer();
    if (soundOn) {
      soundPlayer.playSound("sound1.wav");
    }

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
    providedTextLabel =
        new JLabel(
            "<html><p style=\"width: 350px; color: white;\">" + providedText + "</p></html>");
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

    typingArea.addKeyListener(
        new KeyAdapter() {
          @Override
          public void keyReleased(KeyEvent e) {
            // Don't count the input if it's not a character
            if (e.getKeyChar() == KeyEvent.CHAR_UNDEFINED) {
              return;
            }
            if (e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
              keyPressCount++;
            }
            String typedText = typingArea.getText();
            // updateProgress(typedText); This function should be done by the client
            updateTextColor(typedText);

            // Calculate the time elapsed since the start of typing
            int timeElapsed =
                (int) ((System.currentTimeMillis() - startTime) / 1000); // Time in seconds
            int wpm = calculateWpm();
            double accuracy = calculateAccuracy(typedText);
            int progress = calculateProgress(typedText);

            if (typedText.charAt(typedText.length() - 1)
                    != providedText.charAt(typedText.length() - 1)
                && e.getKeyCode() != KeyEvent.VK_BACK_SPACE) {
              wrongChars++;
            }
            clientController.updateProgress(
                currentPlayer.getName(), wpm, progress, accuracy, timeElapsed);
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
    JPanel accuracyTimePanel = new JPanel(new GridLayout(3, 1));
    accuracyTimePanel.setOpaque(false);
    accuracyTimePanel.add(wpmLabel);
    accuracyTimePanel.add(accuracyLabel);
    accuracyTimePanel.add(timeLabel);

    // Main bottom panel
    JPanel mainBottomPanel = new JPanel(new BorderLayout());
    mainBottomPanel.setOpaque(false);
    mainBottomPanel.add(textTypingPanel, BorderLayout.CENTER);
    mainBottomPanel.add(accuracyTimePanel, BorderLayout.EAST);

    // Final layout
    add(carPanel, BorderLayout.NORTH);
    add(mainBottomPanel, BorderLayout.CENTER);

    startTimer();
    SwingUtilities.invokeLater(() -> typingArea.requestFocusInWindow());
  }

  /** Adds cars / UFOs and their racetracks to the game window. */
  public void addCars() {
    carShapes.clear();
    for (Player player : racers) {
      Car newCar = new Car(player);
      CarShape newCarShape =
          new CarShape(newCar, player, 0, carShapes.size() * 60, 60, 50, customFont);
      carShapes.add(newCarShape);
      repaint();
    }
  }

  /**
   * Update progress.
   *
   * @param typedText the typed text
   */
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

  /**
   * Updates the position of a specified player's car in the window.
   *
   * @param playerName the player to move
   * @param progress the player's progress
   * @param wpm the player's wpm count
   */
  public void updateCarPositions(String playerName, int progress, int wpm) {
    int roadLength = carPanel.getWidth() - 100; // Use carPanel width for movement bounds

    for (CarShape carShape : carShapes) {
      if (carShape.getPlayer().getName().equals(playerName)) {
        int newProgress = (progress * (roadLength - carShape.getWidth())) / 100;
        carShape.setHorizontal(newProgress);
        carShape.setProgress(progress);
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
    int totalLength = providedText.length();
    return (correctChars * 100) / totalLength; // Return progress percentage
  }

  private double calculateAccuracy(String typedText) {
    int maxLength = Math.min(typedText.length(), providedText.length());
    int correctChars = 0;
    for (int i = 0; i < maxLength; i++) {
      if (typedText.charAt(i) == providedText.charAt(i)) {
        correctChars++;
      }
    }
    // Stopper for accuracy, this only really comes up when typing way too fast
    double accuracy = (correctChars * 100.0) / keyPressCount;
    if (accuracy > 100) {
      return 100;
    }

    return keyPressCount == 0 ? 100 : accuracy;
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

    timer =
        new Timer(
            1000,
            e -> {
              long elapsedTime = ((System.currentTimeMillis() - gameState.getStartTime()) / 1000);
              long remainingTime = 60 - elapsedTime;
              timeLabel.setText("TIME: " + (int) remainingTime);

              if (remainingTime <= 10) {
                timeLabel.setBackground(Color.RED);
              } else {
                timeLabel.setBackground(Color.GREEN);
              }

              String typedText = typingArea.getText();
              boolean isCompleted = typedText.equals(providedText);
              if (elapsedTime >= 60 || isCompleted) {
                gameState.endCurrentRace();
                showResults((int) elapsedTime);
              }

              int wpm = calculateWpm();
              int progress = calculateProgress(typedText);
              double accuracy = calculateAccuracy(typedText);
              clientController.updateProgress(
                  currentPlayer.getName(), wpm, progress, accuracy, (int) elapsedTime);
            });
    timer.start();
  }

  private void showResults(int elapsedTime) {
    if (timer != null) {
      timer.stop();
      timer = null;
      System.out.println("Timer stopped after game end.");
    }

    if (soundPlayer != null) {
      soundPlayer.stopSound();
    }

    int wpm = calculateWpm();
    double accuracy = calculateAccuracy(typingArea.getText());

    currentPlayer.setWpm(wpm);
    currentPlayer.setAccuracy(accuracy);
    clientController.endGame(currentPlayer.getName(), elapsedTime, wpm, accuracy);

    for (CarShape carShape : carShapes) {
      if (carShape.getPlayer().isCompleted()) {
        carShape.setHorizontal(440);
      }
    }

    isFinished = true;
    createCarPanel();
  }

  private void createCarPanel() {
    carPanel =
        new JPanel() {
          @Override
          protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int roadLength = getWidth() - 100; // Use the width of carPanel
            for (CarShape carShape : carShapes) {
              carShape.draw(g, roadLength);
            }
          }
        };
    carPanel.setOpaque(false);
    carPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    carPanel.setPreferredSize(new Dimension(600, 300));
  }

  /**
   * Gets car panel.
   *
   * @return the car panel
   */
  public JPanel getCarPanel() {
    return this.carPanel;
  }

  /**
   * Gets the list of car entities that move according to progress.
   *
   * @return the car shapes
   */
  public ArrayList<CarShape> getCarShapes() {
    return this.carShapes;
  }

  private void updateTextColor(String typedText) {
    StyledDocument doc = typingArea.getStyledDocument();
    StyleContext context = new StyleContext();
    AttributeSet correctStyle =
        context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.GREEN);
    AttributeSet incorrectStyle =
        context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.RED);
    AttributeSet defaultStyle =
        context.addAttribute(context.getEmptySet(), StyleConstants.Foreground, Color.WHITE);

    doc.setCharacterAttributes(0, typedText.length(), defaultStyle, true);

    boolean mistakeFound = false;
    for (int i = 0; i < typedText.length(); i++) {
      if (!mistakeFound
          && i < providedText.length()
          && typedText.charAt(i) == providedText.charAt(i)) {
        doc.setCharacterAttributes(i, 1, correctStyle, true);
      } else {
        doc.setCharacterAttributes(i, 1, incorrectStyle, true);
        mistakeFound = true;
        if (soundOn) {
          errorSoundPlayer.playSound("error.wav");
        }
      }

      // Check if a space character is encountered to reset the mistake flag
      if (typedText.charAt(i) == ' ') {
        mistakeFound = false;
      }
    }
  }

  /** Updates the display with the current WPM and accuracy. */
  public void updateProgressDisplay() {
    wpmLabel.setText("WPM: " + currentPlayer.getWpm());
    accuracyLabel.setText("Accuracy: " + String.format("%.1f", currentPlayer.getAccuracy()) + "%");
  }

  /**
   * Gives the amount of wrong inputs for the player.
   *
   * @return the amount of counted wrong characters
   */
  public int getWrongChars() {
    return wrongChars;
  }
}
