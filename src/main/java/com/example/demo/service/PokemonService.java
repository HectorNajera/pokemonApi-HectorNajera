package com.example.demo.service;

import com.example.demo.model.Pokemon;
import com.example.demo.model.PokemonDTO;
import com.example.demo.model.PokemonResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PokemonService {
    private final WebClient webClient;
    private final RestTemplate restTemplate = new RestTemplate();

    public PokemonService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("https://pokeapi.co/api/v2").build();
    }

    public Mono<Pokemon> getPokemon(int id) {
        return webClient.get()
                .uri("/pokemon/{id}", id)
                .retrieve()
                .bodyToMono(Pokemon.class);
    }

    public List<Pokemon> getPokemons(int limit) {
        return fetchPokemons(limit);
    }

    public List<Pokemon> getFirst25Pokemons() {
        return fetchPokemons(25);
    }

    private List<Pokemon> fetchPokemons(int limit) {
        String baseUrl = "https://pokeapi.co/api/v2/pokemon";
        String url = baseUrl + "?limit=" + limit;
        Map response = restTemplate.getForObject(url, Map.class);
        List<Map<String, String>> results = (List<Map<String, String>>) response.get("results");

        List<Pokemon> pokemons = new ArrayList<>();

        for (Map<String, String> p : results) {
            String pokemonUrl = p.get("url");
            Map pokemonDetails = restTemplate.getForObject(pokemonUrl, Map.class);

            String name = (String) pokemonDetails.get("name");
            int height = (Integer) pokemonDetails.get("height");
            int weight = (Integer) pokemonDetails.get("weight");
            int id = (Integer) pokemonDetails.get("id");

            pokemons.add(new Pokemon(name, height, weight, id));
        }

        return pokemons;
    }

    public PokemonResponse getFirst25PokemonsStructured() {
        List<Pokemon> pokemons = getFirst25Pokemons();
        List<PokemonDTO> characters = new ArrayList<>();

        int id = 1;
        for (Pokemon p : pokemons) {
            characters.add(new PokemonDTO(
                    id,
                    p.getName(),
                    p.getHeight(),
                    p.getWeight()
            ));
            id++;
        }

        return new PokemonResponse("SUCCESS", "CONTINUE", characters);
    }
}
