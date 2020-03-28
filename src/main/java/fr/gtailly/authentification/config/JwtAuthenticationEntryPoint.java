package fr.gtailly.authentification.config;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JwtAuthenticationEntryPoint
 *
 * @author Grégory TAILLY
 */
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * It provides an Unauthorized exception when you doesn't have a correct token
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     * @param authException {@link AuthenticationException}
     * @throws IOException
     */
    @Override
    public void commence(final HttpServletRequest request, final HttpServletResponse response,
                         final AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
    }
}
