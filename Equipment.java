package com.example.theukuleleband.modules.storeman;
class Equipment {
    private int equipmentID;
    private int price;
    private String equipmentName;
    private String description;
    private char purchaseDate;
    private String condition;
    private String availability;

    public Equipment(int equipmentID, int price, String equipmentName,String description, char purchaseDate, String condition, String availability){
        this.equipmentID = equipmentID;
        this.price = price;
        this.equipmentName = equipmentName;
        this.description = description;
        this.purchaseDate = purchaseDate;
        this.condition = condition;
        this.availability = availability;
    }
    //Getters and Setters
    public int getEquipmentID(){
        return equipmentID;
    }
    public int getPrice(){
        return price;
    }
    public String getEquipmentName(){ return equipmentName; }
    public String getDescription(){
        return description;
    }
    public char getPurchaseDate(){
        return purchaseDate;
    }
    public String getCondition(){
        return condition;
    }
    public String getAvailability(){
        return availability;
    }
}