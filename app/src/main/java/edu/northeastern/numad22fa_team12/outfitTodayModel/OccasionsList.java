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

    public void setMonday_Daytime(String occasion) {
        monday_Daytime = occasion;
    }

    public void setMonday_Night(String occasion) {
        monday_Night = occasion;
    }

    public void setTuesday_Daytime(String occasion) {
        tuesday_Daytime = occasion;
    }

    public void setTuesday_Night(String occasion) {
        tuesday_Night = occasion;
    }

    public void setWednesday_Daytime(String occasion) {
        wednesday_Daytime = occasion;
    }

    public void setWednesday_Night(String occasion) {
        wednesday_Night = occasion;
    }

    public void setThursday_Daytime(String occasion) {
        thursday_Daytime = occasion;
    }

    public void setThursday_Night(String occasion) {
        thursday_Night = occasion;
    }

    public void setFriday_Daytime(String occasion) {
        friday_Daytime = occasion;
    }

    public void setFriday_Night(String occasion) {
        friday_Night = occasion;
    }

    public void setSaturday_Daytime(String occasion) {
        saturday_Daytime = occasion;
    }

    public void setSaturday_Night(String occasion) {
        saturday_Night = occasion;
    }

    public void setSunday_Daytime(String occasion) {
        sunday_Daytime = occasion;
    }

    public void setSunday_Night(String occasion) {
        sunday_Night = occasion;
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
        if (occasion.length() < 1) {
            return "No occasion configured";
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
