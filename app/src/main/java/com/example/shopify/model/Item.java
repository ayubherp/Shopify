package com.example.shopify.model;

public class Item {
    public String name;
    public double price;
    public String type;
    public String linkPic;

    public Item(String name, String type, double price, String linkPic)
    {
        this.name = name;
        this.price = price;
        this.type = type;
        this.linkPic = linkPic;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setLinkPic(String linkPic) {
        this.linkPic = linkPic;
    }

    public String getLinkPic() {
        return linkPic;
    }
}
