package org.flexess.games.service;

import org.flexess.games.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
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

    public Game createGame(String playerWhite, String playerBlack) {

        Position pos = new Position();

        Game game = new Game();
        game.setPlayerWhite(playerWhite);
        game.setPlayerBlack(playerBlack);
        game.setFen(pos.toString());
        game.setActiveColour(pos.getActiveColour());
        game.setFullMoveNumber(pos.getFullmoveNumber());

        game.setCreated(new Date());
        game.setModified(new Date());

        game.setGameState(GameState.OPEN);

        gameRepository.save(game);

        return game;
    }

    public void endGame(Long gameId) {
        Game game = this.getGameById(gameId);
        game.setGameState(GameState.ENDED);
        game.setModified(new Date());
        gameRepository.save(game);
    }

    public Move createAndPerformMove(Long gameId, String sMove) {

        Move move = new Move(sMove);
        Game game = this.getGameById(gameId);

        performMove(game, move);

        move.setGame(game);
        Date now = new Date();
        move.setCreated(now);
        game.setModified(now);
        List<Move> moves = moveRepository.findByGame(game);
        move.setNumber(moves.size() + 1L);

        moveRepository.save(move);
        gameRepository.save(game);

        return move;
    }

    void performMove(Game game, Move move) {

        Position pos = new Position(game.getFen());
        char piece = pos.getPiece(move.getFromField());

        if (piece == ' ') {
            throw new IllegalArgumentException("Square "+move.getFromField()+" is empty.");
        }

        if (Character.isUpperCase(piece) && game.getActiveColour() == 'b') {
            throw new IllegalArgumentException("Piece at "+move.getFromField()+" has wrong colour.");
        }

        if (Character.isLowerCase(piece) && game.getActiveColour() == 'w') {
            throw new IllegalArgumentException("Piece at "+move.getFromField()+" has wrong colour.");
        }

        pos.setPiece(move.getFromField(), ' ');
        pos.setPiece(move.getToField(), piece);

        if (pos.getActiveColour() == 'b') {
            pos.setFullmoveNumber(pos.getFullmoveNumber() + 1);
        }

        pos.setActiveColour(otherColour(pos.getActiveColour()));

        game.setFen(pos.toString());
        game.setFullMoveNumber(pos.getFullmoveNumber());
        game.setActiveColour(pos.getActiveColour());
    }

    char otherColour(char colour) {
        if (colour == 'b') return 'w';
        else return 'b';
    }
}
