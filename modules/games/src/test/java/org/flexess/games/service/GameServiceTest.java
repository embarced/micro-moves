package org.flexess.games.service;

import org.flexess.games.domain.Game;
import org.flexess.games.domain.Move;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests for GameService class.
 */
public class GameServiceTest {

    /**
     * Simple move from initial position, white moves e2-e4.
     */
    @Test
    public void simplePerformMove() {
        GameService service = new GameService();

        Position pos = new Position();
        Game game = new Game();
        game.setPosition(pos.toString());
        game.setActiveColour('w');

        Move move = new Move("e2e4");
        service.performMove(game, move);

        Position newPos = new Position(game.getPosition());
        Assert.assertEquals(' ', newPos.getPiece("e2"));
        Assert.assertEquals('P', newPos.getPiece("e4")); // white pawn
    }
}
