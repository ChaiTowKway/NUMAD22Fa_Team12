package edu.northeastern.numad22fa_team12.outfitTodayModel;

public class Category {
    private Season season;
    private Occasion occasion;

    public Category() {
        this.season = new Season();
        this.occasion= new Occasion();
    }

    public Season getSeason() {
        return this.season;
    }

    public Occasion getOccasion() {
        return this.occasion;
    }

}
