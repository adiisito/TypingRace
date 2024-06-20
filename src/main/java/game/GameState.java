package game;

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
    public String setText() {
        return null;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public Race startGame() {
        return null;
    }

    @Override
    public Race endGame() {
        return null;
    }
}
