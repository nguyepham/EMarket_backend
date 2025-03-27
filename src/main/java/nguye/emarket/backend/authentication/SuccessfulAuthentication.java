package nguye.emarket.backend.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class SuccessfulAuthentication implements Authentication {

    private final SecurityUser userDetails;
    private final String jwtToken;
    private boolean authenticated = true;

    public SuccessfulAuthentication(SecurityUser userDetails, String jwtToken) {
        this.userDetails = userDetails;
        this.jwtToken = jwtToken;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return userDetails.getId();
    }

    @Override
    public Object getDetails() {
        return jwtToken;
    }

    @Override
    public Object getPrincipal() {
        return userDetails;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
