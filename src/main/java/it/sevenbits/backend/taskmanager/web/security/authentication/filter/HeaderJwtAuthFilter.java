package it.sevenbits.backend.taskmanager.web.security.authentication.filter;

import it.sevenbits.backend.taskmanager.web.security.authentication.JwtAuthenticationException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;

/**
 * Class that extracts token from request header
 */
public class HeaderJwtAuthFilter extends JwtAuthFilter {
    private static final Pattern BEARER_AUTH_PATTERN = Pattern.compile("^Bearer\\s+(.*)$");
    private static final int TOKEN_GROUP = 1;

    /**
     * Create JWT authentication filter
     *
     * @param matcher some matcher
     */
    public HeaderJwtAuthFilter(RequestMatcher matcher) {
        super(matcher);
    }

    /**
     * Get token from header
     *
     * @param request request with token in header
     * @return encoded token from header
     * @throws AuthenticationException if authorization header are invalid
     */
    @Override
    protected String takeToken(final HttpServletRequest request) throws AuthenticationException {
        String authHeader = request.getHeader("Authorization");
        Matcher m = BEARER_AUTH_PATTERN.matcher(authHeader);
        if (m.matches()) {
            return m.group(TOKEN_GROUP);
        } else {
            throw new JwtAuthenticationException("Invalid Authorization header: " + authHeader);
        }
    }
}
