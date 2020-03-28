package fr.gtailly.authentification.controller;

import fr.gtailly.authentification.config.JwtTokenUtil;
import fr.gtailly.authentification.model.JwtRequest;
import fr.gtailly.authentification.model.JwtResponse;
import fr.gtailly.authentification.service.JwtUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * JwtAuthenticationController
 *
 * @author Grégory TAILLY
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final JwtUserDetailsService userDetailsService;

    /**
     * It allows you to authenticate an user for this API
     * It provides a valid token
     * @param jwtRequest {@link JwtRequest}
     * @return {@link JwtResponse}
     * @throws Exception
     */
    @PostMapping(value = "/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody final JwtRequest jwtRequest) throws Exception {
        this.authenticate(jwtRequest.getUsername(), jwtRequest.getPassword());
        final UserDetails userDetails = this.userDetailsService
                                            .loadUserByUsername(jwtRequest.getUsername());
        final String token = this.jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    private void authenticate(final String username, final String password) throws Exception {
        try {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
