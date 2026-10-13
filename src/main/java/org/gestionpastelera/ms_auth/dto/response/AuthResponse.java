package org.gestionpastelera.ms_auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {

        private String token;

    @Builder.Default
        private String tokenType = "Bearer";

        private Long id;

        private String nombres;

        private String apellidos;

        private String email;

        private String rol;
}
