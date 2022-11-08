package edu.northeastern.numad22fa_team12.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.northeastern.numad22fa_team12.R;

public class User {

    private String userName;
    private String userEmail;
    private String userRegistrationToken;
    private List<Sticker> sentStickerList;
    private int numberOfStickersSent;
    private List<Sticker> receivedStickerList;
    private int numberOfStickersReceived;
    private HashMap<String, Integer> sentHistoryRecord;
    private HashMap<String, Integer> receivedHistoryRecord;

    public User(){
        this.userEmail = "";
        this.userName = "";
        this.userRegistrationToken = "";
        this.sentStickerList = new ArrayList<>();
        this.numberOfStickersSent = 0;
        this.receivedStickerList = new ArrayList<>();
        this.numberOfStickersReceived = 0;
        sentHistoryRecord = new HashMap<>();
        receivedHistoryRecord = new HashMap<>();

    }
    public User(String userEmail, String userName, String userRegistrationToken) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userRegistrationToken = userRegistrationToken;
        this.sentStickerList = new ArrayList<>();
        this.numberOfStickersSent = 0;
        this.receivedStickerList = new ArrayList<>();
        this.numberOfStickersReceived = 0;
        sentHistoryRecord = new HashMap<>();
        sentHistoryRecord.put(String.valueOf(R.drawable.cat), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.dog), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.duck), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.hedgehog), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.koala), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.panda), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.pig), 0);
        sentHistoryRecord.put(String.valueOf(R.drawable.rooster), 0);

        receivedHistoryRecord = new HashMap<>();
        receivedHistoryRecord.put(String.valueOf(R.drawable.cat), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.dog), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.duck), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.hedgehog), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.koala), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.panda), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.pig), 0);
        receivedHistoryRecord.put(String.valueOf(R.drawable.rooster), 0);
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserRegistrationToken() {
        return this.userRegistrationToken;
    }

    public List<Sticker> getSentStickerList() {
        return new ArrayList<>(this.sentStickerList);
    }

    public List<Sticker> getReceivedStickerList() {
        return new ArrayList<>(this.receivedStickerList);
    }

    public int getNumberOfStickersSent() {
        return this.sentStickerList.size();
    }

    public int getNumberOfStickersReceived() {
        return this.receivedStickerList.size();
    }

    public HashMap<String, Integer> getSentHistoryRecord() {
        return this.sentHistoryRecord;
    }

    public HashMap<String, Integer> getReceivedHistoryRecord() {
        return this.receivedHistoryRecord;
    }

    public void setNumberOfStickersSent(int num) {
        this.numberOfStickersSent = num;
    }

    public void setNumberOfStickersReceived(int num) {
        this.numberOfStickersReceived = num;
    }

    public void setSentStickerList(Sticker stickerSent) {
        this.sentStickerList.add(stickerSent);
    }

    public void setReceivedStickerList(Sticker stickerReceived) {
        this.receivedStickerList.add(stickerReceived);
    }

}
