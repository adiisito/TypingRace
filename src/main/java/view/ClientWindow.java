//for better understanding you can also call it "lobby"/"waiting room"
package view;

import controller.client.ClientController;
import game.GameState;
import game.TypingPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ClientWindow extends JFrame {
    private String playerName;
    private ClientController clientController;
    private DefaultListModel<String> playerListModel;
    private GameState gameState;

    public ClientWindow(String playerName, GUI mainGui) throws IOException {
        this.playerName = playerName;
        this.gameState = new GameState();
        this.clientController = new ClientController(this, mainGui);

        setTitle("Client: " + playerName);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        clientController.joinGame(playerName);

        showWaitingRoom();
        setVisible(true);
    }

    private void showWaitingRoom() {
        JPanel waitingPanel = new JPanel(new BorderLayout());
        waitingPanel.add(new JLabel("Waiting for players to join..."), BorderLayout.NORTH);

        playerListModel = new DefaultListModel<>();
        JList<String> playerList = new JList<>(playerListModel);
        JScrollPane scrollPane = new JScrollPane(playerList);
        waitingPanel.add(scrollPane, BorderLayout.CENTER);

        JButton startButton = new JButton("Start Game");
        startButton.addActionListener(e -> clientController.startGame());
        waitingPanel.add(startButton, BorderLayout.SOUTH);

        showExitButton();

        setContentPane(waitingPanel);
        revalidate();
        repaint();
    }

    public void updatePlayerList(List<String> players) {
        SwingUtilities.invokeLater(() -> {
            playerListModel.clear();
            for (String player : players) {
                playerListModel.addElement(player);
            }
        });
    }

    public void showPlayerLeftMessage(String playerName) {
        JOptionPane.showMessageDialog(this, playerName + " has left the game.", "Player Left", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showLobbyFullButton() {
        JButton lobbyFullButton = new JButton("Lobby full, Start the Game");
        lobbyFullButton.addActionListener(e -> clientController.startGame());
        JPanel waitingPanel = (JPanel) getContentPane();
        waitingPanel.add(lobbyFullButton, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void showExitButton() {
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> {
            try {
                clientController.playerLeft(playerName);
                dispose();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        JPanel panel = (JPanel) getContentPane();
        panel.add(exitButton, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

//    public void startGame() {
//        JPanel gamePanel = new GameScreen(gameState, new TypingPlayer(playerName));
//        showExitButton();
//        setContentPane(gamePanel);
//        revalidate();
//        repaint();
//    }

    public String getPlayerName() {
        return playerName;
    }
}
