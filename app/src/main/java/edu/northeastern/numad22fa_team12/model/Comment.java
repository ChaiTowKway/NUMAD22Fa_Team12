package edu.northeastern.numad22fa_team12.model;

import com.google.gson.annotations.SerializedName;

public class Comment {

    private double price;
    private String image_link;
    private String name;
    private String brand;
    @SerializedName("product_type")
    private String productType;
    private String description;

    public double getPrice() {
        return price;
    }

    public String getImage_link() {
        return image_link;
    }

    public String getName() {
        return name;
    }

    public String getProduct_type() {
        return productType;
    }

    public String getBrand() {
        return brand;
    }

    public String getDescription() {
        return description;
    }
}
