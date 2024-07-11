package view;

import controller.client.ClientController;
import game.GameState;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GUI extends JFrame {
    private JTextField playerNameField;
    private JButton joinButton;
    private JButton createGameButton;
    private GameState gameState;
    private DefaultListModel<String> playerListModel;
    private List<ClientWindow> clientWindows = new ArrayList<>();
    private Font dozerFont;
    private ClientController clientController;

    public GUI(GameState gameState, ClientController clientController) {
        this.gameState = gameState;
        this.clientController = clientController;
        loadFont();
        initComponents();
    }

    private void loadFont() {
        try {
            dozerFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/dozer.ttf")).deriveFont(Font.PLAIN, 20);
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
            Image backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/1screen4.gif"));
            BackgroundPanel loginPanel = new BackgroundPanel(backgroundImage);
            loginPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel welcomeLabel = new JLabel("Welcome To SpaceRally");
            welcomeLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 30));
            welcomeLabel.setForeground(Color.WHITE);
            loginPanel.add(welcomeLabel, gbc);

            JLabel nameLabel = new JLabel("Please Enter Your Name");
            nameLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
            nameLabel.setForeground(Color.WHITE);
            loginPanel.add(nameLabel, gbc);

            playerNameField = new JTextField(20);
            playerNameField.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
            loginPanel.add(playerNameField, gbc);

            JLabel serverLabel = new JLabel("Enter Server IP:");
            serverLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
            serverLabel.setForeground(Color.WHITE);
            loginPanel.add(serverLabel, gbc);

            JTextField serverIPField = new JTextField(20);
            serverIPField.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
            loginPanel.add(serverIPField, gbc);

            createGameButton = new JButton("Create Game");
            createGameButton.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
            createGameButton.addActionListener(e -> {
                String playerName = playerNameField.getText().trim();
                try {
                    String serverIP = "127.0.0.1";
                    createNewClient(playerName);
                    clientController.joinGame(playerName, serverIP);
                    playerNameField.setText(""); // Clear the text field
                    serverIPField.setText("");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
            loginPanel.add(createGameButton, gbc);

            joinButton = new JButton("Join new Game");
            joinButton.setFont(dozerFont.deriveFont(Font.PLAIN, 20));
            joinButton.setBackground(Color.green);
            joinButton.addActionListener(e -> {
                String playerName = playerNameField.getText().trim();
                String serverIP = serverIPField.getText().trim();

                if (!playerName.isEmpty() && !serverIP.isEmpty()) {

                    try {
                        createNewClient(playerName);
                        clientController.joinGame(playerName, serverIP);
                        playerNameField.setText(""); // Clear the text field
                        serverIPField.setText("");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Please enter a name or an IP-Address", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            loginPanel.add(joinButton, gbc);

            setContentPane(loginPanel);
            revalidate();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Background image not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void createNewClient(String playerName) {
        try {
            ClientWindow clientWindow = new ClientWindow(playerName, clientController);
            clientWindows.add(clientWindow);
            clientController.setClientWindow(clientWindow);
            this.dispose();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Failed to create new client", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void updateAllClientWindows(List<String> playerNames) {
        for (ClientWindow clientWindow : clientWindows) {
            clientWindow.updatePlayerList(playerNames);
        }
    }

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

    public static void main(String[] args) throws IOException {
        GameState gameState = new GameState();
        ClientController controller = new ClientController();
        SwingUtilities.invokeLater(() -> {
            GUI mainGui = new GUI(gameState, controller);
            controller.setMainGui(mainGui);
            mainGui.setVisible(true);
        });
    }

}
