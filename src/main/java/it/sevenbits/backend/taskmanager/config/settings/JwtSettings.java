package it.sevenbits.backend.taskmanager.config.settings;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.time.Duration;

/**
 * Class with settings for creating jwt tokens
 */
@Component
public class JwtSettings {
    private String tokenIssuer;
    private String tokenSigningKey;
    private int aTokenDuration;

    /**
     * Read settings from configuration
     *
     * @param tokenIssuer     server, that gives us a token
     * @param tokenSigningKey signing key
     * @param aTokenDuration  token time-to-live
     */
    public JwtSettings(@Value("${jwt.issuer}") final String tokenIssuer,
                       @Value("${jwt.signingKey}") final String tokenSigningKey,
                       @Value("${jwt.aTokenDuration}") final int aTokenDuration) {
        this.tokenIssuer = tokenIssuer;
        this.tokenSigningKey = tokenSigningKey;
        this.aTokenDuration = aTokenDuration;
    }

    /**
     * Get token issuer
     *
     * @return issuer
     */
    public String getTokenIssuer() {
        return tokenIssuer;
    }

    /**
     * Get token signing key
     *
     * @return signing key
     */
    public String getTokenSigningKey() {
        return tokenSigningKey;
    }

    /**
     * Get token life duration
     *
     * @return token duration
     */
    public Duration getTokenExpiredIn() {
        return Duration.ofMinutes(aTokenDuration);
    }
}
