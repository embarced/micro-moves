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

    /**
     * Create some game data, if no games available.
     */
    @PostConstruct
    public void createDemoData() {

        if (!gameService.getAllGames().iterator().hasNext()) {

            // Fools mate. See https://en.wikipedia.org/wiki/Fool%27s_mate
            //
            Game foolsMate = gameService.openGame("pinky", 'w');
            Long foolsMateId = foolsMate.getId();
            gameService.enterGame(foolsMateId, "brain");
            gameService.createAndPerformMove(foolsMateId, "f2f3");
            gameService.createAndPerformMove(foolsMateId, "e7e5");
            gameService.createAndPerformMove(foolsMateId, "g2g4");
            gameService.createAndPerformMove(foolsMateId, "d8h4");
            gameService.endGame(foolsMateId, GameResult.BLACK_WINS);

            gameService.openGame("peter", 'b');

            Game thirdGame = gameService.openGame("paul", 'w');
            gameService.enterGame(thirdGame.getId(), "mary");
        }
    }


}
