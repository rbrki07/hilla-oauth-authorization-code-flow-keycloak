package com.example.application.services;

import dev.hilla.BrowserCallable;
import jakarta.annotation.security.RolesAllowed;

@BrowserCallable
public class HelloWorldService {

    @RolesAllowed({"ROLE_IC", "ROLE_MANAGER"})
    public String sayHello(String name) {
        if (name.isEmpty()) {
            return "Hello stranger";
        } else {
            return "Hello " + name;
        }
    }
}
