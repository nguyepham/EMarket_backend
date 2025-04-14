package nguye.emarket.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nguye.emarket.backend.authentication.JwtHelper;
import nguye.emarket.backend.authentication.SecurityUser;
import nguye.emarket.backend.authentication.SuccessfulAuthentication;
import nguye.emarket.backend.authentication.UserDetailsServiceImpl;
import nguye.emarket.backend.exception.InvalidJwtException;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtHelper jwtHelper, UserDetailsServiceImpl userDetailsService) {
        this.jwtHelper = jwtHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String username = jwtHelper.extractUsername(jwt);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                SecurityUser userDetails = (SecurityUser) userDetailsService.loadUserByUsername(username);

                if (jwtHelper.isTokenValid(userDetails, jwt)) {
                    SuccessfulAuthentication authentication = new SuccessfulAuthentication(userDetails, jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            filterChain.doFilter(request, response);
        }
        catch (InvalidJwtException ex) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(String.format("{ \"statusText\": \"%s\" }", ex.getMessage()));
        }
    }
}
