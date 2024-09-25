package com.szelestamas.bookstorebddtest.core.authorization;

import com.szelestamas.bookstorebddtest.core.persona.Persona;
import com.szelestamas.bookstorebddtest.core.persona.PersonaProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class AuthorizationProvider {
    private final PersonaProperty personaProperty;

    @Value("${authorization.method}")
    private String authorizationMethod;

    public String authorizationForPersona(String personaName) {
        Persona persona = personaProperty.getPersona(personaName);

        if ("BASIC".equalsIgnoreCase(authorizationMethod))
            return basicAuthorization(persona);
        else
            return null;
    }

    private String basicAuthorization(Persona persona) {
        String credentials = persona.getEmail() + ":" + persona.getPassword();
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}
