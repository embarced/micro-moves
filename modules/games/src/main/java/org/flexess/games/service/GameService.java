package org.flexess.games.service;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.GameRepository;
import org.flexess.games.domain.GameResult;
import org.flexess.games.domain.GameStatus;
import org.flexess.games.domain.Move;
import org.flexess.games.domain.MoveRepository;
import org.flexess.games.domain.Position;
import org.flexess.games.messaging.PositionSender;
import org.flexess.games.rulesclient.MoveValidationNotPossibleException;
import org.flexess.games.rulesclient.RulesClient;
import org.flexess.games.rulesclient.ValidateMoveResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Business logic for the game service.
 *
 * @author stefanz
 */
@Service
@Transactional
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MoveRepository moveRepository;

    @Autowired
    private RulesClient rulesClient;

    @Autowired
    private PositionSender positionSender;

    /**
     * Get a game by its ID.
     *
     * @param id ID of the game
     * @return the game, or null if not found.
     */
    public Game getGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);
        return game.orElse(null);
    }

    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Move> getMovesByGameId(Long id) {
        Game game = this.getGameById(id);
        return moveRepository.findByGame(game);
    }

    /**
     * Open a new game. Status is OPEN afterwards -- it waits for the second player.
     *
     * @param player first player in the game
     * @param colour colour of the given player, 'w' for white, 'b' for black.
     * @return game created.
     */
    public Game openGame(String player, char colour) {

        Game game = new Game();
        switch (colour) {
            case 'w':
                game.setPlayerWhite(player);
                break;
            case 'b':
                game.setPlayerBlack(player);
                break;
            default:
                throw new IllegalArgumentException("'" + colour + "' is not a valid colour. Use 'w' or 'b'");
        }

        Position pos = new Position();
        game.setPosition(pos.toString());
        game.setActiveColour(pos.getActiveColour());
        game.setStatus(GameStatus.OPEN);

        gameRepository.save(game);

        return game;
    }

    /**
     * Enter an open game. Afterwards the status of the game is RUNNING.
     *
     * @param gameId       game ID
     * @param secondPlayer second player.
     */
    public void enterGame(Long gameId, String secondPlayer) {
        Game game = this.getGameById(gameId);
        if (game.getStatus() == GameStatus.OPEN) {
            if (game.getPlayerWhite() == null) {
                game.setPlayerWhite(secondPlayer);
            } else {
                game.setPlayerBlack(secondPlayer);
            }
            game.setStatus(GameStatus.RUNNING);
            gameRepository.save(game);
            sendPositionIfNeccessary(game);
        } else {
            throw new IllegalStateException("Not possible to enter this game.");
        }
    }

    /**
     * End a given game.
     *
     * @param gameId game ID.
     * @param result game result
     */
    public void endGame(Long gameId, GameResult result) {
        Game game = this.getGameById(gameId);
        game.setStatus(GameStatus.ENDED);
        game.setResult(result);
        gameRepository.save(game);
    }

    /**
     * Create and perform a move.
     *
     * @param gameId game ID
     * @param sMove  move as String, e.g. "e2e4"
     * @return move object
     * @throws IllegalMoveException move is not compliant according to rules.
     * @throws MoveValidationNotPossibleException move validation failed.
     */
    public Move createAndPerformMove(Long gameId, String sMove) throws IllegalMoveException, MoveValidationNotPossibleException {

        Move move = new Move(sMove);
        Game game = this.getGameById(gameId);

        Position currentPos = new Position(game.getPosition());

        ValidateMoveResult result
                = rulesClient.validateMove(currentPos, move);

        if (result.isValidationFailed()) {
            String message = "Validation failed. "+result.getDescription();
            throw new IllegalMoveException(message);
        } else if (result.isValid()) {

            Position newPos = new Position(result.getResultingFen());
            game.setPosition(newPos.toString());
            game.setActiveColour(newPos.getActiveColour());

            move.setGame(game);
            List<Move> moves = moveRepository.findByGame(game);
            move.setNumber(moves.size() + 1L);

            moveRepository.save(move);
            gameRepository.save(game);

            if (result.isStalemateAfterMove()) {
                this.endGame(game.getId(), GameResult.DRAW);
            }

            if (result.isDrawnByFiftyMoveRule()) {
                this.endGame(game.getId(), GameResult.DRAW);
            }

            if (result.isCheckmateAfterMove()) {
                if (newPos.getActiveColour() == 'b') {
                    this.endGame(game.getId(), GameResult.WHITE_WINS);
                } else {
                    this.endGame(game.getId(), GameResult.BLACK_WINS);
                }
            }

            sendPositionIfNeccessary(game);
        } else {
            throw new IllegalMoveException("Move " + move.getText() + " not compliant to chess rules.");
        }

        return move;
    }

    private char otherColour(char colour) {
        if (colour == 'b') {
            return 'w';
        } else {
            return 'b';
        }
    }

    void sendPositionIfNeccessary(Game game) {
        if (game.getStatus() == GameStatus.RUNNING) {
            if (game.getActiveColour() == 'w'
                    && game.getPlayerWhite().equals("stockfish")) {
                positionSender.send(game);
            } else if (game.getActiveColour() == 'b'
                    && game.getPlayerBlack().equals("stockfish")) {
                positionSender.send(game);
            }
        }
    }
}
