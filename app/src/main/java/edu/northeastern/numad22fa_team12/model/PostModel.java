package edu.northeastern.numad22fa_team12.model;

import com.google.gson.annotations.SerializedName;

public class PostModel {
    private double price;
    private String image_link;
    private String name;
    private String brand;
    @SerializedName("product_type")
    private String productType;
    private String description;

    public PostModel(int price, String image_link, String name, String brand, String productType, String description) {
        this.price = price;
        this.image_link = image_link;
        this.name = name;
        this.brand = brand;
        this.description = description;
    }

    // declare
    public double getPrice() {
        return price;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getName() {
        return name;
    }

    public String getBrand() {
        return brand;
    }

    public String getProductType() {
        return productType;
    }

    public String getDescription() {
        return description;
    }
}
