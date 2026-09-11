package com.iyaadam.app;

public class Dish {
    public String id;
    public String name;
    public int price;
    public String description;
    public String imageUrl;
    public double rating;
    public int reviewCount;
    public int prepTime;
    public boolean available;

    public Dish(String id, String name, int price, String description, String imageUrl, 
                double rating, int reviewCount, int prepTime, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.prepTime = prepTime;
        this.available = available;
    }
}
