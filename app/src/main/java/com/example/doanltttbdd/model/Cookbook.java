package com.example.doanltttbdd.model;

import java.util.List;

public class Cookbook {
    private String creatorId;
    private String recipeName;
    private List<String> ingredients;
    private List<String> instructions;
    private String preparationTime;
    private int servingSize;
    private String image;
    private String description;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Cookbook() {
    }

    public Cookbook(String creatorId,String description, String recipeName, List<String> ingredients, List<String> instructions, String preparationTime, int servingSize, String image) {
        this.creatorId = creatorId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.servingSize = servingSize;
        this.image = image;
        this.description = description;
    }
    public Cookbook(String key,String creatorId,String description, String recipeName, List<String> ingredients, List<String> instructions, String preparationTime, int servingSize, String image) {
        this.creatorId = creatorId;
        this.recipeName = recipeName;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.preparationTime = preparationTime;
        this.servingSize = servingSize;
        this.image = image;
        this.description = description;
        this.key = key;
    }
    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(List<String> instructions) {
        this.instructions = instructions;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public void setPreparationTime(String preparationTime) {
        this.preparationTime = preparationTime;
    }

    public int getServingSize() {
        return servingSize;
    }

    public void setServingSize(int servingSize) {
        this.servingSize = servingSize;
    }



    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Cookbook{" +
                ", creatorId='" + creatorId + '\'' +
                ", recipeName='" + recipeName + '\'' +
                ", ingredients=" + ingredients +
                ", instructions='" + instructions + '\'' +
                ", preparationTime=" + preparationTime +
                ", servingSize=" + servingSize +
                ", image='" + image + '\'' +
                '}';
    }
}
