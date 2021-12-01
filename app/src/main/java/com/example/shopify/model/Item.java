package com.example.shopify.model;

public class Item {
    private Long id;
    private String name;
    private double price;
    private String type;
    private String image;

    public Item(String name, String type, double price, String image)
    {
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
