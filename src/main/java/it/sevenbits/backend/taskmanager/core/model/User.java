package it.sevenbits.backend.taskmanager.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.sevenbits.backend.taskmanager.core.service.validation.annotations.id.ValidID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

/**
 * Class for user data
 */
public class User {
    @ValidID
    @JsonProperty("id")
    private final String id;

    @NotBlank
    @JsonProperty("username")
    private final String username;

    @NotBlank
    @JsonIgnore
    private final String password;

    @NotNull
    @JsonProperty("authorities")
    private final List<String> authorities;

    /**
     * Create user
     *
     * @param id          ID of the user
     * @param username    name of the user
     * @param password    password of the user
     * @param authorities user's authorities
     */
    @JsonCreator
    public User(final String id,
                final String username,
                final String password,
                final List<String> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Get ID of the user
     *
     * @return user ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get name of the user
     *
     * @return user name
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

    /**
     * Get user's authorities
     *
     * @return authorities
     */
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(username, user.username) &&
                Objects.equals(password, user.password) &&
                Objects.equals(authorities, user.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, authorities);
    }
}
