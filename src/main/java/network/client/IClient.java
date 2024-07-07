package network.client;

public interface IClient {
    /**
     * Sends a request to join the game.
     */
    void joinGameRequest();

    /**
     * Sends a request to start the game.
     */
    void startGameRequest();

    /**
     * Sends a progress update to the server.
     */
    void updateProgressRequest();

    /**
     * Sends a request to end the game.
     */
    void endGameRequest();

    /**
     * Sends a request indicating that the player left the game.
     */
    void playerLeftRequest();




    /**
     * Receives a notification when a player joins.
     */
    void playerJoinedNotification();

    /**
     * Receives a notification when all players have joined.
     */
    void allJoinedNotification();

    /**
     * Receives a notification when the game starts.
     */
    void gameStartNotification();

    /**
     * Receives a notification with the game state.
     */
    void gameStateNotification();

    /**
     * Receives a notification when the game ends.
     */
    void gameEndedNotification();

    /**
     * Receives a notification when all players have ended the game.
     */
    void allEndedNotification();

    /**
     * Receives a notification when a player leaves the game.
     */
    void playerLeftNotification();
}
