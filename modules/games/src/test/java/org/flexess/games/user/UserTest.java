package org.flexess.games.user;

import org.junit.Assert;
import org.junit.Test;

public class UserTest {

    @Test
    public void hasRole() throws Exception {
        User user = new User();
        user.setUserid("ppanther");
        user.setName("Paul Panther");
        user.setRoles("user,admin");

        Assert.assertTrue(user.hasRole("user"));
        Assert.assertTrue(user.hasRole("admin"));
        Assert.assertFalse(user.hasRole("engine"));
    }
}
