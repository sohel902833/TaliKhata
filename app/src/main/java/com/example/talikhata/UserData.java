package com.example.talikhata;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserData {

    private Activity activity;


    public UserData(Activity activity) {
        this.activity = activity;
    }

    public String getEmail(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String email=sharedPreferences.getString("email","");
        return  email;
    }
    public String getPhoneNumber(){
        SharedPreferences sharedPreferences=activity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String phoneNumber=sharedPreferences.getString("phoneNumber","");
        return  phoneNumber;
    }







}
