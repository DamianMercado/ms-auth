package org.gestionpastelera.ms_auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioPerfilResponse {

        private Long id;

        private String rut;

        private String nombres;

        private String apellidos;

        private String email;

        private String rol;
}
