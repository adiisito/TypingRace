package view;

import controller.client.ClientController;
import game.GameState;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * The GUI is the starting window for players. From here the player can create a game room, connect
 * to another lobby, and access game settings.
 */
public class Gui extends JFrame {
  /** A list of open client windows. This list helps manage multiple game sessions or views. */
  private final List<ClientWindow> clientWindows = new ArrayList<>();

  /**
   * The controller that manages game and network interactions for this GUI. It facilitates
   * communication between the GUI and the server-side components.
   */
  private final ClientController clientController;

  /** Custom font used throughout the GUI for consistency in styling. */
  public Font dozerFont;

  /** Field for players to enter their name. This is required to join or create a game session. */
  private JTextField playerNameField;

  /**
   * Button that allows players to join an existing game. This action is managed by the
   * clientController.
   */
  private JButton joinButton;

  /**
   * Button that allows players to create a new game room. This action is managed by the
   * clientController.
   */
  private JButton createGameButton;

  /**
   * Creates a GUI instance and builds a screen for the player to interact with.
   *
   * @param clientController the controller to assign to the GUI
   */
  public Gui(ClientController clientController) {
    this.clientController = clientController;
    loadFont();
    initComponents();
  }

  /**
   * The entry point of application.
   *
   * @param args the input arguments
   * @throws IOException the io exception
   */
  public static void main(String[] args) throws IOException {
    GameState gameState = new GameState();
    ClientController controller = new ClientController();
    SwingUtilities.invokeLater(
        () -> {
          Gui mainGui = new Gui(controller);
          controller.setMainGui(mainGui);
          mainGui.setVisible(true);
        });
  }

  private void loadFont() {
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
  }

  private void initComponents() {
    setTitle("SpaceRally");
    setSize(800, 500);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    showLoginWindow();
  }

  private void showLoginWindow() {
    try {
      Image backgroundImage =
          Toolkit.getDefaultToolkit().getImage(getClass().getResource("/1screen4.gif"));
      BackgroundPanel loginPanel = new BackgroundPanel(backgroundImage);
      loginPanel.setLayout(new GridBagLayout());

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.anchor = GridBagConstraints.CENTER;
      gbc.fill = GridBagConstraints.HORIZONTAL;
      gbc.insets = new Insets(10, 10, 10, 10);

      JLabel welcomeLabel = new JLabel("");
      welcomeLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 30));
      welcomeLabel.setForeground(Color.WHITE);
      loginPanel.add(welcomeLabel, gbc);

      animateText(welcomeLabel, "Welcome To SpaceRally");

      JLabel nameLabel = new JLabel("");
      nameLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      nameLabel.setForeground(Color.WHITE);
      loginPanel.add(nameLabel, gbc);

      animateText(nameLabel, "Please Enter Your Name");

      playerNameField = new JTextField(20);
      playerNameField.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      loginPanel.add(playerNameField, gbc);

      JLabel serverLabel = new JLabel("");
      serverLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      serverLabel.setForeground(Color.WHITE);
      loginPanel.add(serverLabel, gbc);

      animateText(serverLabel, "Enter Server IP:");

      JTextField serverIpField = new JTextField(20);
      serverIpField.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      loginPanel.add(serverIpField, gbc);

      createGameButton = new JButton("Create Game");
      createGameButton.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      createGameButton.addActionListener(
          e -> {
            String playerName = playerNameField.getText().trim();

            if (playerName.isEmpty()) {
              JOptionPane.showMessageDialog(
                  Gui.this, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
            } else if (playerName.length() > 7) {
              JOptionPane.showMessageDialog(
                  Gui.this, "Please use a shorter name!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
              try {
                String serverIp = "127.0.0.1";
                createNewClient(playerName);
                clientController.joinGame(playerName, serverIp);
                playerNameField.setText(""); // Clear the text field
                serverIpField.setText("");
              } catch (IOException ex) {
                throw new RuntimeException(ex);
              }
            }
          });
      loginPanel.add(createGameButton, gbc);

      joinButton = new JButton("Join new Game");
      joinButton.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      joinButton.setBackground(Color.green);
      joinButton.addActionListener(
          e -> {
            String playerName = playerNameField.getText().trim();
            String serverIp = serverIpField.getText().trim();

            if (playerName.isEmpty() || serverIp.isEmpty()) {
              JOptionPane.showMessageDialog(
                  Gui.this,
                  "Please enter a name and server IP",
                  "Error",
                  JOptionPane.ERROR_MESSAGE);
            } else if (playerName.length() > 7) {
              JOptionPane.showMessageDialog(
                  Gui.this,
                  "Please use a shorter name!",
                  "Name too long",
                  JOptionPane.ERROR_MESSAGE);
            } else {
              try {
                createNewClient(playerName);
                clientController.joinGame(playerName, serverIp);
                playerNameField.setText(""); // Clear the text field
                serverIpField.setText("");
              } catch (IOException ex) {
                throw new RuntimeException(ex);
              }
            }
          });
      loginPanel.add(joinButton, gbc);

      JButton settingsButton = new JButton("Settings");
      settingsButton.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
      settingsButton.addActionListener(
          e -> {
            SettingsWindow settingsWindow = new SettingsWindow(this);
            settingsWindow.setVisible(true);
          });
      loginPanel.add(settingsButton, gbc);

      setContentPane(loginPanel);
      revalidate();
      repaint();
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(
          this, "Background image not found", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void animateText(JLabel label, String text) {
    Timer timer = new Timer(30, null);
    final int[] index = {0};

    timer.addActionListener(
        e -> {
          if (index[0] < text.length()) {
            label.setText(label.getText() + text.charAt(index[0]));
            index[0]++;
          } else {
            timer.stop();
          }
        });

    timer.start();
  }

  /**
   * Creates a new game client for this GUI instance.
   *
   * @param playerName the player name to display
   */
  public void createNewClient(String playerName) {
    try {
      ClientWindow clientWindow = new ClientWindow(playerName, clientController);
      clientWindows.add(clientWindow);
      clientController.setClientWindow(clientWindow);
      this.dispose();
    } catch (IOException ex) {
      JOptionPane.showMessageDialog(
          this, "Failed to create new client", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Updates all clients with the specified list of players.
   *
   * @param playerNames the list of current players
   */
  public void updateAllClientWindows(List<String> playerNames) {
    for (ClientWindow clientWindow : clientWindows) {
      clientWindow.updatePlayerList(playerNames);
    }
  }

  /**
   * Update all client windows with player left.
   *
   * @param playerName the player name
   */
  public void updateAllClientWindowsWithPlayerLeft(String playerName) {
    for (ClientWindow clientWindow : clientWindows) {
      // clientWindow.showPlayerLeftMessage(playerName);
    }
    List<String> playerNames = new ArrayList<>();
    for (ClientWindow clientWindow : clientWindows) {
      playerNames.add(clientWindow.getPlayerName());
    }
    updateAllClientWindows(playerNames); // Update the player list to reflect the player has left
  }

  /**
   * Retrieves the GUI's client controller.
   *
   * @return the client controller
   */
  public ClientController getClientController() {
    return clientController;
  }
}
