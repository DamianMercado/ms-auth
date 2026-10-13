package org.gestionpastelera.ms_auth.controller;

import lombok.RequiredArgsConstructor;
import org.gestionpastelera.ms_auth.enums.Rol;
import org.gestionpastelera.ms_auth.service.RolService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RolController {

    private final RolService rolService;

        @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Rol>> obtenerTodosLosRoles() {
        return ResponseEntity.ok(rolService.obtenerTodosLosRoles());
    }

        @GetMapping("/usuarios/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Rol> obtenerRolPorUsuario(@PathVariable Long usuarioId) {
        Rol rol = rolService.obtenerRolDeUsuario(usuarioId);
        return ResponseEntity.ok(rol);
    }

        @PutMapping("/usuarios/{usuarioId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> actualizarRolUsuario(
            @PathVariable Long usuarioId,
            @RequestBody Rol nuevoRol) {
        rolService.actualizarRolDeUsuario(usuarioId, nuevoRol);
        return ResponseEntity.noContent().build();
    }
}
