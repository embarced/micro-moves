package org.flexess.games.service;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for Posistion class.
 */
public class PositionTest {

    /**
     * Creation with default constructor.
     * Result is initial posistion, white to move.
     */
    @Test
    public void emptyConstructor() {

        Position pos = new Position();
        String fen = pos.toString();
        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen);

        Assert.assertEquals('w', pos.getActiveColour());
        Assert.assertEquals("KQkq", pos.getCastlingAvailability());
        Assert.assertEquals("-", pos.getEnPassantTargetSquare());
        Assert.assertEquals(0, pos.getHalfmoveClock());
        Assert.assertEquals(1, pos.getFullmoveNumber());

    }

    /**
     * Creation with a FEN String.
     */
    @Test
    public void fenConstructor() {

        String fen = "r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1";

        Position pos = new Position(fen);
        String newfen = pos.toString();
        Assert.assertEquals(fen, newfen);

        Assert.assertEquals('b', pos.getActiveColour());
        Assert.assertEquals("KQkq", pos.getCastlingAvailability());
        Assert.assertEquals("-", pos.getEnPassantTargetSquare());
        Assert.assertEquals(0, pos.getHalfmoveClock());
        Assert.assertEquals(1, pos.getFullmoveNumber());
    }

    @Test
    public void getPieceBySquareName() {
        Position pos = new Position();

        Assert.assertEquals('P', pos.getPiece("e2"));
        Assert.assertEquals('r', pos.getPiece("a8"));
        Assert.assertEquals(' ', pos.getPiece("f5"));
    }

    @Test
    public void toNumber() {

        Position pos = new Position();

        Assert.assertEquals(0, pos.squareNametoNumber("a8"));
        Assert.assertEquals(7, pos.squareNametoNumber("h8"));
        Assert.assertEquals(63, pos.squareNametoNumber("h1"));
    }
}
