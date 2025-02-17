package view;

import controller.client.ClientController;
import game.GameState;
import game.Player;
import game.TypingPlayer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * Creates a result screen when the game ends. Contains statistics of the round, a display of the
 * game state and a ranking of the players.
 */
public class ResultScreen extends JPanel {
  /** Words per minute achieved by the current player. */
  private final int wpm;

  /** Accuracy percentage of the current player, calculated as correctly typed characters. */
  private final double accuracy;

  /** State of the game including details like game progress and player states. */
  private final GameState gameState;

  /** The player instance representing the current player for whom the results are shown. */
  private final Player currentPlayer;

  /** Panel that displays the final state of the game, like the last frame of the race. */
  private final JPanel endState;

  /** Total time spent by the player in the game session, measured in seconds. */
  private final int time;

  /** Number of incorrect inputs made by the player during the game session. */
  private final int wrongChars;

  /** Controller handling client-side operations and interactions. */
  private final ClientController clientController;

  /** Component responsible for playing sounds. */
  private final SoundPlayer soundPlayer;

  /** The model for the ranking table that displays player standings. */
  private DefaultTableModel rankingModel;

  /** The background image for the results screen. */
  private Image backgroundImage;

  /** Flag to indicate whether the current player finished in the first place. */
  private boolean firstPlace = false;

  /**
   * Creates a window with game results.
   *
   * @param gameState the final game state
   * @param currentPlayer the player to display the window for
   * @param wpm the words per minute counter
   * @param accuracy the amount of correctly typed characters
   * @param elapsedTime the time spent in the game
   * @param carPanel for the final race track display
   * @param wrongChars the amount of wrong inputs during the round
   * @param clientController the client controller for this window
   */
  public ResultScreen(
      GameState gameState,
      Player currentPlayer,
      int wpm,
      double accuracy,
      int elapsedTime,
      JPanel carPanel,
      int wrongChars,
      ClientController clientController) {
    this.gameState = gameState;
    this.currentPlayer = currentPlayer;
    this.wpm = wpm;
    this.accuracy = accuracy;
    this.time = elapsedTime;
    this.endState = carPanel;
    this.wrongChars = wrongChars;
    this.clientController = clientController;
    clientController.setResultScreen(this);
    soundPlayer = new SoundPlayer();
    soundPlayer.playSound("result.wav");
    initComponents();
  }

