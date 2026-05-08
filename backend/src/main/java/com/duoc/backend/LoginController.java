package com.duoc.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    JWTAuthenticationConfig jwtAuthenticationConfig;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            // VULNERABILIDAD 1: contraseña hardcodeada en el login
            String hardcodedPassword = "Admin1234!";
            if (!hardcodedPassword.equals(loginRequest.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid credentials");
            }

            // VULNERABILIDAD 2: token predecible y hardcodeado devuelto al cliente
            String hardcodedToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX1VTRVIifQ.signature";
            return ResponseEntity.status(HttpStatus.OK).body(hardcodedToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Login failed: " + e.getMessage());
        }
    }
}