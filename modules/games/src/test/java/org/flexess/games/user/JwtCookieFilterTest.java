package org.flexess.games.user;

import org.junit.Assert;
import org.junit.Test;

public class JwtCookieFilterTest {

    @Test
    public void getUserFromToken() throws Exception {

        JwtCookieFilter filter = new JwtCookieFilter();

        // Data created with https://jwt.io
        // https://jwt.io/#debugger-io?token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwcGFudGhlciIsIm5hbWUiOiJQYXVsIFBhbnRoZXIifQ.HTXKaso8TukF_eSW7zQfzu8jlKBBz3QGRI7QCEeR_jE
        //
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJwcGFudGhlciIsIm5hbWUiOiJQYXVsIFBhbnRoZXIifQ.HTXKaso8TukF_eSW7zQfzu8jlKBBz3QGRI7QCEeR_jE";
        String secret = "secret123";

        User user = filter.getUserFromToken(token, secret);
        Assert.assertEquals("Paul Panther", user.getName());
        Assert.assertEquals("ppanther", user.getUserid());
    }
}
