package nguye.emarket.backend.authentication;

import nguye.emarket.backend.exception.BadCredentialsException;
import nguye.emarket.backend.util.SecurityUtil;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsServiceImpl userDetailsService;

    public CustomAuthenticationProvider(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(username);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if (passwordEncoder.matches(password, userDetails.getPassword())) {
            String jwt = SecurityUtil.generateToken(userDetails);

            SuccessfulAuthentication successfulAuthentication = new SuccessfulAuthentication(userDetails, jwt);
            SecurityContextHolder.getContext().setAuthentication(successfulAuthentication);
            return successfulAuthentication;
        }

        throw new BadCredentialsException();
    }

    @Override
    public boolean supports(Class<?> authenticationType) {
        return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
    }
}
