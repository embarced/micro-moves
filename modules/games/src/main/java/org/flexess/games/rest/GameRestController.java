package org.flexess.games.rest;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.GameStatus;
import org.flexess.games.domain.Move;
import org.flexess.games.service.GameService;
import org.flexess.games.service.IllegalMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * REST API for games and moves.
 *
 * @author stefanz
 */
@CrossOrigin(origins = "*")
@RestController
public class GameRestController {

    @Autowired
    GameService gameService;

    @Autowired
    SimpMessagingTemplate messagingTemplate;

    /**
     * Provides essential information for all games.
     */
    @RequestMapping(value = "/api/games", method = RequestMethod.GET)
    public List<GameInfoDto> allGames() {
        Iterable<Game> games = gameService.getAllGames();
        List<GameInfoDto> infos = new ArrayList<>();
        games.forEach(g -> infos.add(DtoFactory.createGameInfoDto(g)));
        return infos;
    }

    /**
     * Provides detailed information for a single game.
     *
     * @param id id the game.
     */
    @RequestMapping(value = "/api/games/{id}", method = RequestMethod.GET)
    public ResponseEntity<GameDetailsDto> gameById(@PathVariable("id") Long id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(DtoFactory.createGameDetailsDto(game));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Provides the moves of a game.
     *
     * @param id game ID
     */
    @RequestMapping(value = "/api/games/{id}/moves", method = RequestMethod.GET)
    public ResponseEntity<List<MoveDto>> movesByGamesId(@PathVariable("id") Long id) {
        Game game = gameService.getGameById(id);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<Move> moves = gameService.getMovesByGameId(id);
        List<MoveDto> moveDtos = new ArrayList<>();
        moves.forEach(m -> moveDtos.add(DtoFactory.createMoveDto(m)));
        return ResponseEntity.ok(moveDtos);
    }

    /**
     * Create a new game.
     *
     * @param input At least one of the two players must be given.
     */
    @RequestMapping(value = "/api/games", method = RequestMethod.POST)
    public ResponseEntity<String> createGame(@RequestBody GameInfoDto input) {

        String black = input.getPlayerBlack();
        String white = input.getPlayerWhite();

        Game game = null;

        if (black == null & white == null) {
            return new ResponseEntity<String>("No player given.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (black != null & white == null) {
            game = gameService.openGame(black, 'b');
        }

        if (black == null & white != null) {
            game = gameService.openGame(white, 'w');

        }

        if (black != null & white != null) {
            game = gameService.openGame(white, 'w');
            gameService.enterGame(game.getId(), black);
        }

        return new ResponseEntity<String>(game + " created.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/api/games", method = RequestMethod.PUT)
    public ResponseEntity<String> enterGame(@RequestBody GameInfoDto input) {

        Game game = gameService.getGameById(input.getGameId());
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if (game.getStatus() != GameStatus.OPEN) {
            return new ResponseEntity<String>("Game not open.", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (game.getPlayerWhite() == null && input.getPlayerWhite() != null) {
            gameService.enterGame(game.getId(), input.getPlayerWhite());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        if (game.getPlayerBlack() == null && input.getPlayerBlack() != null) {
            gameService.enterGame(game.getId(), input.getPlayerBlack());
            return ResponseEntity.status(HttpStatus.ACCEPTED).build();
        }

        return new ResponseEntity<String>("Game not entered.", HttpStatus.UNPROCESSABLE_ENTITY);

    }

    /**
     * Create and perform a move.
     *
     * @param gameId ID of the game
     * @param input move to perform
     */
    @RequestMapping(value = "/api/games/{id}/moves", method = RequestMethod.POST)
    public ResponseEntity<String> createMove(
            @PathVariable("id") Long gameId,
            @RequestBody MoveDto input) {

        Game game = gameService.getGameById(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        try {
            gameService.createAndPerformMove(gameId, input.getText());
            messagingTemplate.convertAndSend("/topic/game_"+gameId, DtoFactory.createGameDetailsDto(game));
        } catch (IllegalMoveException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
