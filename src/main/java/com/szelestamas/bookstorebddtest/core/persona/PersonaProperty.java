package com.szelestamas.bookstorebddtest.core.persona;

import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Configuration
public class PersonaProperty {
    private Map<String, Persona> personae = new HashMap<>();

    public Persona getPersona(String personaName) {
        Persona persona = personae.get(personaName);
        if (persona == null)
            throw new PersonaNotFoundException("Persona " + personaName + " not found");
        return persona;
    }
}
