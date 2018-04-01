package org.flexess.games.rest;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.Move;
import org.flexess.games.service.GameService;
import org.flexess.games.service.IllegalMoveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public List<GameInfoDto> allGames() {
        Iterable<Game> games = gameService.getAllGames();
        List<GameInfoDto> infos = new ArrayList<>();
        games.forEach(g -> infos.add(DtoFactory.createGameInfoDto(g)));
        return infos;
    }

    @RequestMapping(value = "/api/games/{id}", method = RequestMethod.GET)
    public ResponseEntity<GameDetailsDto> gameById(@PathVariable("id") Long id) {
        Game game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(DtoFactory.createGameDetailsDto(game));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

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

    @RequestMapping(value = "/api/games", method = RequestMethod.POST)
    public ResponseEntity<String> createGame(@RequestBody Game input) {

        // TODO: Change type of parameter
        gameService.openGame(input.getPlayerWhite(), 'b');
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
        } catch (IllegalMoveException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
