package org.gestionpastelera.ms_auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import org.gestionpastelera.ms_auth.dto.request.LoginRequest;
import org.gestionpastelera.ms_auth.dto.request.RegistroRequest;
import org.gestionpastelera.ms_auth.dto.response.AuthResponse;
import org.gestionpastelera.ms_auth.dto.response.UsuarioPerfilResponse;
import org.gestionpastelera.ms_auth.dto.response.ValidarTokenResponse;
import org.gestionpastelera.ms_auth.enums.Rol;
import org.gestionpastelera.ms_auth.mapper.UsuarioMapper;
import org.gestionpastelera.ms_auth.model.Usuario;
import org.gestionpastelera.ms_auth.repository.UsuarioRepository;
import org.gestionpastelera.ms_auth.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UsuarioMapper usuarioMapper;

    /**
     * Inicio de sesión con correo y contraseña
     */
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        // 1. Buscar usuario por email
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (!Boolean.TRUE.equals(usuario.getActive())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "La cuenta se encuentra inactiva");
        }

        // 2. Validar credenciales
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 3. Generar token
        String token = jwtUtil.generarToken(usuario);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .id(usuario.getId())
                .nombres(usuario.getNombres())
                .apellidos(usuario.getApellidos())
                .email(usuario.getEmail())
                .rol(usuario.getRol() != null ? usuario.getRol().name() : null)
                .build();
    }

    /* Registro público de cliente */
    @Transactional
    public AuthResponse registrarCliente(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (usuarioRepository.existsByRut(request.getRut())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUT ya está registrado");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .rut(request.getRut())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.CLIENTE)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        String token = jwtUtil.generarToken(guardado);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .id(guardado.getId())
                .nombres(guardado.getNombres())
                .apellidos(guardado.getApellidos())
                .email(guardado.getEmail())
                .rol(guardado.getRol() != null ? guardado.getRol().name() : null)
                .build();
    }

    /* Registro de administrador (asigna rol ADMIN) */
    @Transactional
    public AuthResponse registrarAdmin(RegistroRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (usuarioRepository.existsByRut(request.getRut())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El RUT ya está registrado");
        }

        Usuario nuevoUsuario = Usuario.builder()
                .rut(request.getRut())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(Rol.ADMIN)
                .active(true)
                .build();

        Usuario guardado = usuarioRepository.save(nuevoUsuario);
        String token = jwtUtil.generarToken(guardado);

        return AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .id(guardado.getId())
                .nombres(guardado.getNombres())
                .apellidos(guardado.getApellidos())
                .email(guardado.getEmail())
                .rol(guardado.getRol() != null ? guardado.getRol().name() : null)
                .build();
    }

    /*
      Obtener el perfil del usuario autenticado actual (/me) a partir de su RUT
     */
    @Transactional(readOnly = true)
    public UsuarioPerfilResponse obtenerPerfilActual(String rut) {
        Usuario usuario = usuarioRepository.findByRut(rut)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con RUT: " + rut));

        return usuarioMapper.toPerfilResponse(usuario);
    }

    /* Valida un token JWT recibido de otros microservicios a partir de su RUT */
    @Transactional(readOnly = true)
    public ValidarTokenResponse validarToken(String tokenHeader) {
        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            return ValidarTokenResponse.builder()
                    .valid(false)
                    .message("Encabezado de autorización ausente o formato no válido (debe ser 'Bearer <token>')")
                    .build();
        }

        String token = tokenHeader.substring(7);
        try {
            String rut = jwtUtil.extractRut(token);
            Usuario usuario = usuarioRepository.findByRut(rut).orElse(null);

            if (usuario == null) {
                return ValidarTokenResponse.builder()
                        .valid(false)
                        .message("El usuario asociado al token no existe")
                        .build();
            }

            if (!Boolean.TRUE.equals(usuario.getActive())) {
                return ValidarTokenResponse.builder()
                        .valid(false)
                        .message("La cuenta del usuario está deshabilitada")
                        .build();
            }

            if (!jwtUtil.isTokenValid(token, usuario)) {
                return ValidarTokenResponse.builder()
                        .valid(false)
                        .message("Token expirado o no coincide con el usuario")
                        .build();
            }

            return ValidarTokenResponse.builder()
                    .valid(true)
                    .id(usuario.getId())
                    .nombres(usuario.getNombres())
                    .apellidos(usuario.getApellidos())
                    .email(usuario.getEmail())
                    .rol(usuario.getRol() != null ? usuario.getRol().name() : null)
                    .message("Token válido")
                    .build();
        } catch (Exception e) {
            return ValidarTokenResponse.builder()
                    .valid(false)
                    .message("Token inválido o manipulado: " + e.getMessage())
                    .build();
        }
    }
}
