package com.example.demo.model;

public class PokemonDTO {
    private int id;
    private String name;
    private int height;
    private int weight;

    public PokemonDTO(int id, String name,  int height, int weight) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.weight = weight;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getHeight() { return height; }
    public int getWeight() { return weight; }
}