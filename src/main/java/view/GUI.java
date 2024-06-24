package view;


import controller.client.ClientController;
import game.GameState;
import game.Player;
import game.TypingPlayer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class GUI extends JFrame {
    private JTextField playerNameField;
    private JButton joinButton;
    private GameState gameState;
    private Player currentPlayer;

    private ClientController clientController;

    public GUI(GameState gameState, ClientController clientController) {
        this.gameState = gameState;
        initComponents();

        this.clientController = clientController;
    }

    private void initComponents() {
        setTitle("Type Racer Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showLoginWindow();
    }

    // Login Window GUI
    private void showLoginWindow() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(173, 216, 230));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        loginPanel.add(new JLabel("Welcome To Type Race"), gbc);
        loginPanel.add(new JLabel("Please Enter Your Name"), gbc);

        playerNameField = new JTextField(20);
        loginPanel.add(playerNameField, gbc);

        joinButton = new JButton("Join new Game");
        joinButton.setBackground(Color.green);
        joinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = playerNameField.getText().trim();
                if (!playerName.isEmpty()) {
                    currentPlayer = new TypingPlayer(playerName); // Changed from ExamplePlayer to TypingPlayer
                    gameState.addPlayer(currentPlayer);

                    try {
                        clientController.joinGame(playerName);
                        showGameWindow();
                        startRace();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    JOptionPane.showMessageDialog(GUI.this, "Please enter a name", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        loginPanel.add(joinButton, gbc);

        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    // Game Window GUI
    private void showGameWindow() {
        JPanel gamePanel = new GameScreen(gameState, currentPlayer);
        setContentPane(gamePanel);
        revalidate();
        repaint();
    }

    private void startRace() {
        gameState.startNewRace();
    }

    public static void main(String[] args) {
        GameState gameState = new GameState(); // Assuming GameState has a default constructor
        ClientController clientController = new ClientController();
        SwingUtilities.invokeLater(() -> new GUI(gameState, clientController).setVisible(true));
    }
}
