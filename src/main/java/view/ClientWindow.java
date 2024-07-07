package view;

import network.client.ClientController;
import game.GameState;
import game.TypingPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

public class ClientWindow extends JFrame {
    private String playerName;
    private ClientController clientController;
    private DefaultListModel<String> playerListModel;
    private GameState gameState;
    private Font dozerFont;

    public ClientWindow(String playerName, GUI mainGui) throws IOException {
        this.playerName = playerName;
        this.gameState = new GameState();
        this.clientController = new ClientController(this, mainGui);

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

        clientController.joinGame(playerName);

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
        JPanel waitingPanel = new JPanel(new BorderLayout());
        waitingPanel.setBackground(Color.BLACK);

        JLabel waitingLabel = new JLabel("Waiting for players to join...");
        waitingLabel.setFont(dozerFont.deriveFont(Font.PLAIN, 14));
        waitingLabel.setForeground(Color.WHITE);
        waitingPanel.add(waitingLabel, BorderLayout.NORTH);

        playerListModel = new DefaultListModel<>();
        JList<String> playerList = new JList<>(playerListModel);
        playerList.setFont(dozerFont);
        playerList.setForeground(Color.WHITE);
        playerList.setBackground(Color.DARK_GRAY);
        playerList.setBorder(BorderFactory.createBevelBorder(1, Color.WHITE, Color.LIGHT_GRAY));

        JScrollPane scrollPane = new JScrollPane(playerList);
        scrollPane.setPreferredSize(new Dimension(400, 300));
        waitingPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton startButton = create3DButton("Start Game");
        startButton.addActionListener(e -> clientController.startGame());
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space between buttons
        buttonPanel.add(startButton);

        JButton exitButton = create3DButton("Exit");
        exitButton.addActionListener(e -> {
            try {
                clientController.playerLeft(playerName);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Space between buttons
        buttonPanel.add(exitButton);

        waitingPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(waitingPanel);
        revalidate();
        repaint();
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

    public void updatePlayerList(List<String> players) {
        SwingUtilities.invokeLater(() -> {
            playerListModel.clear();
            for (String player : players) {
                playerListModel.addElement(player);
                TypingPlayer newPlayer = new TypingPlayer(player);
                gameState.addPlayer(newPlayer);

            }
        });
    }

    public void showPlayerLeftMessage(String playerName) {
        JOptionPane.showMessageDialog(this, playerName + " has left the game.", "Player Left", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLobbyFullButton() {
        JButton lobbyFullButton = create3DButton("Lobby full, Start the Game");
        lobbyFullButton.addActionListener(e -> clientController.startGame());
        JPanel waitingPanel = (JPanel) getContentPane();
        waitingPanel.add(lobbyFullButton, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

/*    public void startGame() {
        JPanel gamePanel = new GameScreen(gameState, new TypingPlayer(playerName), clientController);
        setContentPane(gamePanel);
        revalidate();
        repaint();
    }*/

    public String getPlayerName() {
        return playerName;
    }
}
