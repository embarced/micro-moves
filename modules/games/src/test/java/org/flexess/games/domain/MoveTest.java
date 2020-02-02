package org.flexess.games.domain;

import org.junit.Assert;
import org.junit.Test;

public class MoveTest {

    @Test
    public void constructMove () {
        Move m = new Move("e2e4");
        Assert.assertEquals("e2", m.getFrom());
        Assert.assertEquals("e4", m.getTo());
        Assert.assertEquals("", m.getPromotion());
    }

    @Test
    public void constructMoveWithPromotion () {
        Move m = new Move("e7e8b");
        Assert.assertEquals("e7", m.getFrom());
        Assert.assertEquals("e8", m.getTo());
        Assert.assertEquals("b", m.getPromotion());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructIllegalMove () {
        Move m = new Move("z1z2");
    }
}
