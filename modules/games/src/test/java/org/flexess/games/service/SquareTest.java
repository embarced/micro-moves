package org.flexess.games.service;

import org.junit.Assert;
import org.junit.Test;

public class SquareTest {

    @Test
    public void toNumber() {
        Assert.assertEquals(0, Square.toNumber("a8"));
        Assert.assertEquals(7, Square.toNumber("h8"));
        Assert.assertEquals(63, Square.toNumber("h1"));
    }

    @Test
    public void toName() {
    }
}