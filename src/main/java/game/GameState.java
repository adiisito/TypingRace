package game;

import java.util.List;

public class GameState implements Game{

    public GameState setGamestate(){
        return null;
    }

    public GameState updateGameState(Typer typer, Car car, Text text){
        return null;
    }

    public double getTime(){
        return 0;
    }
    /**public double setTime(){
        return startRace().incrementTime();
    }*/

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
