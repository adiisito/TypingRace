package game;

import controller.client.ClientController;
import view.ResultScreen;
import view.GameScreen;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class TypeRace {

    private static GameState gameState;
    private static long startTime;

    public TypeRace(GameState gameState){
        this.gameState = gameState;
    }

    public static int calculateProgress(String typedText) {
        int maxLength = Math.min(typedText.length(), GameScreen.providedText.length());
        int correctChars = 0;
        for (int i = 0; i < maxLength; i++) {
            if (typedText.charAt(i) == GameScreen.providedText.charAt(i)) {
                correctChars++;
            } else {
                break; // Stop at the first incorrect character
            }
        }
        System.out.println("Calculated progress: " + correctChars + " characters");
        return correctChars;
    }

    public static double calculateAccuracy(String typedText) {
        int maxLength = Math.min(typedText.length(), GameScreen.providedText.length());
        int correctChars = 0;
        for (int i = 0; i < maxLength; i++) {
            if (typedText.charAt(i) == GameScreen.providedText.charAt(i)) {
                correctChars++;
            }
        }
        return GameScreen.keyPressCount == 0 ? 100 : (correctChars * 100.0) / GameScreen.keyPressCount;
    }

    public static int calculateWpm() {
        long elapsedTime = System.currentTimeMillis() - gameState.getStartTime();
        double elapsedMinutes = elapsedTime / 60000.0;
        int totalWords = GameScreen.keyPressCount / 5;
        return (int) (totalWords / elapsedMinutes);
    }

    public List<TypingPlayer> computeRankings(List<TypingPlayer> completedPlayers) {
        completedPlayers.sort((p1, p2) -> Integer.compare(p2.getWpm(), p1.getWpm()));  // Sort descending by WPM
        return completedPlayers;
    }
}
