package it.sevenbits.backend.taskmanager.web.security.filter;

import it.sevenbits.backend.taskmanager.web.security.model.NonAuthenticatedJwtToken;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.util.Collections;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Class for work with work with token in "Authorization" header
 */
public abstract class JwtAuthFilter extends AbstractAuthenticationProcessingFilter {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Create JWT authentication filter
     *
     * @param matcher some matcher
     */
    public JwtAuthFilter(final RequestMatcher matcher) {
        super(matcher);
    }

    /**
     * Get token from request
     *
     * @param request  web request with token or maybe don't
     * @param response web response
     * @return non-authenticated token if it exists in request or anonymous token
     * @throws AuthenticationException if request without token
     */
    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response) throws AuthenticationException {
        String token;
        try {
            token = takeToken(request);
        } catch (Exception e) {
            logger.warn("Failed to get token: {}", e.getMessage());
            return anonymousToken();
        }
        return new NonAuthenticatedJwtToken(token);
    }

    /**
     * Get token from header
     *
     * @param request request where we would search "Authorization" header
     * @return encoded token from header or null
     * @throws AuthenticationException if authorization header are invalid
     */
    protected abstract String takeToken(HttpServletRequest request) throws AuthenticationException;

    private Authentication anonymousToken() {
        return new AnonymousAuthenticationToken(
                "ANONYMOUS", "ANONYMOUS",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANONYMOUS"))
        );
    }

    /**
     * Save authorization
     *
     * @param request    web request
     * @param response   web response
     * @param chain      chain of a filtered requests
     * @param authResult result of authentication
     */
    @Override
    protected void successfulAuthentication(
            final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain chain, final Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        chain.doFilter(request, response);
    }
}
