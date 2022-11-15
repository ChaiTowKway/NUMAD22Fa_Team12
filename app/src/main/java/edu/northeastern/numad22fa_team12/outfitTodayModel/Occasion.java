package edu.northeastern.numad22fa_team12.outfitTodayModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Occasion {
    private HashMap<String, String> work;
    private HashMap<String, String>  casual;
    private HashMap<String, String>  formal;
    private HashMap<String, String>  sports;

    public Occasion() {
        work = new HashMap<>();
        casual = new HashMap<>();
        formal = new HashMap<>();
        sports = new HashMap<>();
        work.put("url", "");
        casual.put("url", "");
        formal.put("url", "");
        sports.put("url", "");
    }

    public Occasion(HashMap<String, String>  work, HashMap<String, String>  casual,
                    HashMap<String, String>  formal, HashMap<String, String>  sports) {
        this.work = new HashMap<>(work);
        this.casual = new HashMap<>(casual);
        this.formal = new HashMap<>(formal);
        this.sports = new HashMap<>(sports);
    }

    public HashMap<String, String>  getWork() {
        return this.work;
    }

    public HashMap<String, String>  getCasual() {
        return this.casual;
    }

    public HashMap<String, String>  getFormal() {
        return this.formal;
    }

    public HashMap<String, String>  getSports() {
        return this.sports;
    }

    public void setWork(String item) {
        this.work.put(item, "");
    }

    public void setCasual(String item) {
        this.casual.put(item, "");
    }

    public void setFormal(String item) {
        this.formal.put(item, "");
    }

    public void setSports(String item) {
        this.sports.put(item, "");
    }

}
