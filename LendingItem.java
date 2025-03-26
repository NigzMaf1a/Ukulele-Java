package com.example.theukuleleband.modules.storeman;

public class LendingItem {
    private int lendID;
    private String genre;
    private String lendingType;
    private int cost;
    private int hours;

    public LendingItem(int lendID, String genre, String lendingType, int cost, int hours) {
        this.lendID = lendID;
        this.genre = genre;
        this.lendingType = lendingType;
        this.cost = cost;
        this.hours = hours;
    }

    public int getLendID() { return lendID; }
    public String getGenre() { return genre; }
    public String getLendingType() { return lendingType; }
    public int getCost() { return cost; }
    public int getHours() { return hours; }
}
