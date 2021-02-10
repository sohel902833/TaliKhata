package com.example.talikhata;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomAlert {
    String title,message;
    Context context;
    private  ProgressDialog progressDialog;

    public CustomAlert(Context context,String title, String message) {
        this.context=context;
        this.title = title;
        this.message = message;
    }

    public void showAlert(){
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    public void cancelAlert(){
        progressDialog.dismiss();
    }






}
