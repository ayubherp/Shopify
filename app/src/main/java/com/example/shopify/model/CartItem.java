package com.example.shopify.model;

public class CartItem {
    private Long id_cart;
    private Long id_user;
    private Long id_item;
    private String name;
    private double price;
    private String type;
    private String image;
    private int amount;
    private double subtotal;
    private int status;

    public CartItem(Long id_cart, Long id_user, Long id_item, String name, double price,
                    String type, String image, int amount, double subtotal, int status) {
        this.id_cart = id_cart;
        this.id_user = id_user;
        this.id_item = id_item;
        this.name = name;
        this.price = price;
        this.type = type;
        this.image = image;
        this.amount = amount;
        this.subtotal = subtotal;
        this.status = status;
    }

    public Long getId_cart() {
        return id_cart;
    }

    public void setId_cart(Long id_cart) {
        this.id_cart = id_cart;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public Long getId_item() {
        return id_item;
    }

    public void setId_item(Long id_item) {
        this.id_item = id_item;
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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
