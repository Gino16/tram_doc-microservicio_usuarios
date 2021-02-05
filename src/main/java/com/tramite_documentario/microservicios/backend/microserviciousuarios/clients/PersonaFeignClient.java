package com.tramite_documentario.microservicios.backend.microserviciousuarios.clients;

import com.tramite_documentario.microservicio.backend.commonpersonas.models.entity.Persona;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "microservicio-personas")
public interface PersonaFeignClient {

    @GetMapping("/buscar-por-email/{correo}")
    public Persona findPersonaByCorreo(@PathVariable String correo);
}
