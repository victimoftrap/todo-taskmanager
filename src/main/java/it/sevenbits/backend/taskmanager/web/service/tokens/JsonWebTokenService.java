package it.sevenbits.backend.taskmanager.web.service.tokens;

import io.jsonwebtoken.security.Keys;
import it.sevenbits.backend.taskmanager.core.model.User;
import it.sevenbits.backend.taskmanager.config.settings.JwtSettings;
import it.sevenbits.backend.taskmanager.web.security.model.AuthenticatedJwtToken;

import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.sql.Date;
import java.time.Instant;
import java.time.Duration;
import java.util.stream.Collectors;

/**
 * Service that encodes and decodes token
 */
@Service
public class JsonWebTokenService implements JwtTokenService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JwtSettings settings;
    private static final String AUTHORITIES = "authorities";

    /**
     * Create token service
     *
     * @param settings defult settings for token
     */
    public JsonWebTokenService(final JwtSettings settings) {
        this.settings = settings;
    }

    @Override
    public String createToken(final User user) {
        logger.debug("Generating token for {}", user.getUsername());
        Instant now = Instant.now();

        // generating body of a token
        Claims claims = Jwts
                .claims()
                .setIssuer(settings.getTokenIssuer())
                .setIssuedAt(Date.from(now))
                .setSubject(user.getUsername())
                .setExpiration(Date.from(now.plus(settings.getTokenExpiredIn())));
        claims.put(AUTHORITIES, user.getAuthorities()); // add authorities of current user

        return Jwts.builder()
                .setClaims(claims)
                .signWith(Keys.hmacShaKeyFor(settings.getTokenSigningKey().getBytes()))
                .compact();
    }

    @Override
    public Duration getTokenExpiredIn() {
        return settings.getTokenExpiredIn();
    }

    @Override
    public Authentication parseToken(final String token) {
        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(settings.getTokenSigningKey().getBytes()))
                .parseClaimsJws(token);

        String subject = claims.getBody().getSubject();
        List<String> tokenAuthorities = claims.getBody().get(AUTHORITIES, List.class);

        List<GrantedAuthority> authorities = tokenAuthorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new AuthenticatedJwtToken(authorities, subject);
    }
}
