package fr.gtailly.authentification.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * JwtTokenUtil
 * It allows you to create/valid/decrypt token
 *
 * @author Grégory TAILLY
 */
@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    /**
     * Retrieve subject from token
     * @param token {@link String}
     * @return {@link String}
     */
    public String getUsernameFromToken(final String token) {
        return this.getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Retrieve expiration date from token
     * @param token {@link String}
     * @return {@link Date}
     */
    public Date getExpirationDateFromToken(final String token) {
        return this.getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Retrieve Claims
     * @param token {@link String}
     * @param claimsResolver {@link Function}
     * @param <T>
     * @return {@link T}
     */
    public <T> T getClaimFromToken(final String token, final Function<Claims, T> claimsResolver) {
        final Claims claims = this.getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generate a token from user data
     * @param userDetails {@link UserDetails}
     * @return {@link String}
     */
    public String generateToken(final UserDetails userDetails) {
        final Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, userDetails.getUsername());
    }

    /**
     * Check if token are valid
     * @param token {@link String}
     * @param userDetails {@link UserDetails}
     * @return {@link Boolean}
     */
    public Boolean validateToken(final String token, final UserDetails userDetails) {
        final String username = this.getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * While generating the token, define claims of the token like subject, issue data, expiration date and the sign ID
     * @param claims {@link Map}
     * @param subject {@link String}
     * @return {@link String}
     */
    private String doGenerateToken(final Map<String, Object> claims, final String subject) {
        return Jwts.builder()
                   .setClaims(claims)
                   .setSubject(subject)
                   .setIssuedAt(new Date(System.currentTimeMillis()))
                   .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                   .signWith(SignatureAlgorithm.HS512, secret)
                   .compact();
    }

    private Claims getAllClaimsFromToken(final String token) {
        return Jwts.parser()
                   .setSigningKey(secret)
                   .parseClaimsJws(token)
                   .getBody();
    }

    private Boolean isTokenExpired(final String token) {
        final Date expiration = this.getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }
}
