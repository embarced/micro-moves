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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class GameRestController {

    private static final Logger LOG = LoggerFactory.getLogger(GameRestController.class);

    @Autowired
    GameService gameService;

    @RequestMapping(value = "/api/games", method = RequestMethod.GET)
    public Iterable<Game> allGames() {
        return gameService.getAllGames();
    }

    @RequestMapping("/api/games/{id}")
    public ResponseEntity<Game> gameById(@PathVariable("id") Long id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(game);
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

        Game game = gameService.createGame(input.getPlayerWhite(), input.getPlayerBlack());
        LOG.debug(game + " created.");

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(value = "/api/games/{id}/moves", method = RequestMethod.POST)
    public ResponseEntity<String> createMove(
                                             @PathVariable("id") Long id,
                                             @RequestBody MoveBody input) {

        Game game = gameService.getGameById(id);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            Move move = gameService.createAndPerformMove(id, input.getMove());
            LOG.debug(move + " created.");
        } catch (IllegalMoveException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
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
