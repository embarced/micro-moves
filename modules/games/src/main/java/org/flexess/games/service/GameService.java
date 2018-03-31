package org.flexess.games.service;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.GameRepository;
import org.flexess.games.domain.GameStatus;
import org.flexess.games.domain.Move;
import org.flexess.games.domain.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private MoveRepository moveRepository;

    public Game getGameById(Long id) {
        return gameRepository.findById(id).get();
    }

    public Iterable<Game> getAllGames() {
        return gameRepository.findAll();
    }

    public List<Move> getMovesByGameId(Long id) {
        Game game = this.getGameById(id);
        return moveRepository.findByGame(game);
    }

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
        game.setFullMoveNumber(pos.getFullmoveNumber());

        game.setStatus(GameStatus.OPEN);

        gameRepository.save(game);

        return game;
    }

    public void endGame(Long gameId) {
        Game game = this.getGameById(gameId);
        game.setStatus(GameStatus.ENDED);
        gameRepository.save(game);
    }

    public Move createAndPerformMove(Long gameId, String sMove) {

        Move move = new Move(sMove);
        Game game = this.getGameById(gameId);

        performMove(game, move);

        move.setGame(game);
        List<Move> moves = moveRepository.findByGame(game);
        move.setNumber(moves.size() + 1L);

        moveRepository.save(move);
        gameRepository.save(game);

        return move;
    }

    void performMove(Game game, Move move) {

        Position pos = new Position(game.getPosition());
        String from = move.getText().substring(0,2);
        String to = move.getText().substring(2,4);

        char piece = pos.getPiece(from);

        if (piece == ' ') {
            throw new IllegalArgumentException("Square "+from+" is empty.");
        }

        if (Character.isUpperCase(piece) && game.getActiveColour() == 'b') {
            throw new IllegalArgumentException("Piece at "+from+" has wrong colour.");
        }

        if (Character.isLowerCase(piece) && game.getActiveColour() == 'w') {
            throw new IllegalArgumentException("Piece at "+from+" has wrong colour.");
        }

        pos.setPiece(from, ' ');
        pos.setPiece(to, piece);

        if (pos.getActiveColour() == 'b') {
            pos.setFullmoveNumber(pos.getFullmoveNumber() + 1);
        }

        pos.setActiveColour(otherColour(pos.getActiveColour()));

        game.setPosition(pos.toString());
        game.setFullMoveNumber(pos.getFullmoveNumber());
        game.setActiveColour(pos.getActiveColour());
    }

    char otherColour(char colour) {
        if (colour == 'b') {
            return 'w';
        } else {
            return 'b';
        }
    }
}
