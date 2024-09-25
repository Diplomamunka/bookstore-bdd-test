package com.szelestamas.bookstorebddtest.core.persona;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Persona {
    private String email;
    private String name;
    private String type;
    private String description;
    private String password;
    private boolean login;
    private boolean canLogin = true;
}
