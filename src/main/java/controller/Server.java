package controller;

public interface Server {
    /**
     * Processes the request from a client to join the game.
     */
    void processJoinGameRequest(Client client);

    /**
     * Sends a notification to all clients that a player has joined.
     */
    void sendPlayerJoinedNotification(Client client);

    /**
     * Sends a notification to all clients that all players have joined.
     */
    void sendAllJoinedNotification();

    /**
     * Processes the request from a client to start the game.
     */
    void processStartGameRequest();

    /**
     * Sends a notification to all clients that the game has started.
     */
    void sendGameStartNotification();

    /**
     * Processes the progress update request from a client.
     */
    void processUpdateProgressRequest(Client client, int progress);

    /**
     * Sends a notification to all clients with the current game state.
     */
    void sendGameStateNotification();

    /**
     * Processes the request from a client to end the game.
     */
    void processEndGameRequest(Client client);

    /**
     * Sends a notification to all clients that the game has ended.
     */
    void sendGameEndedNotification();

    /**
     * Sends a notification to all clients that all players have ended the game.
     */
    void sendAllEndedNotification();

    /**
     * Processes the request from a client indicating that the player left the game.
     * (opt.)
     */
    void processPlayerLeftRequest(Client client);

    /**
     * Sends a notification to all clients that a player has left the game.
     * (opt.)
     */
    void sendPlayerLeftNotification(Client client);
}
