package org.flexess.games.domain;

import org.junit.Assert;
import org.junit.Test;

public class GameTest {

    @Test
    public void activePlayer() {

        Game game = new Game();
        game.setPlayerWhite("peter");
        game.setPlayerBlack("mary");

        game.setActiveColour('w');
        Assert.assertEquals("peter", game.getActivePlayer());

        game.setActiveColour('b');
        Assert.assertEquals("mary", game.getActivePlayer());
    }

    @Test
    public void takingPart() {

        Game game = new Game();
        game.setPlayerWhite("paul");
        game.setPlayerBlack("ringo");

        Assert.assertTrue(game.isTakingPart("paul"));
        Assert.assertTrue(game.isTakingPart("ringo"));

        Assert.assertFalse(game.isTakingPart("peter"));
        Assert.assertFalse(game.isTakingPart(null));

    }

}
