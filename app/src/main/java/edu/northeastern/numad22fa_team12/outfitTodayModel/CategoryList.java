package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class CategoryList {

    private Category tops;
    private Category bottoms;
    private Category dresses;
    private Category suits;
    private Category shoes;
    private Category handbags;

    public CategoryList() {
        this.tops = new Category();
        this.bottoms = new Category();
        this.dresses = new Category();
        this.suits = new Category();
        this.shoes = new Category();
        this.handbags = new Category();
    }

    public Category getTops() {
        return this.tops;
    }

    public Category getBottoms() {
        return this.bottoms;
    }

    public Category getDresses() {
        return this.dresses;
    }

    public Category getSuits() {
        return this.suits;
    }

    public Category getShoes() {
        return this.shoes;
    }

    public Category getHandbags() {
        return this.handbags;
    }

}
