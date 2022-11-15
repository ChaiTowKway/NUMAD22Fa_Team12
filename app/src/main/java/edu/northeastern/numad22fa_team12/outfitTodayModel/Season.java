package edu.northeastern.numad22fa_team12.outfitTodayModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Season {
    private HashMap<String, String> spring;
    private HashMap<String, String> summer;
    private HashMap<String, String> autumn;
    private HashMap<String, String> winter;
    private HashMap<String, String> all;

    public Season() {
        this.spring = new HashMap<>();
        this.summer = new HashMap<>();
        this.autumn = new HashMap<>();
        this.winter = new HashMap<>();
        this.all = new HashMap<>();
        this.spring.put("url", "");
        this.summer.put("url", "");
        this.autumn.put("url", "");
        this.winter.put("url", "");
        this.all.put("url", "");
    }

    public Season(HashMap<String, String> spring, HashMap<String, String> summer, HashMap<String, String> autumn, HashMap<String, String> winter, HashMap<String, String> all) {
        this.spring = new HashMap<>(spring);
        this.summer = new HashMap<>(summer);
        this.autumn = new HashMap<>(autumn);
        this.winter = new HashMap<>(winter);
        this.all = new HashMap<>(all);
     }

     public HashMap<String, String> getSpring() {
        return this.spring;
     }

     public HashMap<String, String> getSummer() {
        return this.summer;
     }

     public HashMap<String, String> getAutumn() {
        return this.autumn;
     }

     public HashMap<String, String> getWinter() {
        return this.winter;
     }

     public HashMap<String, String> getAll() {
        return this.all;
     }

     public void setSpring(String item) {
        this.spring.put(item, "");
     }

     public void setSummer(String item) {
        this.summer.put(item, "");
     }

     public void setAutumn(String item) {
        this.autumn.put(item, "");
     }

     public void setWinter(String item) {
        this.winter.put(item, "");
     }

     public void setAll(String item) {
        this.all.put(item, "");
     }
}
