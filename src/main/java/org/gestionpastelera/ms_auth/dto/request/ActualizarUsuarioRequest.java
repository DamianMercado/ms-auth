package org.gestionpastelera.ms_auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ActualizarUsuarioRequest {

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100)
        private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100)
        private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El formato de correo no es válido")
        private String email;
}
