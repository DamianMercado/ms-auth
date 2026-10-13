package org.gestionpastelera.ms_auth.mapper;

import org.gestionpastelera.ms_auth.dto.request.CrearUsuarioRequest;
import org.gestionpastelera.ms_auth.dto.response.UsuarioPerfilResponse;
import org.gestionpastelera.ms_auth.dto.response.UsuarioResponse;
import org.gestionpastelera.ms_auth.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponse toResponse(Usuario usuario);

    UsuarioPerfilResponse toPerfilResponse(Usuario usuario);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", constant = "true")
    @Mapping(target = "password", ignore = true)
    Usuario toEntity(CrearUsuarioRequest request);
}
