package view;

import controller.client.ClientController;
import game.GameState;
import game.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultScreen extends JPanel {
    private int wpm;
    private double accuracy;
    private GameState gameState;
    private Player currentPlayer;
    private ClientController clientController;

    public ResultScreen(GameState gameState, Player currentPlayer, int wpm, double accuracy, ClientController clientController) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.wpm = wpm;
        this.accuracy = accuracy;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JLabel resultLabel = new JLabel("Game Over");
        resultLabel.setFont(new Font("Serif", Font.BOLD, 24));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel wpmLabel = new JLabel("WPM: " + wpm);
        wpmLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wpmLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel accuracyLabel = new JLabel("Accuracy: " + String.format("%.2f", accuracy) + "%");
        accuracyLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        accuracyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accuracyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.add(wpmLabel);
        statsPanel.add(accuracyLabel);

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Serif", Font.PLAIN, 16));
        newGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameState.startNewRace();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ResultScreen.this);
                frame.setContentPane(new GameScreen(gameState, currentPlayer, clientController));
                frame.revalidate();
                frame.repaint();
            }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Serif", Font.PLAIN, 16));
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);

        add(resultLabel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.PAGE_END);
    }
}
