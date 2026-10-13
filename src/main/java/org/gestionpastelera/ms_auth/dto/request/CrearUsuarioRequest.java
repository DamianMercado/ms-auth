package org.gestionpastelera.ms_auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.gestionpastelera.ms_auth.enums.Rol;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrearUsuarioRequest {

    @NotBlank(message = "El RUT es obligatorio")
        private String rut;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100)
        private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(min = 2, max = 100)
        private String apellidos;

    @NotBlank(message = "El correo es obligatorio")
    @Email
        private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6)
        private String password;

    @NotNull(message = "Debe asignar un rol al usuario")
        private Rol rol;
}
