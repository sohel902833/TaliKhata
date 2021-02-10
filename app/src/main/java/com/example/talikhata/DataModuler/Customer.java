package com.example.talikhata.DataModuler;

public class Customer {

    String id,name,type,getMoney,givenMoney;


    public Customer(){

    }


    public Customer(String id, String name, String type, String getMoney, String givenMoney) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.getMoney = getMoney;
        this.givenMoney = givenMoney;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
