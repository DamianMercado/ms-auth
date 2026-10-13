package org.gestionpastelera.ms_auth.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import org.gestionpastelera.ms_auth.enums.Rol;
import org.gestionpastelera.ms_auth.model.Usuario;
import org.gestionpastelera.ms_auth.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RolService {

    private final UsuarioRepository usuarioRepository;

    /**
     * Devuelve la lista completa de roles disponibles en el enum
     */
    public List<Rol> obtenerTodosLosRoles() {
        return Arrays.asList(Rol.values());
    }

    /**
     * Obtiene el rol único de un usuario específico
     */
    @Transactional(readOnly = true)
    public Rol obtenerRolDeUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + usuarioId));
        return usuario.getRol();
    }

    /**
     * Actualiza el rol único de un usuario
     */
    @Transactional
    public void actualizarRolDeUsuario(Long usuarioId, Rol nuevoRol) {
        if (nuevoRol == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe proporcionar un rol válido");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado con ID: " + usuarioId));

        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
    }
}
