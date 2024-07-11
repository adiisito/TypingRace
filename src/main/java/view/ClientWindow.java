package view;

import controller.client.ClientController;
import game.GameState;
import game.TypingPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

/**
 * The Client window aka Lobby of players where players wait before entering the game!.
 */
public class ClientWindow extends JFrame {
    private String playerName;
    private ClientController clientController;
    private DefaultListModel<String> playerListModel;
    private GameState gameState;
    private Font dozerFont;
    private JButton startButton;
    private JLabel statusLabel;

    /**
     * Instantiates a new Client window.
     *
     * @param playerName       the player name
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
            dozerFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/dozer.ttf")).deriveFont(Font.PLAIN, 20);
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
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    clientController.playerLeft(playerName);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                e.getWindow().dispose();
            }
        });

        showWaitingRoom();
        setVisible(true);
    }

    private void showWaitingRoom() {
        try {
            Image backgroundImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/3screen.gif"));
            BackgroundPanel waitingPanel = new BackgroundPanel(backgroundImage);
            waitingPanel.setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.CENTER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            JLabel waitingLabel = new JLabel("WAITING FOR PLAYERS TO JOIN...");
            waitingLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 14));
            waitingLabel.setForeground(Color.GREEN);
            waitingPanel.add(waitingLabel, gbc);

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

            this.startButton = create3DButton("START GAME");
            startButton.addActionListener(e -> {
                if (clientController.isHost(playerName)) {
                    clientController.startGame(playerName);
                } else {
                    JOptionPane.showMessageDialog(this, "You are not the host. Only the host can start the game.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                }
            });
            buttonPanel.add(Box.createRigidArea(new Dimension(20, 0))); // Space between buttons
            buttonPanel.add(startButton);

            JButton exitButton = create3DButton("EXIT");
            exitButton.addActionListener(e -> {
                try {
                    clientController.playerLeft(playerName);
                    dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            buttonPanel.add(Box.createRigidArea(new Dimension(10, 0))); // Space between buttons
            buttonPanel.add(exitButton);

            waitingPanel.add(buttonPanel, gbc);

            setContentPane(waitingPanel);
            addStatusLabel();
            revalidate();
            repaint();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Background image not found", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JButton create3DButton(String text) {
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
     * Update player list.
     *
     * @param players the players
     */
    public void updatePlayerList(List<String> players) {
        SwingUtilities.invokeLater(() -> {
            playerListModel.clear();
            for (String player : players) {
                playerListModel.addElement(player);
                TypingPlayer newPlayer = new TypingPlayer(player);
                gameState.addPlayer(newPlayer);
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
        JOptionPane.showMessageDialog(this, playerName + " has left the game.", "Player Left", JOptionPane.INFORMATION_MESSAGE);
    }

    public void addStatusLabel () {
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

    /**
     * Show lobby full button.
     */
    public void updateLobbyStatus() {
        if (isLobbyFull()) {
            statusLabel.setText("Lobby full, please start the game");
        } else {
            statusLabel.setText("");
        }
        revalidate();
        repaint();
    }


    private boolean isLobbyFull() {
        return playerListModel.size() == 6;
    }

/*    public void startGame() {
        JPanel gamePanel = new GameScreen(gameState, new TypingPlayer(playerName), clientController);
        setContentPane(gamePanel);
        revalidate();
        repaint();
    }*/

    /**
     * Gets player name.
     *
     * @return the player name
     */
    public String getPlayerName() {
        return playerName;
    }
}
