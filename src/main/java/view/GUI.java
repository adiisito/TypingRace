// GUI Class
package view;

import game.GameState;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class GUI extends JFrame {
    private JTextField playerNameField;
    private JButton joinButton;
    private GameState gameState;
    private DefaultListModel<String> playerListModel;
    private List<ClientWindow> clientWindows = new ArrayList<>();

    public GUI(GameState gameState) {
        this.gameState = gameState;
        initComponents();
    }

    private void initComponents() {
        setTitle("Type Racer Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginWindow();
    }

    private void showLoginWindow() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(158, 255, 199));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        loginPanel.add(new JLabel("Welcome To KeySprint"), gbc);
        loginPanel.add(new JLabel("Please Enter Your Name"), gbc);

        playerNameField = new JTextField(20);
        loginPanel.add(playerNameField, gbc);

        joinButton = new JButton("Join new Game");
        joinButton.setBackground(Color.green);
        joinButton.addActionListener(e -> {
            String playerName = playerNameField.getText().trim();
            if (!playerName.isEmpty()) {
                createNewClient(playerName);
            } else {
                JOptionPane.showMessageDialog(GUI.this, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        loginPanel.add(joinButton, gbc);

        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private void createNewClient(String playerName) {
        try {
            ClientWindow clientWindow = new ClientWindow(playerName, this);
            clientWindows.add(clientWindow);
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
            clientWindow.showPlayerLeftMessage(playerName);
        }
        List<String> playerNames = new ArrayList<>();
        for (ClientWindow clientWindow : clientWindows) {
            playerNames.add(clientWindow.getPlayerName());
        }
        updateAllClientWindows(playerNames); // Update the player list to reflect the player has left
    }

    public static void main(String[] args) {
        GameState gameState = new GameState();
        SwingUtilities.invokeLater(() -> new GUI(gameState).setVisible(true));
    }
}
