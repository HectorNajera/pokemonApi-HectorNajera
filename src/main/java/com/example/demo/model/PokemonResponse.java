package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class PokemonResponse {

    private String type;
    private String action;
    private Data data;
    private String errorCode; // Solo se llena si es error
    private String errorMessage;

    // Constructor para lista de Pokemons
    public PokemonResponse(String type, String action, List<PokemonDTO> characters) {
        this.type = type;
        this.action = action;
        this.data = new Data(characters);
    }

    // Constructor para un solo Pokemon
    public PokemonResponse(String type, String action, PokemonDTO character) {
        this.type = type;
        this.action = action;
        this.data = new Data(character);
    }

    // Constructor para error
    public PokemonResponse(String type, String action, String errorCode, String errorMessage) {
        this.type = type;
        this.action = action;
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL) // Oculta campos null en JSON
    public static class Data {
        private int items;
        private List<PokemonDTO> characters; // solo si es lista
        private PokemonDTO character;        // solo si es uno

        // Caso lista
        public Data(List<PokemonDTO> characters) {
            this.items = characters.size();
            this.characters = characters;
        }

        // Caso único
        public Data(PokemonDTO character) {
            this.items = 1;
            this.character = character;
        }

        public int getItems() { return items; }
        public List<PokemonDTO> getCharacters() { return characters; }
        public PokemonDTO getCharacter() { return character; }
    }

    public String getType() { return type; }
    public String getAction() { return action; }
    public Data getData() { return data; }
}