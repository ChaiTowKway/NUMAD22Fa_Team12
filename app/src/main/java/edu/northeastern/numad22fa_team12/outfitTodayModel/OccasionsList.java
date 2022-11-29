package edu.northeastern.numad22fa_team12.outfitTodayModel;
import androidx.annotation.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Objects;

public class OccasionsList {
    private String monday_Daytime;
    private String monday_Night;
    private String tuesday_Daytime;
    private String tuesday_Night;
    private String wednesday_Daytime;
    private String wednesday_Night;
    private String thursday_Daytime;
    private String thursday_Night;
    private String friday_Daytime;
    private String friday_Night;
    private String saturday_Daytime;
    private String saturday_Night;
    private String sunday_Daytime;
    private String sunday_Night;

    public OccasionsList() {
        monday_Daytime = "";
        monday_Night = "";
        tuesday_Daytime = "";
        tuesday_Night = "";
        wednesday_Daytime = "";
        wednesday_Night = "";
        thursday_Daytime = "";
        thursday_Night = "";
        friday_Daytime = "";
        friday_Night = "";
        saturday_Daytime = "";
        saturday_Night = "";
        sunday_Daytime = "";
        sunday_Night = "";
    }

    public String getMonday_Daytime() {
        return monday_Daytime;
    }

    public String getMonday_Night() {
        return monday_Night;
    }

    public String getTuesday_Daytime() {
        return tuesday_Daytime;
    }

    public String getTuesday_Night() {
        return tuesday_Night;
    }

    public String getWednesday_Daytime() {
        return wednesday_Daytime;
    }

    public String getWednesday_Night() {
        return wednesday_Night;
    }

    public String getThursday_Daytime() {
        return thursday_Daytime;
    }

    public String getThursday_Night() {
        return thursday_Night;
    }

    public String getFriday_Daytime() {
        return friday_Daytime;
    }

    public String getFriday_Night() {
        return friday_Night;
    }

    public String getSaturday_Daytime() {
        return saturday_Daytime;
    }

    public String getSaturday_Night() {
        return saturday_Night;
    }

    public String getSunday_Daytime() {
        return sunday_Daytime;
    }

    public String getSunday_Night() {
        return sunday_Night;
    }

    public void setMonday_Daytime(String monday_Daytime) {
        monday_Daytime = monday_Daytime;
    }

    public void setMonday_Night(String monday_Night) {
        monday_Night = monday_Night;
    }

    public void setTuesday_Daytime(String tuesday_Daytime) {
        tuesday_Daytime = tuesday_Daytime;
    }

    public void setTuesday_Night(String tuesday_Night) {
        tuesday_Night = tuesday_Night;
    }

    public void setWednesday_Daytime(String wednesday_Daytime) {
        wednesday_Daytime = wednesday_Daytime;
    }

    public void setWednesday_Night(String wednesday_Night) {
        wednesday_Night = wednesday_Night;
    }

    public void setThursday_Daytime(String thursday_Daytime) {
        thursday_Daytime = thursday_Daytime;
    }

    public void setThursday_Night(String thursday_Night) {
        thursday_Night = thursday_Night;
    }

    public void setFriday_Daytime(String friday_Daytime) {
        friday_Daytime = friday_Daytime;
    }

    public void setFriday_Night(String friday_Night) {
        friday_Night = friday_Night;
    }

    public void setSaturday_Daytime(String saturday_Daytime) {
        saturday_Daytime = saturday_Daytime;
    }

    public void setSaturday_Night(String saturday_Night) {
        saturday_Night = saturday_Night;
    }

    public void setSunday_Daytime(String sunday_Daytime) {
        sunday_Daytime = sunday_Daytime;
    }

    public void setSunday_Night(String sunday_Night) {
        sunday_Night = sunday_Night;
    }

    public void updateOccasion(@NonNull String time, String occasion) {
        switch (time) {
            case "monday_Daytime":
                setMonday_Daytime(occasion);
                break;
            case "monday_Night":
                setMonday_Night(occasion);
                break;
            case "tuesday_Daytime":
                setTuesday_Daytime(occasion);
                break;
            case "tuesday_Night":
                setTuesday_Night(occasion);
                break;
            case "wednesday_Daytime":
                setWednesday_Daytime(occasion);
                break;
            case "wednesday_Night":
                setWednesday_Night(occasion);
                break;
            case "thursday_Daytime":
                setThursday_Daytime(occasion);
                break;
            case "thursday_Night":
                setThursday_Night(occasion);
                break;
            case "friday_Daytime":
                setFriday_Daytime(occasion);
                break;
            case "friday_Night":
                setFriday_Night(occasion);
                break;
            case "saturday_Daytime":
                setSaturday_Daytime(occasion);
                break;
            case "saturday_Night":
                setSaturday_Night(occasion);
                break;
            case "sunday_Daytime":
                setSunday_Daytime(occasion);
                break;
            case "sunday_Night":
                setSunday_Night(occasion);
                break;
        }
    }

    public String checkOccasion() {
        String dateOfWeek = checkDateOfWeek();
        String currentTime = checkTime();
        if (!Objects.equals(dateOfWeek, "") && !currentTime.equals("")) {
            String dateAndTime = dateOfWeek + "_" + currentTime;
            return checkOccasionWithDate(dateAndTime);
        }
        return "No occasion found!";
    }

    private String checkOccasionWithDate(@NonNull String dateAndTime) {
        String occasion = "";
        switch (dateAndTime) {
            case "monday_Daytime":
                occasion = getMonday_Daytime();
                break;
            case "monday_Night":
                occasion = getMonday_Night();
                break;
            case "tuesday_Daytime":
                occasion = getTuesday_Daytime();
                break;
            case "tuesday_Night":
                occasion = getTuesday_Night();
                break;
            case "wednesday_Daytime":
                occasion = getWednesday_Daytime();
                break;
            case "wednesday_Night":
                occasion = getWednesday_Night();
                break;
            case "thursday_Daytime":
                occasion = getThursday_Daytime();
                break;
            case "thursday_Night":
                occasion = getThursday_Night();
                break;
            case "friday_Daytime":
                occasion = getFriday_Daytime();;
                break;
            case "friday_Night":
                occasion = getFriday_Night();
                break;
            case "saturday_Daytime":
                occasion = getSaturday_Daytime();
                break;
            case "saturday_Night":
                occasion = getSaturday_Night();
                break;
            case "sunday_Daytime":
                occasion = getSunday_Daytime();
                break;
            case "sunday_Night":
                occasion = getSunday_Night();
                break;
        }
        return occasion;
    }

    private String checkDateOfWeek() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        return dayOfWeek.toString().toLowerCase();
    }

    private String checkTime() {
        String time = "";
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour < 17) {
            time = "Daytime";
        } else {
            time = "Night";
        }
        return time;
    }
}
