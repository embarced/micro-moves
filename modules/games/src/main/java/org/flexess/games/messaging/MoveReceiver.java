package org.flexess.games.messaging;


import org.flexess.games.domain.Game;
import org.flexess.games.rulesclient.MoveValidationNotPossibleException;
import org.flexess.games.service.GameService;
import org.flexess.games.websocket.WebSocketPublisher;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MoveReceiver {

    @Autowired
    GameService gameService;

    @Autowired
    WebSocketPublisher webSocketPublisher;

    public void receiveMessage(String message) {
        System.out.println("Received <" + message + ">");

        long gameId = -1;
        String move = null;

        JSONObject jsonObject = new JSONObject(message);
        if (jsonObject.has("gameId")) {
            gameId = jsonObject.getLong("gameId");
        }
        if (jsonObject.has("move")) {
            move = jsonObject.getString("move");
        }

        try {
            if (gameId != -1 && move != null) {
                gameService.createAndPerformMove(gameId, move);
                Game game = gameService.getGameById(gameId);
                webSocketPublisher.publish(game);
            }
        } catch (MoveValidationNotPossibleException e) {
            e.printStackTrace();
        }

    }
}