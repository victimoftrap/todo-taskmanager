package it.sevenbits.backend.taskmanager.web.model.requests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Class for transfer data for sign-in user
 */
public class SignInRequest {
    @NotBlank
    private final String username;
    @NotBlank
    private final String password;

    /**
     * Create request
     *
     * @param username the name of the user
     * @param password the password of the user
     */
    @JsonCreator
    public SignInRequest(@JsonProperty("username") final String username,
                         @JsonProperty("password") final String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Get name of the user
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Get password of the user
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SignInRequest that = (SignInRequest) o;
        return Objects.equals(username, that.username) &&
                Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password);
    }
}
