package com.duoc.seguridadcalidad;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // VULNERABILIDAD 1: Hardcoded credentials (ejemplo)
        String adminUser = "admin";
        String adminPass = "admin123";

        // VULNERABILIDAD 2: Redirección abierta
        String redirect = System.getProperty("redirectUrl");
        if (redirect != null) {
            return "redirect:" + redirect;
        }

        return "login";
    }
}