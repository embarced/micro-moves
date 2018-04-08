package org.flexess.games;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.GameResult;
import org.flexess.games.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Creation of some games for test purposes.
 */
@Component
public class InitDemoData {

    @Autowired
    GameService gameService;

    @PostConstruct
    public void createDemoData() {

        if (! gameService.getAllGames().iterator().hasNext()) {

            Game foolsMate = gameService.openGame("pinky", 'w');
            gameService.enterGame(foolsMate.getId(), "brain");
            gameService.createAndPerformMove(foolsMate.getId(), "f2f3");
            gameService.createAndPerformMove(foolsMate.getId(), "e7e5");
            gameService.createAndPerformMove(foolsMate.getId(), "g2g4");
            gameService.createAndPerformMove(foolsMate.getId(), "d8h4");
            gameService.endGame(foolsMate.getId(), GameResult.BLACK_WINS);

            gameService.openGame("peter", 'b');
        }
    }


}
