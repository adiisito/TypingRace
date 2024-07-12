package view;

import controller.client.ClientController;
import game.GameState;
import game.Player;
import game.TypingPlayer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
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

    private Image backgroundImage;
    private SoundPlayer soundPlayer;
    private boolean firstPlace = false;

    /**
     * Creates a window with game results.
     * @param gameState the final game state
     * @param currentPlayer the player to display the window for
     * @param wpm the words per minute counter
     * @param accuracy the amount of correctly typed characters
     * @param elapsedTime the time spent in the game
     * @param carPanel for the final race track display
     * @param clientController the client controller for this window
     */
    public ResultScreen(GameState gameState, Player currentPlayer, int wpm,
                        double accuracy, int elapsedTime, JPanel carPanel,
                        ClientController clientController) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.wpm = wpm;
        this.accuracy = accuracy;
        this.time = elapsedTime;
        this.endState = carPanel;
        this.clientController = clientController;
        clientController.setResultScreen(this);
        soundPlayer = new SoundPlayer();
        soundPlayer.playSound("rssound.wav");
        initComponents();
    }

    /**
     * Builds the results screen for the GUI.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        setUpRankingTable();
        gameState.setPlayers(computeRankings(gameState.getPlayers()));
        if (gameState.getCompletedPlayers().size() == 1) {
            firstPlace = true;
        }
        // updateRankingTable(gameState.getPlayers());

        try {
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("Result_Moon.jpeg");
            if (imageStream != null) {
                backgroundImage = ImageIO.read(imageStream);
            } else {
                System.err.println("Image not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String header = (firstPlace) ? "Congratulations!" : "Game Over!";
        JLabel resultLabel = new JLabel(header);
        resultLabel.setFont(new Font("Nougat", Font.BOLD, 24));
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        resultLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        resultLabel.setForeground(Color.WHITE);

        JLabel wpmLabel = new JLabel("WPM: " + wpm);
        wpmLabel.setFont(new Font("Nougat", Font.PLAIN, 18));
        wpmLabel.setHorizontalAlignment(SwingConstants.CENTER);
        wpmLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel accuracyLabel = new JLabel("Accuracy: " + accuracy + "%");
        accuracyLabel.setFont(new Font("Nougat", Font.PLAIN, 18));
        accuracyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        accuracyLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Nougat", Font.PLAIN, 16));
        newGameButton.setBackground(Color.BLACK);
        newGameButton.setBackground(Color.WHITE);
        newGameButton.addActionListener(e -> {
            gameState.startNewRace();
            clientController.startNewGame(currentPlayer.getName());
        });

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Nougat", Font.PLAIN, 16));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
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
        stats.setFont(new Font("Nougat", Font.PLAIN, 24));
        stats.setForeground(Color.WHITE);
        stats.setOpaque(false);
        stats.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        //stats.setBackground(new Color(184, 112, 247));

        endState.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 10));
        JPanel westPanel = new JPanel(new BorderLayout());
        westPanel.add(stats, BorderLayout.SOUTH);
        westPanel.add(endState, BorderLayout.NORTH);

        resultLabel.setOpaque(false);
        westPanel.setOpaque(false);
        accuracyLabel.setOpaque(false);
        buttonPanel.setOpaque(false);

        add(resultLabel, BorderLayout.NORTH);
        // add(tablePanel, BorderLayout.EAST);
        add(westPanel, BorderLayout.WEST);
        add(accuracyLabel, BorderLayout.SOUTH);
        add(buttonPanel, BorderLayout.PAGE_END);
    }

    /**
     * Initializes the ranking table with predefined column names and sets up the rendering for the table.
     * The table is placed within a scroll pane and added to a container which is then added to the east of this panel.
     */
    private void setUpRankingTable() {
        String[] columnNames = {"Rank", "Player", "WPM"};
        rankingModel = new DefaultTableModel(columnNames, 0);
        JTable rankingTable = new JTable(rankingModel);
        rankingTable.getTableHeader().setFont(new Font("Consolas", Font.PLAIN, 16));
        rankingTable.getTableHeader().setBackground(Color.BLACK);
        rankingTable.getTableHeader().setForeground(Color.WHITE);
        rankingTable.setRowHeight(18);

        HighlightRenderer highlightRenderer = new HighlightRenderer(currentPlayer.getName());
        rankingTable.setDefaultRenderer(Object.class, highlightRenderer);

        JScrollPane scrollPane = new JScrollPane(rankingTable);
        scrollPane.setPreferredSize(new Dimension(185, 150));
        scrollPane.getViewport().setBackground(Color.BLACK);
        //scrollPane.getViewport().setBackground(new Color(20, 5, 30));

        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.add(scrollPane, BorderLayout.CENTER);
        tableContainer.setPreferredSize(new Dimension(185, 150));;

        rankingTable.setOpaque(false);
        scrollPane.setOpaque(false);
        rankingTable.setShowGrid(false);
        highlightRenderer.setOpaque(false);
        highlightRenderer.setBorder(null);

        add(tableContainer, BorderLayout.EAST);
    }

    /**
     * Updates the ranking table with a new set of ranked players.
     * This method is invoked on the event dispatch thread to ensure thread safety.
     *
     * @param rankedPlayers the list of players sorted by their rank to be displayed in the table.
     */
    public void updateRankingTable(List<TypingPlayer> rankedPlayers) {
        SwingUtilities.invokeLater(() -> {
            rankingModel.setRowCount(0);
            for (int i = 0; i < rankedPlayers.size(); i++) {
                Player p = rankedPlayers.get(i);
                rankingModel.addRow(new Object[]{i + 1, p.getName(), p.getWpm()});
            }
        });
    }

    /**
     * Computes the rankings of the players based on their words per minute (WPM) in descending order.
     *
     * @param completedPlayers the list of players who have completed the game.
     * @return a sorted list of players by their WPM.
     */
    public List<TypingPlayer> computeRankings(List<TypingPlayer> completedPlayers) {
        completedPlayers.sort((p1, p2) -> Integer.compare(p2.getWpm(), p1.getWpm()));  // Sort descending by WPM
        return completedPlayers;
    }

    /**
     * Paints the background image on the result screen.
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
