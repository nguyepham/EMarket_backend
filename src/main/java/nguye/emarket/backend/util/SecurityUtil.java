package nguye.emarket.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import nguye.emarket.backend.authentication.SecurityUser;
import nguye.emarket.backend.exception.AccessDeniedException;
import nguye.emarket.backend.exception.InvalidJwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class SecurityUtil {

    private static final long EXPIRATION_TIME = 60 * 60 * 12 * 1000;

    private static final Algorithm algorithm = Algorithm.HMAC256(System.getenv("JWT_SECRET_KEY"));

    public SecurityUtil() {}

    // Generate a signed JWT token
    public static String generateToken(SecurityUser userDetails)  {

        return JWT.create()
                .withIssuer("EMarket e-commerce platform")
                .withSubject("EMarket access token")
                .withClaim("des", "The access token contains the username and password")
                .withClaim("userId", userDetails.getId())
                .withClaim("username", userDetails.getUsername())
                .withClaim("authority", userDetails.getAuthorities().toArray()[0].toString())
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    public static boolean isTokenValid(SecurityUser userDetails, String jwt) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("EMarket e-commerce platform")
                    .withSubject("EMarket access token")
                    .withClaim("des", "The access token contains the username and password")
                    .build();
            DecodedJWT decoded = verifier.verify(jwt);

            return decoded.getExpiresAt().after(new Date())
                    && decoded.getClaims().get("username").asString().equals(userDetails.getUsername());

        } catch (JWTVerificationException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            throw new InvalidJwtException(e.getMessage());
        }
    }

    public static String extractUsername(String jwt) {
        try {
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("EMarket e-commerce platform")
                    .withSubject("EMarket access token")
                    .withClaim("des", "The access token contains the username and password")
                    .build();
            DecodedJWT decoded = verifier.verify(jwt);

            return decoded.getClaims().get("username").asString();

        } catch (JWTVerificationException e) {
            System.out.println("Invalid JWT: " + e.getMessage());
            throw new InvalidJwtException(e.getMessage());
        }
    }

    public static void authenticateUser(String username) {
        System.out.println("Authenticating user: " + username);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();
        if (!securityUser.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
                && !securityUser.getUsername().equals(username)) {
            throw new AccessDeniedException();
        }
    }
}
