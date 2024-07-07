package view;

import controller.client.ClientController;
import game.GameState;
import game.Player;
import game.TypeRace;
import game.TypingPlayer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;


/**
 * Creates a result screen when the game ends.
 */
public class ResultScreen extends JPanel {
    private final int wpm;
    private final double accuracy;
    private final GameState gameState;
    private final Player currentPlayer;
    private final JPanel endState;
    private final int time;
    private ClientController clientController;
    private DefaultTableModel rankingModel;
    private TypeRace typeRace;

    /**
     * Creates a window with game results.
     * @param gameState the final game state
     * @param currentPlayer the player to display the window for
     * @param wpm the words per minute counter
     * @param accuracy the amount of correctly typed characters
     * @param elapsedTime the time spent in the game
     * @param carPanel the final race track display
     * @param clientController the client controller for this window
     */
    public ResultScreen(GameState gameState, Player currentPlayer, int wpm, double accuracy,
                        int elapsedTime, JPanel carPanel, ClientController clientController, TypeRace typeRace) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.endState = carPanel;
        this.time = elapsedTime;
        this.clientController = clientController;
        this.typeRace = typeRace;
        initComponents();
    }

    /**
     * Builds the results screen for the GUI.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        setBackground(new Color(57, 174, 207));

        JLabel resultLabel = new JLabel("Game Over");
        resultLabel.setFont(new Font("Serif", Font.BOLD, 24));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel wpmLabel = new JLabel("WPM: " + wpm);
        wpmLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wpmLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel accuracyLabel = new JLabel("Accuracy: " + accuracy + "%");
        accuracyLabel.setFont(new Font("Serif", Font.PLAIN, 18));
        accuracyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accuracyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Serif", Font.PLAIN, 16));
        newGameButton.addActionListener(e -> {
            gameState.startNewRace();
          try {
              JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
              frame.setVisible(false);
              frame = new ClientWindow(currentPlayer.getName(), clientController.getMainGui());
              frame.revalidate();
              frame.repaint();
          } catch (IOException ex) {
            throw new RuntimeException(ex);
          }
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Serif", Font.PLAIN, 16));
        exitButton.addActionListener(e -> {
            clientController.playerLeft(currentPlayer.getName());
            System.exit(0);

        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(newGameButton);
        buttonPanel.add(exitButton);

        String stat_text = "<html>WPM: " + wpm + " <br> Accuracy: " +
                (double) Math.round(accuracy * 100) / 100 + "% <br> Time: " + time + " seconds </html>";
        JLabel stats = new JLabel(stat_text);
        stats.setFont(new Font("Consolas", Font.PLAIN, 24));
        stats.setOpaque(true);
        stats.setBackground(new Color(184, 112, 247));

        setUpRankingTable();
        gameState.setPlayers(typeRace.computeRankings(gameState.getPlayers()));
        // updateRankingTable(gameState.getPlayers());

        endState.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(stats, BorderLayout.SOUTH);
        westPanel.add(endState, BorderLayout.NORTH);

        add(resultLabel, BorderLayout.NORTH);
        // add(tablePanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
        add(accuracyLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    private void setUpRankingTable() {
        String[] columnNames = {"Rank", "Player", "WPM"};
        rankingModel = new DefaultTableModel(columnNames, 0);
        JTable rankingTable = new JTable(rankingModel);

        HighlightRenderer highlightRenderer = new HighlightRenderer(currentPlayer.getName());
        rankingTable.setDefaultRenderer(Object.class, highlightRenderer);

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setPreferredSize(new Dimension(185, 150));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.setPreferredSize(new Dimension(185, 150));

        add(tableContainer, BorderLayout.EAST);
    }

    public void updateRankingTable(List<TypingPlayer> rankedPlayers) {
        SwingUtilities.invokeLater(() -> {
            rankingModel.setRowCount(0);
            for (int i = 0; i < rankedPlayers.size(); i++) {
                Player p = rankedPlayers.get(i);
                rankingModel.addRow(new Object[]{i + 1, p.getName(), p.getWpm()});
            }
        });
    }

}