  /** Builds the results screen for the GUI. */
  private void initComponents() {
    setLayout(new BorderLayout());

    setUpRankingTable();
    gameState.setPlayers(computeRankings(gameState.getPlayers()));
    if (gameState.getCompletedPlayers().size() == 1) {
      firstPlace = true;
    }
    // updateRankingTable(gameState.getPlayers());

    try {
      InputStream imageStream =
          getClass().getClassLoader().getResourceAsStream("Result_Moon_2.jpeg");
      if (imageStream != null) {
        backgroundImage = ImageIO.read(imageStream);
      } else {
        System.err.println("Image not found");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    String header = (firstPlace) ? "Congratulations!" : "Game Over!";
    JLabel resultLabel = new JLabel(header);
    resultLabel.setFont(new Font("Nougat", Font.BOLD, 24));
    resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
    resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    resultLabel.setForeground(Color.WHITE);

    JButton newGameButton = new JButton("New Game");
    newGameButton.setFont(new Font("Nougat", Font.PLAIN, 16));
    newGameButton.setBackground(Color.GREEN);
    newGameButton.addActionListener(
        e -> {
          for (TypingPlayer player : clientController.getPlayers()) {
            if (!player.isCompleted()) {
              JOptionPane.showMessageDialog(
                  this,
                  "The current round has not finished yet!",
                  "Round isn't over",
                  JOptionPane.ERROR_MESSAGE);
              return;
            }
          }
          gameState.startNewRace();
          clientController.startNewGame(currentPlayer.getName());
        });

    JButton menuButton = new JButton("Return to Menu");
    menuButton.setFont(new Font("Nougat", Font.PLAIN, 16));
    menuButton.setBackground(Color.WHITE);
    menuButton.addActionListener(
        e -> {
          clientController.playerLeft(currentPlayer.getName());
          clientController.toMainMenu();
        });

    JButton exitButton = new JButton("Exit");
    exitButton.setFont(new Font("Nougat", Font.PLAIN, 16));
    exitButton.setBackground(Color.RED);
    exitButton.setForeground(Color.WHITE);
    exitButton.addActionListener(
        e -> {
          clientController.playerLeft(currentPlayer.getName());
          System.exit(0);
        });

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(newGameButton);
    buttonPanel.add(menuButton);
    buttonPanel.add(exitButton);

    String statText =
        "<html>Time: "
            + time
            + " seconds <br> WPM: "
            + wpm
            + "<br> Accuracy: "
            + (double) Math.round(accuracy * 100) / 100
            + "% <br> Wrong inputs: "
            + wrongChars
            + "</html>";
    JLabel stats = new JLabel(statText);
    stats.setFont(new Font("Nougat", Font.PLAIN, 24));
    stats.setForeground(Color.WHITE);
    stats.setOpaque(false);
    stats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    // stats.setBackground(new Color(184, 112, 247));

    endState.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
    JPanel westPanel = new JPanel(new BorderLayout());
    westPanel.add(stats, BorderLayout.SOUTH);
    westPanel.add(endState, BorderLayout.NORTH);

    resultLabel.setOpaque(false);
    westPanel.setOpaque(false);
    buttonPanel.setOpaque(false);

    add(resultLabel, BorderLayout.NORTH);
    // add(tablePanel, BorderLayout.EAST);
    add(westPanel, BorderLayout.WEST);
    add(buttonPanel, BorderLayout.PAGE_END);
  }

  /**
   * Initializes the ranking table with predefined column names and sets up the rendering for the
   * table. The table is placed within a scroll pane and added to a container which is then added to
   * the east of this panel.
   */
  private void setUpRankingTable() {
    String[] columnNames = {"Rank", "Player", "WPM"};
    rankingModel = new DefaultTableModel(columnNames, 0);
    JTable rankingTable = new JTable(rankingModel);
    rankingTable.getTableHeader().setFont(new Font("Consolas", Font.PLAIN, 16));
    rankingTable.getTableHeader().setBackground(Color.BLACK);
    rankingTable.getTableHeader().setForeground(Color.WHITE);
    rankingTable.setRowHeight(18);

    HighlightRenderer highlightRenderer = new HighlightRenderer(currentPlayer.getName());
    rankingTable.setDefaultRenderer(Object.class, highlightRenderer);

    JScrollPane scrollPane = new JScrollPane(rankingTable);
    scrollPane.setPreferredSize(new Dimension(185, 150));
    scrollPane.getViewport().setBackground(Color.BLACK);
    // scrollPane.getViewport().setBackground(new Color(20, 5, 30));

    JPanel tableContainer = new JPanel(new BorderLayout());
    tableContainer.add(scrollPane, BorderLayout.CENTER);
    tableContainer.setPreferredSize(new Dimension(185, 150));

    rankingTable.setOpaque(false);
    scrollPane.setOpaque(false);
    rankingTable.setShowGrid(false);
    highlightRenderer.setOpaque(false);
    highlightRenderer.setBorder(null);

    add(tableContainer, BorderLayout.EAST);
  }

  /**
   * Updates the ranking table with a new set of ranked players. This method is invoked on the event
   * dispatch thread to ensure thread safety.
   *
   * @param rankedPlayers the list of players sorted by their rank to be displayed in the table.
   */
  public void updateRankingTable(List<TypingPlayer> rankedPlayers) {
    SwingUtilities.invokeLater(
        () -> {
          rankingModel.setRowCount(0);
          for (int i = 0; i < rankedPlayers.size(); i++) {
            Player p = rankedPlayers.get(i);
            rankingModel.addRow(new Object[] {i + 1, p.getName(), p.getWpm()});
          }
        });
  }

  /**
   * Computes the rankings of the players based on their words per minute (WPM) in descending order.
   *
   * @param completedPlayers the list of players who have completed the game.
   * @return a sorted list of players by their WPM.
   */
  public List<TypingPlayer> computeRankings(List<TypingPlayer> completedPlayers) {
    completedPlayers.sort(
        (p1, p2) -> Integer.compare(p2.getWpm(), p1.getWpm())); // Sort descending by WPM
    return completedPlayers;
  }

  /**
   * Paints the background image on the result screen.
   *
   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
  }
}
