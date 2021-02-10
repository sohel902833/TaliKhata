package com.example.talikhata.DataModuler;

public class Bill {
    String key;
    String details,time,date,getMoney,givenMoney;

    public Bill(){

    }

    public Bill(String key, String details, String time, String date, String getMoney, String givenMoney) {
        this.key = key;
        this.details = details;
        this.time = time;
        this.date = date;
        this.getMoney = getMoney;
        this.givenMoney = givenMoney;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGetMoney() {
        return getMoney;
    }

    public void setGetMoney(String getMoney) {
        this.getMoney = getMoney;
    }

    public String getGivenMoney() {
        return givenMoney;
    }

    public void setGivenMoney(String givenMoney) {
        this.givenMoney = givenMoney;
    }
}
