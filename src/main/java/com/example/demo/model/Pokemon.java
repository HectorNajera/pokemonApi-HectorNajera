package com.example.demo.model;

public class Pokemon {
    private String name;
    private int height;
    private int weight;
    private int id;

    public Pokemon() {
    }

    public Pokemon(String name, int height, int weight,  int id) {
        this.name = name;
        this.height = height;
        this.weight = weight;
        this.id = id;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }
}
