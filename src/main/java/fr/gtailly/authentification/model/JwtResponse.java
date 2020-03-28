package fr.gtailly.authentification.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * JwtResponse
 *
 * @author Grégory TAILLY
 */
@Data
@RequiredArgsConstructor
public class JwtResponse {
    private final String jwtToken;
}
