package org.gestionpastelera.ms_auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestionpastelera.ms_auth.dto.request.LoginRequest;
import org.gestionpastelera.ms_auth.dto.request.RegistroRequest;
import org.gestionpastelera.ms_auth.dto.response.AuthResponse;
import org.gestionpastelera.ms_auth.dto.response.UsuarioPerfilResponse;
import org.gestionpastelera.ms_auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

        @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

        @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegistroRequest request) {
        AuthResponse response = authService.registrarCliente(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/register-admin")
    public ResponseEntity<AuthResponse> registerAdmin(@Valid @RequestBody RegistroRequest request) {
        AuthResponse response = authService.registrarAdmin(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

        @GetMapping("/me")
    public ResponseEntity<UsuarioPerfilResponse> getUsuarioAutenticado(
            org.springframework.security.core.Authentication authentication) {
        UsuarioPerfilResponse perfil = authService.obtenerPerfilActual(authentication.getName());
        return ResponseEntity.ok(perfil);
    }

        @GetMapping("/validate")
    public ResponseEntity<org.gestionpastelera.ms_auth.dto.response.ValidarTokenResponse> validarToken(
            @RequestHeader(value = "Authorization", required = false) String tokenHeader) {
        org.gestionpastelera.ms_auth.dto.response.ValidarTokenResponse response = authService.validarToken(tokenHeader);
        if (!response.isValid()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
        return ResponseEntity.ok(response);
    }
}
