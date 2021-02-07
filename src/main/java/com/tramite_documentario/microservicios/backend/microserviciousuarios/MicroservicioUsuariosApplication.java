package com.tramite_documentario.microservicios.backend.microserviciousuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EntityScan({"com.tramite_documentario.microservicios.backend.commonusuarios.models.entity"})
public class MicroservicioUsuariosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroservicioUsuariosApplication.class, args);
    }

}
