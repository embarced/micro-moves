package org.flexess.games.service;


import org.flexess.games.service.Position;
import org.junit.Assert;
import org.junit.Test;

public class PositionTest {

    @Test
    public void emptyConstructor() {
        Position pos = new Position();

        Assert.assertEquals('w', pos.getActiveColour());
        Assert.assertEquals("KQkq", pos.getCastlingAvailability());
        Assert.assertEquals("-", pos.getEnPassantTargetSquare());
        Assert.assertEquals(0, pos.getHalfmoveClock());
        Assert.assertEquals(1, pos.getFullmoveNumber());

        String fen = pos.toString();

        Assert.assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", fen);
    }

    @Test
    public void fenConstructor() {

        String fen = "r1bqkb1r/pppp1Qpp/2n2n2/4p3/2B1P3/8/PPPP1PPP/RNB1K1NR b KQkq - 0 1";

        Position pos = new Position(fen);

        Assert.assertEquals('b', pos.getActiveColour());
        Assert.assertEquals("KQkq", pos.getCastlingAvailability());
        Assert.assertEquals("-", pos.getEnPassantTargetSquare());
        Assert.assertEquals(0, pos.getHalfmoveClock());
        Assert.assertEquals(1, pos.getFullmoveNumber());

        String newfen = pos.toString();

        Assert.assertEquals(fen, newfen);
    }
    @Test
    public void getPieceBySquareName() {
        Position pos = new Position();

        Assert.assertEquals('P', pos.getPiece("e2"));
        Assert.assertEquals('r', pos.getPiece("a8"));
        Assert.assertEquals(' ', pos.getPiece("f5"));
    }
}
