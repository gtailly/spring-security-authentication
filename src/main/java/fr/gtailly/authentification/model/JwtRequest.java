package fr.gtailly.authentification.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * JwtRequest
 *
 * @author Grégory TAILLY
 */
@Data
@RequiredArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
