package game;

import java.util.ArrayList;
import java.util.List;

public class Typer implements Player{

    private int wpm=0;
    private long startTime;
    private Player newPlayer;
    private List<Player> players;
    private String name;
    private int progress;

    public Typer(String name){
        this.wpm=0;
        this.players = new ArrayList<>(players);
        this.name=name;

    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getWpm() {
        return wpm;
    }

    @Override
    public void setWpm(int wpm) {
        this.wpm=calculateWpm(newPlayer);

    }

    @Override
    public int getProgress() {
        return progress;
    }

    @Override
    public void setProgress(int progress) {
        this.progress=TyperProgress();
    }

    private int calculateWpm(Player player) {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        double elapsedTimeInMinutes = elapsedTime / 60000.0; //This is to convert to minutes.
        int wordsTyped = player.getProgress() / 5;
        // Average word length is considered as 5 characters.
        return (int) (wordsTyped / elapsedTimeInMinutes);
    }

    private void addPlayer(Player newPlayer){
         players.add(newPlayer);

    }

    // gets the player typing progress for getProgress and setProgress
    public int TyperProgress(){
        String typedText = Text.getRandomText();
        int correctChars = 0;

        for (int i = 0; i < typedText.length(); i++) {
            if (typedText.charAt(i) == Text.getRandomText().charAt(i)) {
                correctChars++;
            }
        }

        return correctChars / Text.getRandomText().length() ;

    }

}
