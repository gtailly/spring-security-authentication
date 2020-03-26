package fr.gtailly.authentification.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class JwtRequest {
    private String username;
    private String password;
}
