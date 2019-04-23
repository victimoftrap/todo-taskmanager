package it.sevenbits.backend.taskmanager.web.model.requests;

import java.util.List;
import java.util.Objects;

/**
 * Request for updating user account
 */
public class UpdateUserRequest {
    private final Boolean enabled;
    private final List<String> authorities;

    /**
     * Create request
     *
     * @param enabled     user account activity
     * @param authorities list wit new authorities
     */
    public UpdateUserRequest(final Boolean enabled, final List<String> authorities) {
        this.enabled = enabled;
        this.authorities = authorities;
    }

    /**
     * Get account status
     *
     * @return status
     */
    public Boolean isEnabled() {
        return enabled;
    }

    /**
     * Get user authorities
     *
     * @return authorities list
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
        UpdateUserRequest request = (UpdateUserRequest) o;
        return Objects.equals(enabled, request.enabled) &&
                Objects.equals(authorities, request.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, authorities);
    }
}
