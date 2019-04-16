package it.sevenbits.backend.taskmanager.web.model.requests;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

/**
 * Request for updating user account
 */
public class UpdateUserRequest {
    @NotNull
    private final boolean enabled;
    private final List<String> authorities;

    /**
     * Create request
     *
     * @param enabled     user account activity
     * @param authorities list wit new authorities
     */
    public UpdateUserRequest(final boolean enabled, final List<String> authorities) {
        this.enabled = enabled;
        this.authorities = authorities;
    }

    /**
     * Get account status
     *
     * @return status
     */
    public boolean isEnabled() {
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
        UpdateUserRequest that = (UpdateUserRequest) o;
        return enabled == that.enabled &&
                Objects.equals(authorities, that.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, authorities);
    }
}
