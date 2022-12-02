package edu.northeastern.numad22fa_team12.outfitTodayModel;

public enum OccasionEnum {
    casual(0),
    formal(1),
    sports(2),
    work(3);
    int value;
    OccasionEnum(int i) {
        value = i;
    }
}
