package org.gestionpastelera.ms_auth.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.gestionpastelera.ms_auth.dto.request.ActualizarUsuarioRequest;
import org.gestionpastelera.ms_auth.dto.request.CambiarPasswordRequest;
import org.gestionpastelera.ms_auth.dto.request.CrearUsuarioRequest;
import org.gestionpastelera.ms_auth.dto.response.UsuarioResponse;
import org.gestionpastelera.ms_auth.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*
    Estos endpoints get almacenan datos sensibles de los usuarios entonces por eso los datos
    estan con preautorizacion del admin para acceder a ellos.
    */
        @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioResponse>> listarUsuarios(Pageable pageable) {
        Page<UsuarioResponse> usuarios = usuarioService.listarUsuarios(pageable);
        return ResponseEntity.ok(usuarios);
    }

        @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Long id) {
        UsuarioResponse usuario = usuarioService.obtenerPorId(id);
        return ResponseEntity.ok(usuario);
    }

        @GetMapping("/rut/{rut}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> obtenerPorRut(@PathVariable String rut) {
        UsuarioResponse usuario = usuarioService.obtenerPorRut(rut);
        return ResponseEntity.ok(usuario);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> crearUsuario(@Valid @RequestBody CrearUsuarioRequest request) {
        UsuarioResponse nuevoUsuario = usuarioService.crearUsuario(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

        @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponse> actualizarUsuario(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarUsuarioRequest request) {
        UsuarioResponse actualizado = usuarioService.actualizarUsuario(id, request);
        return ResponseEntity.ok(actualizado);
    }

        @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarEstadoUsuario(
            @PathVariable Long id,
            @RequestParam boolean activo) {
        usuarioService.cambiarEstado(id, activo);
        return ResponseEntity.noContent().build();
    }

        @PatchMapping("/me/password")
    public ResponseEntity<Void> cambiarPasswordPropia(
            org.springframework.security.core.Authentication authentication,
            @Valid @RequestBody CambiarPasswordRequest request) {
        usuarioService.cambiarPassword(authentication.getName(), request);
        return ResponseEntity.noContent().build();
    }

        @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}