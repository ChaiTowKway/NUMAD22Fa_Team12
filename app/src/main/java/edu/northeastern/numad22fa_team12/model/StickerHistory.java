package edu.northeastern.numad22fa_team12.model;

import java.util.ArrayList;
import java.util.List;

public class StickerHistory {
    public int img1 = 0;
    public int img2 = 0;
    public int img3 = 0;
    public int img4 = 0;
    public int img5 = 0;
    public int img6 = 0;
    public int img7 = 0;
    public StickerHistory(){

    }


    public List<Integer> getUsedRecordList(){
        List<Integer> record = new ArrayList<>();
        record.add(img1);
        record.add(img2);
        record.add(img3);
        record.add(img4);
        record.add(img5);
        record.add(img6);
        record.add(img7);
        return record;
    }

}
