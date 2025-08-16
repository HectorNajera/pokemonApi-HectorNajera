package com.example.demo.controller;

import com.example.demo.model.Pokemon;
import com.example.demo.model.PokemonDTO;
import com.example.demo.model.PokemonResponse;
import com.example.demo.service.PokemonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @Operation(
            summary = "Obtener Pokemon mediante id",
            description = "Devuelve un Pokemon mediante su id."
    )
    @GetMapping("/character/{id}")
    public Mono<PokemonResponse> getPokemon(@PathVariable int id) {
        return pokemonService.getPokemon(id)
                .map(pokemon -> new PokemonResponse(
                        "SUCCESS",
                        "CONTINUE",
                        new PokemonDTO(
                                pokemon.getId(),
                                pokemon.getName(),
                                pokemon.getHeight(),
                                pokemon.getWeight()
                        )
                ))
                .onErrorResume(e -> Mono.just(
                        new PokemonResponse(
                                "ERROR",
                                "CANCEL",
                                "POKEAPI_UNAVAILABLE",
                                "Failed to fetch data from PokeAPI."
                        )
                ));
    }

    @Operation(
            summary = "Obtener lista de Pokemons",
            description = "Devuelve una lista de Pokemons con un límite entre 1 y 100."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pokemons obtenidos exitosamente",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PokemonResponse.class))),
            @ApiResponse(responseCode = "400", description = "Parámetro limit inválido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Error al comunicarse con la PokeAPI",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/characters")
    public ResponseEntity<?> getPokemons(
            @Parameter(description = "Número de personajes a devolver", example = "25")
            @RequestParam(defaultValue = "25") int limit
    ) {
        // Validar rango
        if (limit < 1 || limit > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ErrorResponse(
                            "ERROR",
                            "CANCEL",
                            "INVALID_LIMIT",
                            "Provided value for limit param is invalid."
                    )
            );
        }

        try {
            // Llamada al servicio
            List<Pokemon> pokemons = pokemonService.getPokemons(limit);

            // Convertir a DTO
            List<PokemonDTO> dtoList = pokemons.stream()
                    .map(p -> new PokemonDTO(
                            p.getId(),
                            p.getName(),
                            p.getHeight(),
                            p.getWeight()
                    ))
                    .collect(Collectors.toList());

            // Respuesta exitosa
            return ResponseEntity.ok(
                    new PokemonResponse("SUCCESS", "CONTINUE", dtoList)
            );

        } catch (Exception e) {
            // Manejo de error de comunicación con la PokeAPI
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(
                            "ERROR",
                            "CANCEL",
                            "POKEAPI_UNAVAILABLE",
                            "Failed to fetch data from PokeAPI."
                    )
            );
        }
    }

    // Modelo de error
    public static class ErrorResponse {
        private String type;
        private String action;
        private String code;
        private String message;

        public ErrorResponse(String type, String action, String code, String message) {
            this.type = type;
            this.action = action;
            this.code = code;
            this.message = message;
        }

        public String getType() { return type; }
        public String getAction() { return action; }
        public String getCode() { return code; }
        public String getMessage() { return message; }
    }

    @GetMapping("/pokemons25-structured")
    @Operation(summary = "Get the first 25 Pokemons in structured format")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pokemons",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PokemonResponse.class))),
            @ApiResponse(responseCode = "500", description = "PokeAPI unavailable",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<?> get25PokemonsStructured() {
        try {
            PokemonResponse response = pokemonService.getFirst25PokemonsStructured();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ErrorResponse(
                            "ERROR",
                            "CANCEL",
                            "POKEAPI_UNAVAILABLE",
                            "Failed to fetch data from PokeAPI."
                    )
            );
        }
    }

}
