package com.example.talikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talikhata.Adapter.CustomerListAdapter;
import com.example.talikhata.DataModuler.Customer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView customerListRecyclerview;
    private TextView totalGetMoneyText,totalGivenMoneyText,todayTotalGetMoney,todayTodatGivenMoney;


    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    
    private FloatingActionButton addCustomerButton;
    private  String currentUserid;
    private List<Customer> customerDataList=new ArrayList<>();

    private CustomerListAdapter customerListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();

        userRef= FirebaseDatabase.getInstance().getReference().child("tali").child("user").child(currentUserid);

        totalGetMoneyText=findViewById(R.id.main_total_ramaining_balance);
        totalGivenMoneyText=findViewById(R.id.main_total_given_balance);
        todayTodatGivenMoney=findViewById(R.id.main_today_TotalGivenMoney);
        todayTotalGetMoney=findViewById(R.id.main_today_TotalGetMoney);
        addCustomerButton=findViewById(R.id.add_Customer_FloatingButtonid);
        customerListRecyclerview=findViewById(R.id.mainCustomerListRecyclerviewid);
        customerListRecyclerview.setHasFixedSize(true);
        customerListRecyclerview.setLayoutManager(new LinearLayoutManager(this));



        todayTotalGetMoney.setText("আজকে দিয়েছি\n"+getTodatGetMoney()+"\u09F3");
        todayTodatGivenMoney.setText("আজকে পেয়েছি\n"+getTodayGivenMoney()+"\u09F3");

        
        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendUserToAddCustomerActivity();
            }
        });
       customerListAdapter=new CustomerListAdapter(this,customerDataList);
       customerListRecyclerview.setAdapter(customerListAdapter);


       customerListAdapter.setOnItemClickListner(new CustomerListAdapter.OnItemClickListner() {
           @Override
           public void onItemClick(int position) {

               Customer currentItem=customerDataList.get(position);

               String givenMoney=currentItem.getGivenMoney();
               String getMoney=currentItem.getGetMoney();

               if(givenMoney.isEmpty()){
                   givenMoney="0";
               }

               if(getMoney.isEmpty()){
                   getMoney="0";
               }


               Intent intent=new Intent(MainActivity.this,CustomerBillActivity.class);
               intent.putExtra("prevGetMoney",getMoney);
               intent.putExtra("prevGivenMoney",givenMoney);
               intent.putExtra("customerName",currentItem.getName());
               intent.putExtra("key",currentItem.getId());

               startActivity(intent);




           }
       });





    }




    public String getTodatGetMoney(){
        SharedPreferences sharedPreferences=getSharedPreferences("todaySell", Context.MODE_PRIVATE);
        return sharedPreferences.getString("getMoney","0");
    }

    public String getTodayGivenMoney(){
        SharedPreferences sharedPreferences=getSharedPreferences("todaySell",Context.MODE_PRIVATE);
        return sharedPreferences.getString("givenMoney","0");
    }





    @Override
    protected void onStart() {
        super.onStart();

        userRef.child("userData").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                customerDataList.clear();
                    if(snapshot.exists()){
                        for(DataSnapshot snapshot1: snapshot.getChildren()){
                                Customer c=snapshot1.getValue(Customer.class);
                                customerDataList.add(c);
                                customerListAdapter.notifyDataSetChanged();
                        }

                        calculateTotalMoney();
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void calculateTotalMoney() {
        int totalGetMoney=0,totalGivenMoney=0;
        for(int i=0; i<customerDataList.size(); i++){
            int getMoney=Integer.parseInt(customerDataList.get(i).getGetMoney());
            int givenMoney=Integer.parseInt(customerDataList.get(i).getGivenMoney());
            totalGetMoney=totalGetMoney+getMoney;
            totalGivenMoney=totalGivenMoney+givenMoney;
        }
        totalGetMoneyText.setText("আমি পাবো \n"+Extras.getdateInBangla(String.valueOf(totalGetMoney))+"\u09f3");
        totalGivenMoneyText.setText("দিতে হবে \n"+Extras.getdateInBangla(String.valueOf(totalGivenMoney))+"\u09f3");
    }

    private void sendUserToAddCustomerActivity() {
        Intent intent=new Intent(MainActivity.this,AddCustomerActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.main_logout_menu){
            mAuth.signOut();
            sendUserToLoginActivity();
        }



        return super.onOptionsItemSelected(item);
    }

    private void sendUserToLoginActivity() {
        Intent intent=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}