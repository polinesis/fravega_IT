package com.example.microservicios.app.sucursales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.example.commons.sucursales.models.entity"})
public class MicroservicioSucursalesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioSucursalesApplication.class, args);
	}

}
