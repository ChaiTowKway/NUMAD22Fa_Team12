package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class CategoryList {

    private Category tops;
    private Category bottoms;
    private Category shoes;

    public CategoryList() {
        this.tops = new Category();
        this.bottoms = new Category();
        this.shoes = new Category();
    }

    public Category getTops() {
        return this.tops;
    }

    public Category getBottoms() {
        return this.bottoms;
    }

    public Category getShoes() {
        return this.shoes;
    }

    public void setTops(Category tops) {
        this.tops = tops;
    }

    public void setBottoms(Category bottoms) {
        this.bottoms = bottoms;
    }

    public void setShoes(Category shoes) {
        this.shoes = shoes;
    }

}
