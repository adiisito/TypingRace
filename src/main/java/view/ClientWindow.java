package view;

import controller.client.ClientController;
import game.GameState;
import game.TypingPlayer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/** The Client window aka Lobby of players where players wait before entering the game!. */
public class ClientWindow extends JFrame {
  /** The name of the player using this client window. */
  private final String playerName;

  /** The controller that manages the interaction between the client and the game server. */
  private final ClientController clientController;

  /** The current state of the game, including all players and game status. */
  private final GameState gameState;

  /** List model for managing the names of players in the lobby. */
  private DefaultListModel<String> playerListModel;

  /** Custom font used for text elements within the client window. */
  private Font dozerFont;

  /** Button that initiates the start of the game. Only enabled for the host. */
  private JButton startButton;

  /** Timer used to animate a series of dots in a label to indicate waiting. */
  private Timer dotAnimationTimer;

  /** Label displaying current status or instructions in the lobby. */
  private JLabel statusLabel;

  /**
   * Instantiates a new Client window.
   *
   * @param playerName the player name
   * @param clientController the client controller
   * @throws IOException the io exception
   */
  public ClientWindow(String playerName, ClientController clientController) throws IOException {
    this.playerName = playerName;
    this.gameState = new GameState();
    this.clientController = clientController;
    clientController.setClientWindow(this);

    // Load Dozer Font
    try {
      dozerFont =
          Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/dozer.ttf"))
              .deriveFont(Font.PLAIN, 20);
      GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      ge.registerFont(dozerFont);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
      dozerFont = new Font("Serif", Font.PLAIN, 20); // Fallback font
    }

    setTitle("Client: " + playerName);
    setSize(800, 600);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLocationRelativeTo(null);

    // Removes player from game when clicking the window's close button
    addWindowListener(
        new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent e) {
            try {
              clientController.playerLeft(playerName);
              dispose();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
            e.getWindow().dispose();
            if (dotAnimationTimer != null) {
              dotAnimationTimer.stop();
            }
          }
        });

