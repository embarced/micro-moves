package org.flexess.games.user;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Component
public class JwtCookieFilter implements Filter {

    private static final Logger LOG = LoggerFactory.getLogger(JwtCookieFilter.class);

    @Value("${JWT_COOKIE_NAME}")
    private String jwtCookieName;

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOG.info("init JwtCookieFilter, JWT_COOKIE_NAME=" + jwtCookieName);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (jwtCookieName.equals(cookie.getName())) {

                    String token = cookie.getValue();
                    LOG.debug("JWT Cookie found: [" + token + "]");

                    Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
                    JWTVerifier verifier = JWT.require(algorithm).build();
                    DecodedJWT jwt = verifier.verify(token);

                    Map<String, Claim> claims = jwt.getClaims();
                    Claim name = claims.get("name");
                    Claim sub = claims.get("sub");

                    User user = new User();
                    user.setName(name.asString());
                    user.setUserid(sub.asString());

                    request.setAttribute("user", user);
                    LOG.debug("user: " + user);
                }
            }
        }
        chain.doFilter(request, res);
    }

    @Override
    public void destroy() {
    }
}