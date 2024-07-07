package controller.newGame;

import game.Game;
import game.GameState;
import game.Player;
import view.GUI;
import view.GameScreen;

import javax.swing.*;
import java.awt.event.KeyEvent;

public class GameController {

    private GUI View;
    private Game newGame;


    public static void main(String[] args) {
        GameState gameState = new GameState();
        SwingUtilities.invokeLater(() -> new GUI(gameState).setVisible(true));
    }

}
