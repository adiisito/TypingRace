package controller.newGame;

import game.Game;
import game.GameState;
import view.GUI;

import javax.swing.*;

public class GameController {

    private GUI View;
    private Game newGame;


    public static void main(String[] args) {
        GameState gameState = new GameState();
        SwingUtilities.invokeLater(() -> new GUI(gameState).setVisible(true));
    }

}
