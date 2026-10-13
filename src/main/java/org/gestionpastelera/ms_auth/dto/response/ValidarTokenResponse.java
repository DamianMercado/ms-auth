package org.gestionpastelera.ms_auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidarTokenResponse {

        private boolean valid;

        private Long id;

        private String nombres;

        private String apellidos;

        private String email;

        private String rol;

        private String message;
}
