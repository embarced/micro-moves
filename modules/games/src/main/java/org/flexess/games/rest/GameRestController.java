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
    public List<GameInfo> allGames() {
        Iterable<Game> games = gameService.getAllGames();
        List<GameInfo> infos = new ArrayList<>();
        games.forEach(g -> infos.add(createGameInfo(g)));
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

    @RequestMapping(value = "/api/games/{id}/moves", method = RequestMethod.GET)
    public List<MoveInfo> movesByGamesId(@PathVariable("id") Long id) {
        List<Move> moves = gameService.getMovesByGameId(id);
        List<MoveInfo> moveInfos = new ArrayList<>();
        moves.forEach(m -> moveInfos.add(createMoveInfo(m)));
        return moveInfos;
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
            @RequestBody MoveInfo input) {

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

    private MoveInfo createMoveInfo(Move move) {
        MoveInfo info = new MoveInfo();
        info.setId(move.getId());
        info.setNumber(move.getNumber());
        info.setText(move.getText());
        info.setCreated(move.getCreated());
        return info;
    }
}
