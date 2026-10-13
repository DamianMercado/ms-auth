package org.gestionpastelera.ms_auth.model;

import jakarta.persistence.*;
import lombok.*;
import org.gestionpastelera.ms_auth.enums.Rol;

@Entity
@Table(name = "usuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rut", nullable = false, unique = true, length = 12)
        private String rut;

    @Column(name = "nombres", nullable = false, length = 100)
        private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
        private String apellidos;

    @Column(name = "email", nullable = false, unique = true, length = 100)
        private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 30)
        private Rol rol;

    @Builder.Default
    private Boolean active = true;
}
