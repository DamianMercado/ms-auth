package org.gestionpastelera.ms_auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

        private Long id;

        private String rut;

        private String nombres;

        private String apellidos;

        private String email;

        private String rol;

        private Boolean active;
}
