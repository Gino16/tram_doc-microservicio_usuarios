package com.tramite_documentario.microservicios.backend.microserviciousuarios.models.repository;

import com.tramite_documentario.microservicios.backend.commonusuarios.models.entity.Usuario;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(path = "usuarios")
public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    @RestResource(path = "buscar-username")
    public Usuario findByUsername(@Param("nombre") String username);
}

