package edu.northeastern.numad22fa_team12.webservice;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class Product {
    private final String name, category, description, price, imageLink;


    public Product(String name, String category, String description, String price, String imageLink) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getImageLink() {
        return imageLink;
    }

    public ArrayList<String> getALlInfo() {
        ArrayList<String> allProductInfo = new ArrayList<>();
        allProductInfo.add(this.name);
        allProductInfo.add(this.category);
        allProductInfo.add(this.price);
        allProductInfo.add(this.description);
        allProductInfo.add(this.imageLink);
        return allProductInfo;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (!(obj instanceof Product)) return false;
        Product other = (Product) obj;
        return Objects.equals(this.name, other.getName()) &&
                Objects.equals(this.category, other.getCategory()) &&
                Objects.equals(this.price, other.getPrice());
    }
}
