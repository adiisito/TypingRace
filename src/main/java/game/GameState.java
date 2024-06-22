package game;

import java.util.List;

public class GameState implements Game{

    public GameState setGamestate(){
        return null;
    }

    public GameState updateGameState(){
        return null;
    }

    public double getTime(){
        return 0;
    }
    public double setTime(){
        return startGame().incrementTime();
    }

    public Typer getPlayer(){
        return null;
    }



    @Override
    public void startRace() {

    }

    @Override
    public void updatePlayerProgress(Player player, int progress) {

    }

    @Override
    public List<Player> getResults() {
        return null;
    }

    @Override
    public void endRace() {

    }
}
