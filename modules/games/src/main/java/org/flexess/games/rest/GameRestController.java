package org.flexess.games.rest;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.Move;
import org.flexess.games.service.GameService;
import org.flexess.games.service.IllegalMoveException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameRestController {

    @Autowired
    GameService gameService;

    @RequestMapping(value = "/api/games", method = RequestMethod.GET)
    public List<GameInfo> allGames() {
        Iterable<Game> games = gameService.getAllGames();
        List<GameInfo> infos = new ArrayList<>();
        for (Game game : games) {
            infos.add(createGameInfo(game));
        }
        return infos;
    }

    @RequestMapping(value = "/api/games/{id}", method = RequestMethod.GET)
    public ResponseEntity<GameDetails> gameById(@PathVariable("id") Long id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(createGameDetails(game));
        } else {
            return ResponseEntity.status(404).build();
        }
    }

    @RequestMapping("/api/games/{id}/moves")
    public List<Move> movesByGamesId(@PathVariable("id") Long id) {
        return gameService.getMovesByGameId(id);
    }

    @RequestMapping(value = "/api/games", method = RequestMethod.POST)
    public ResponseEntity<String> createGame(@RequestBody Game input) {

        // TODO: Change type of parameter
        gameService.openGame(input.getPlayerWhite(), 'b');
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/api/games/{id}/moves", method = RequestMethod.POST)
    public ResponseEntity<String> createMove(
            @PathVariable("id") Long gameId,
            @RequestBody MoveBody input) {

        Game game = gameService.getGameById(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            gameService.createAndPerformMove(gameId, input.getMove());
        } catch (IllegalMoveException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private GameInfo createGameInfo(Game game) {
        GameInfo info = new GameInfo();
        info.setId(game.getId());
        info.setPlayerWhite(game.getPlayerWhite());
        info.setPlayerBlack(game.getPlayerBlack());
        info.setStatus(game.getStatus());
        return info;
    }

    private GameDetails createGameDetails(Game game) {
        GameDetails details = new GameDetails();
        details.setId(game.getId());
        details.setActiveColour(game.getActiveColour());
        details.setCreated(game.getCreated());
        details.setModified(game.getModified());
        details.setFen(game.getPosition());
        details.setFullMoveNumber(game.getFullMoveNumber());
        details.setPlayerBlack(game.getPlayerBlack());
        details.setPlayerWhite(game.getPlayerWhite());
        details.setStatus(game.getStatus());
        return details;
    }

    /**
     * Container for posted moves.
     */
    public static class MoveBody {

        private String move;

        public void setMove(String move) {
            this.move = move;
        }

        public String getMove() {
            return move;
        }

        @Override
        public String toString() {
            return "MoveBody, move=" + move;
        }
    }
}
