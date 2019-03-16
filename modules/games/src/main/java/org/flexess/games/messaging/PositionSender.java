package org.flexess.games.messaging;

import org.flexess.games.domain.Game;
import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PositionSender {

    @Autowired
    RabbitTemplate rabbitTemplate;

    public void send(Game game) {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("gameId", game.getId());
        jsonObject.put("fen", game.getPosition());

        rabbitTemplate.convertAndSend(MessageConfiguration.positionsExchangeName, "", jsonObject.toString());
    }
}
