package fr.gtailly.authentification.config;

import fr.gtailly.authentification.service.JwtUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtRequestFilter are triggered on each HTTP Call
 * It allows you to check token validity and authorize or deny HTTP Call
 *
 * @author Grégory TAILLY
 */

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Main filter which is applied for each secure endpoint
     * In our case, we don't check authenticate endpoint
     * In first we take "Authorization" parameter in header
     * If the fully token is not null and begin with "Bearer" then we only get the rest of token
     * Next, we get the username with the token
     * With the username we get the ServiceDetails
     * In first, we check if token are valid and corresponding to ServiceDetails
     * Finally we set an UsernamePasswordAuthenticationToken is Spring Security Context
     *
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param chain {@link FilterChain}
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain chain)
        throws ServletException, IOException {

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                username = this.jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }

        if (username != null && SecurityContextHolder.getContext()
                                                     .getAuthentication() == null) {

            final UserDetails userDetails = this.jwtUserDetailsService.loadUserByUsername(username);

            if (this.jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                final UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext()
                                     .setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }

}
