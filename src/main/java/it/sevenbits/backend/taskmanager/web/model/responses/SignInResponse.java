package it.sevenbits.backend.taskmanager.web.model.responses;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Response after signing-in
 */
public class SignInResponse {
    @NotBlank
    private final String token;

    /**
     * Create response
     *
     * @param token JWT user token
     */
    @JsonCreator
    public SignInResponse(@JsonProperty("token") final String token) {
        this.token = token;
    }

    /**
     * Get JWT token for user
     *
     * @return token
     */
    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignInResponse that = (SignInResponse) o;
        return Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token);
    }
}
