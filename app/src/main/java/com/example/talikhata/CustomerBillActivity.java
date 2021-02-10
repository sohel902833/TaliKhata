package com.example.talikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class CustomerBillActivity extends AppCompatActivity {


    private RelativeLayout relativeLayout;
    private Button backButton,detailsButton,addButton;
    private TextView customerNameTextview,customerMoneyTextview,customerSymboleTextview;

    private  EditText getMoneyEdittext,givenMoneyEdittext,detailsEdittext;

    //<=----------------Firebase---------------->]

    private DatabaseReference customerRef,billRef;
    private FirebaseAuth mAuth;
    private  String currentUserid;


    //<----------------prev Actiity Data--------------------->
    String customerId,customerName,prevGetMoney,prevGivenMoney,todayDate;
    int prevGetBalance=0,prevGivenBalance=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bill);


        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyy");
         todayDate=currentDate.format(calForDate.getTime());




        getSupportActionBar().hide();

            mAuth=FirebaseAuth.getInstance();
            currentUserid=mAuth.getCurrentUser().getUid();
            customerRef= FirebaseDatabase.getInstance().getReference().child("tali").child("user").child(currentUserid).child("userData");
            billRef= FirebaseDatabase.getInstance().getReference().child("tali").child("user").child(currentUserid).child("billDetails");



        customerId=getIntent().getStringExtra("key");
        customerName=getIntent().getStringExtra("customerName");
        prevGetMoney=getIntent().getStringExtra("prevGetMoney");
        prevGivenMoney=getIntent().getStringExtra("prevGivenMoney");
        if(prevGetMoney!=null){
            prevGetBalance=Integer.parseInt(prevGetMoney);
        }

        if(prevGivenMoney!=null){
            prevGivenBalance=Integer.parseInt(prevGivenMoney);
        }


        backButton=findViewById(R.id.bill_BackButtonid);
        detailsButton=findViewById(R.id.bill_DetailsButton);
        addButton=findViewById(R.id.addCustomerBillButton);

        customerNameTextview=findViewById(R.id.bill_CustomerNameTextviewid);
        customerMoneyTextview=findViewById(R.id.bill_CustomerAccountTextviewid);
        customerSymboleTextview=findViewById(R.id.bill_UserNameSymbleTextviewid);
        relativeLayout=findViewById(R.id.blr1);
        getMoneyEdittext=findViewById(R.id.bill_getMoneyEdittextid);
        givenMoneyEdittext=findViewById(R.id.bill_givenMoneyEdittextid);
        detailsEdittext=findViewById(R.id.bill_DetailsEdittextid);

        customerNameTextview.setText(""+customerName);
        customerSymboleTextview.setText(customerName.substring(0,2));



        if(prevGetBalance==0 && prevGivenBalance==0){
            customerMoneyTextview.setText(0+"\u09F3");
        }else if(prevGetBalance>0){
            customerMoneyTextview.setText("আমি পাবো : "+prevGetBalance+"\u09F3");
        }else if(prevGivenBalance>0){
            customerMoneyTextview.setText("সে পাবে : "+prevGivenBalance+"\u09F3");
        }

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getMoney=getMoneyEdittext.getText().toString();
                String givenMoney=givenMoneyEdittext.getText().toString();


                if(!getMoney.isEmpty() && !givenMoney.isEmpty()){
                    calculateAll(Integer.parseInt(givenMoney),Integer.parseInt(getMoney));
                }else if(!getMoney.isEmpty()){
                   setGetMoney(Integer.parseInt(getMoney));
                }else if(!givenMoney.isEmpty()){
                   setGivenMoney(Integer.parseInt(givenMoney));

                }else{
                    Toast.makeText(CustomerBillActivity.this, "Write Something", Toast.LENGTH_SHORT).show();
                 }
            }
        });



        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CustomerBillActivity.this,CustomerBillDetailsActivity.class);
                intent.putExtra("customerId",customerId);
                startActivity(intent);
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(CustomerBillActivity.this,CustomerBillDetailsActivity.class);
                intent.putExtra("customerId",customerId);
                startActivity(intent);
            }
        });





    }

    private void calculateAll(int givenBalance ,int getBalance) {
        setDetailsToDatabase(String.valueOf(getBalance),String.valueOf(givenBalance));
        int totalgivenBalance=prevGivenBalance+givenBalance;
        int totalgetBalance=prevGetBalance+getBalance;


        if(totalgivenBalance>totalgetBalance){
            int newGivenBalance=totalgivenBalance-totalgetBalance;
            setGivenMoneyToDatabase(String.valueOf(newGivenBalance));
            setGetMoneyToDatabase("0");
        }
        else{
            int newGetBalance=totalgetBalance-totalgivenBalance;
            setGetMoneyToDatabase(String.valueOf(newGetBalance));
            setGivenMoneyToDatabase("0");
        }





    }

    public void setGetMoney(int getMoney){
        setDetailsToDatabase(String.valueOf(getMoney),"0");
        if(prevGivenBalance>getMoney){
            int newGivenBalance=prevGivenBalance-getMoney;
            setGivenMoneyToDatabase(String.valueOf(newGivenBalance));
            setGetMoneyToDatabase("0");
        }else if(prevGivenBalance>0 && getMoney>prevGivenBalance){
           int newGetMoney=getMoney-prevGivenBalance;
           setGetMoneyToDatabase(String.valueOf(newGetMoney));
           setGivenMoneyToDatabase("0");
        }else if(prevGivenBalance==0){
            int newGetMoney=prevGetBalance+getMoney;
            setGetMoneyToDatabase(String.valueOf(newGetMoney));
        }else{
            setGetMoneyToDatabase("0");
            setGivenMoneyToDatabase("0");
        }
    }
   public void setGivenMoney(int givenBalance){
       setDetailsToDatabase("0",String.valueOf(givenBalance));
        if(prevGetBalance<givenBalance){
            int newGivenBalance=prevGivenBalance+(givenBalance-prevGetBalance);
            setGivenMoneyToDatabase(String.valueOf(newGivenBalance));
            setGetMoneyToDatabase("0");
        }else{
            int newGetMoney=prevGetBalance-givenBalance;
            setGetMoneyToDatabase(String.valueOf(newGetMoney));
        }

    }


    public void setDetailsToDatabase(String getMoney,String givenMoney){
        Calendar calForDate=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd-MMMM-yyy");
        String saveCurrentDate=currentDate.format(calForDate.getTime());

        Calendar callForTime=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        String saveCurrentTime=currentTime.format(callForTime.getTime());
        String  postRandomName=saveCurrentDate+saveCurrentTime+System.currentTimeMillis();

        HashMap<String,String> billMap=new HashMap<>();

        billMap.put("details",detailsEdittext.getText().toString());
        billMap.put("time",saveCurrentTime);
        billMap.put("date",saveCurrentDate);
        billMap.put("key",postRandomName);
        billMap.put("getMoney",getMoney);
        billMap.put("givenMoney",givenMoney);
        billRef.child(customerId).child(postRandomName)
                .setValue(billMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    sendUserToMainActivity();
                    if(saveCurrentDate.equals(todayDate)){
                        String totalGetMoney=String.valueOf(Integer.parseInt(getTodatGetMoney())+Integer.parseInt(getMoney));
                        String totalGivenMoney=String.valueOf(Integer.parseInt(getTodayGivenMoney())+Integer.parseInt(givenMoney));
                        saveTodayGetMoney(totalGetMoney);
                        saveTodaGivenMoney(totalGivenMoney);

                    }else{
                        saveTodayGetMoney("0");
                        saveTodaGivenMoney("0");

                    }






                }
            }
        });
    }






    public void setGetMoneyToDatabase(String getMoney){
        customerRef.child(customerId)
                .child("getMoney").setValue(getMoney)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            sendUserToMainActivity();
                        }
                    }
                });
    }


    public void saveTodayGetMoney(String getMoney){
        SharedPreferences sharedPreferences=getSharedPreferences("todaySell", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();

        editor.putString("getMoney",getMoney);
        editor.commit();
    }

    public void saveTodaGivenMoney(String givenMoney){
        SharedPreferences sharedPreferences=getSharedPreferences("todaySell",Context.MODE_PRIVATE);
       SharedPreferences.Editor editor= sharedPreferences.edit();
       editor.putString("givenMoney",givenMoney);
       editor.commit();
    }
    public String getTodatGetMoney(){
        SharedPreferences sharedPreferences=getSharedPreferences("todaySell", Context.MODE_PRIVATE);
        return sharedPreferences.getString("getMoney","0");
    }

    public String getTodayGivenMoney(){
        SharedPreferences sharedPreferences=getSharedPreferences("todaySell",Context.MODE_PRIVATE);
        return sharedPreferences.getString("givenMoney","0");
    }







    public void setGivenMoneyToDatabase(String givenMoney){
        customerRef.child(customerId)
                .child("givenMoney").setValue(givenMoney)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            sendUserToMainActivity();
                        }
                    }
                });
    }


    private void sendUserToMainActivity() {
        Intent intent=new Intent(CustomerBillActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


}