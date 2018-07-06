package org.flexess.games.websocket;

import org.flexess.games.domain.Game;
import org.flexess.games.rest.DtoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebSocketPublisher {

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    public void publish(Game game) {
        messagingTemplate.convertAndSend("/topic/game_"+game.getId(), DtoFactory.createGameDetailsDto(game));
    }
}
