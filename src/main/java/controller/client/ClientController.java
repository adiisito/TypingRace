package controller.client;

import communication.messages.JoinGameRequest;

import java.io.IOException;

public class ClientController {

    private GameClient clientModel;

    public ClientController() {
        super();
    }

    public void joinGameRequest(String playerName) throws IOException {
        this.clientModel = new GameClient(this);

        JoinGameRequest joinRequest = new JoinGameRequest(playerName);
        clientModel.sendMessage(joinRequest);
    }
}
