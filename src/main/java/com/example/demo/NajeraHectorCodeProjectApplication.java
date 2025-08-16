package com.example.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@OpenAPIDefinition(
		info = @Info(
				title = "Pokémon API - Héctor Nájera",
				version = "1.0",
				description = "API REST que permite consultar información básica de Pokémon utilizando datos provenientes de la PokéAPI pública. "
						+ "Proporciona endpoints para obtener listas de Pokémon, así como detalles individuales como nombre, altura y peso."
		)
)

@SpringBootApplication
public class NajeraHectorCodeProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(NajeraHectorCodeProjectApplication.class, args);
	}

}
