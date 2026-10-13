package org.gestionpastelera.ms_auth.repository;

import org.gestionpastelera.ms_auth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByRut(String rut);

    boolean existsByEmail(String email);
    boolean existsByRut(String rut);
}