    showWaitingRoom();
    setVisible(true);
  }

  /**
   * Sets up and displays the lobby UI with a list of players and control buttons. The UI includes a
   * background, a scrollable player list, and buttons to start or exit the game.
   */
  private void showWaitingRoom() {
    try {
      Image backgroundImage =
          Toolkit.getDefaultToolkit().getImage(getClass().getResource("/3screen.gif"));
      BackgroundPanel waitingPanel = new BackgroundPanel(backgroundImage);
      waitingPanel.setLayout(new GridBagLayout());

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(10, 10, 10, 10);

      JLabel waitingLabel = new JLabel("WAITING FOR PLAYERS TO JOIN");
      waitingLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 14));
      waitingLabel.setForeground(Color.GREEN);
      waitingPanel.add(waitingLabel, gbc);

      // Start the dot animation
      startDotAnimation(waitingLabel);

      playerListModel = new DefaultListModel<>();
      JList<String> playerList = new JList<>(playerListModel);
      playerList.setFont(dozerFont);
      playerList.setForeground(Color.WHITE);
      playerList.setOpaque(false);
      playerList.setBackground(new Color(0, 0, 0, 0)); // Make background transparent
      playerList.setBorder(BorderFactory.createEmptyBorder());

      JScrollPane scrollPane = new JScrollPane(playerList);
      scrollPane.setPreferredSize(new Dimension(400, 300));
      scrollPane.setOpaque(false);
      scrollPane.getViewport().setOpaque(false);
      scrollPane.setBorder(BorderFactory.createEmptyBorder());
      waitingPanel.add(scrollPane, gbc);

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      buttonPanel.setOpaque(false);

      this.startButton = create3dButton("START GAME");
      startButton.addActionListener(
          e -> {
            if (clientController.isHost(playerName)) {
              clientController.startGame(playerName);
            }
          });
      buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Space between buttons
      buttonPanel.add(startButton);

      JButton exitButton = create3dButton("EXIT");
      exitButton.addActionListener(
          e -> {
            try {
              clientController.playerLeft(playerName);
              clientController.toMainMenu();
            } catch (Exception ex) {
              ex.printStackTrace();
            }
          });
      buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons
      buttonPanel.add(exitButton);

      // Settings button
      JButton settingsButton = create3dButton("SETTINGS");
      settingsButton.addActionListener(
          e -> {
            SettingsWindow settingsWindow = new SettingsWindow(clientController.getMainGui());
            settingsWindow.setVisible(true);
          });
      buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons
      buttonPanel.add(settingsButton);

      waitingPanel.add(buttonPanel, gbc);

      setContentPane(waitingPanel);
      addStatusLabel();
      revalidate();
      repaint();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
          this, "Background image not found", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Creates a styled 3D button with specified text. This method configures the button's appearance
   * including font, colors, and border to give it a 3D look.
   *
   * @param text The text to display on the button.
   * @return JButton The configured button with the specified text and 3D styling.
   */
  private JButton create3dButton(String text) {
    JButton button = new JButton(text);
    button.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
    button.setForeground(Color.WHITE);
    button.setBackground(Color.DARK_GRAY);
    button.setBorder(BorderFactory.createBevelBorder(1, Color.WHITE, Color.LIGHT_GRAY));
    button.setFocusPainted(false);
    button.setPreferredSize(new Dimension(200, 40));
    return button;
  }

  /**
   * Initiates a dot animation on a JLabel to indicate a waiting state. This method cyclically
   * appends dots to the label's text every 500 milliseconds, resetting after three dots to convey
   * ongoing activity.
   *
   * @param label The JLabel that displays the waiting message.
   */
  private void startDotAnimation(JLabel label) {
    dotAnimationTimer =
        new Timer(
            500,
            e -> {
              String currentText = label.getText();
              if (currentText.endsWith("...")) {
                label.setText("WAITING FOR PLAYERS TO JOIN");
              } else {
                label.setText(currentText + ".");
              }
            });
    dotAnimationTimer.start();
  }

  /**
   * Update player list.
   *
   * @param players the players
   */
  public void updatePlayerList(List<String> players) {
    SwingUtilities.invokeLater(
        () -> {
          playerListModel.clear();
          for (String player : players) {
            playerListModel.addElement(player);
            TypingPlayer newPlayer = new TypingPlayer(player);
            gameState.addPlayer(newPlayer);
          }
          // System.out.println(clientController.hostPlayer);
          if (!clientController.isHost(playerName) && players.size() > 1) {
            startButton.setForeground(Color.DARK_GRAY);
            startButton.setBackground(new Color(34, 34, 34));
          } else {
            startButton.setForeground(Color.WHITE);
            startButton.setBackground(Color.DARK_GRAY);
          }
          updateLobbyStatus();
        });
  }

  /**
   * Show player left message.
   *
   * @param playerName the player name
   */
  public void showPlayerLeftMessage(String playerName) {
    JOptionPane.showMessageDialog(
        this, playerName + " has left the game.", "Player Left", JOptionPane.INFORMATION_MESSAGE);
  }

  /**
   * Adds a status label to the user interface. This method configures and adds a new label to the
   * main window that displays various status messages. The label is centered and set with specific
   * styling including font size and color.
   */
  public void addStatusLabel() {
    statusLabel = new JLabel();
    statusLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
    statusLabel.setHorizontalAlignment(JLabel.CENTER);
    statusLabel.setForeground(Color.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridwidth = GridBagConstraints.REMAINDER;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.insets = new Insets(10, 10, 10, 10);

    JPanel waitingPanel = (JPanel) getContentPane();
    waitingPanel.add(statusLabel, gbc);
  }

  /** Show lobby full button. */
  public void updateLobbyStatus() {
    if (isLobbyFull()) {
      statusLabel.setText("Lobby full, please start the game");
    } else {
      statusLabel.setText("");
    }
    revalidate();
    repaint();
  }

  /**
   * Determines if the lobby has reached its maximum capacity. Assumes a maximum capacity of 6
   * players.
   *
   * @return True if the player list size is 6, false otherwise.
   */
  private boolean isLobbyFull() {
    return playerListModel.size() == 6;
  }

  /**
   * Gets player name.
   *
   * @return the player name
   */
  public String getPlayerName() {
    return playerName;
  }
}
