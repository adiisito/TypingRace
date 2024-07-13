import static org.junit.jupiter.api.Assertions.*;

import game.GameState;
// import game.Player;
import game.TypingPlayer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GameStateTest {

  private GameState gameState;
  private TypingPlayer typingPlayer;

  @BeforeEach
  public void setUp() {
    gameState = new GameState();
    typingPlayer = new TypingPlayer("TypingTestPlayer");
  }

  //    @Test
  //    public void testAddPlayer() {
  //        gameState.addPlayer(typingPlayer);
  //        List<TypingPlayer> players = gameState.getPlayers();
  //        assertEquals(1, players.size());
  //        assertTrue(players.contains(typingPlayer));
  //    }

  @Test
  public void testGetPlayers() {
    List<TypingPlayer> typingPlayers = new ArrayList<>();
    typingPlayers.add(typingPlayer);
    gameState.setPlayers(typingPlayers);
    assertEquals(typingPlayers, gameState.getPlayers());
  }

  @Test
  public void testStartNewRace() {
    gameState.startNewRace();
    assertNotNull(gameState.getCurrentRace());
  }

  @Test
  public void testEndCurrentRace() {
    gameState.startNewRace();
    gameState.endCurrentRace();
    assertNull(gameState.getCurrentRace());
  }

  @Test
  public void testSetStartTime() {
    long startTime = System.currentTimeMillis();
    gameState.setStartTime(startTime);
    assertEquals(startTime, gameState.getStartTime());
  }

  @Test
  public void testGetStartTime() {
    long startTime = System.currentTimeMillis();
    gameState.setStartTime(startTime);
    assertEquals(startTime, gameState.getStartTime());
  }

  @Test
  public void testSetPlayers() {
    List<TypingPlayer> typingPlayers = new ArrayList<>();
    typingPlayers.add(typingPlayer);
    gameState.setPlayers(typingPlayers);
    assertEquals(1, gameState.getPlayers().size());
    assertEquals(typingPlayer, gameState.getPlayers().get(0));
  }
}
