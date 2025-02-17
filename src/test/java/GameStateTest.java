import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import game.GameState;
import game.TypingPlayer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** The type Game state test. */
public class GameStateTest {

  private GameState gameState;
  private TypingPlayer typingPlayer;

  @BeforeEach
  public void setUp() {
    gameState = new GameState();
    typingPlayer = new TypingPlayer("TypingTestPlayer");
  }

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
