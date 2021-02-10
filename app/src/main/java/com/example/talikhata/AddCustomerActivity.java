package com.example.talikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.talikhata.DataModuler.Customer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCustomerActivity extends AppCompatActivity {

    private EditText customerNameEdittext,customerPhoneEdittxt,prevGetMoneyEdittext,detailsEdittext;
    private RadioGroup radioGroup;

    private Button addCustomerButton;


    String customerName,customerPhone,prevMoney="",details="",customerType,currentUserid;


    //<-----------Firebase----------------------->



    private DatabaseReference userRef;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);

        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("tali").child("user").child(currentUserid);


        customerNameEdittext=findViewById(R.id.addCustomer_CustomerNameEditext);
        customerPhoneEdittxt=findViewById(R.id.addCustomer_MobileNumberEdittext);
        prevGetMoneyEdittext=findViewById(R.id.addCustomer_previousGetMoney);
        detailsEdittext=findViewById(R.id.addCustomer_DetailsEditText);
        radioGroup=findViewById(R.id.rd1);
        addCustomerButton=findViewById(R.id.addCustomerButton);




        addCustomerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 customerName=customerNameEdittext.getText().toString();
                 customerPhone=customerPhoneEdittxt.getText().toString();
                 prevMoney=prevGetMoneyEdittext.getText().toString();
                 details=detailsEdittext.getText().toString();
                 int selectedId=radioGroup.getCheckedRadioButtonId();
                 RadioButton genderButton=findViewById(selectedId);
                 customerType=genderButton.getText().toString();

                 if(customerName.isEmpty()){
                     customerNameEdittext.setError("কাস্টমার নাম প্রদান করুন");
                     customerNameEdittext.requestFocus();
                 }else if(customerPhone.isEmpty()){
                     customerPhoneEdittxt.setError("কাস্টমার ফোন নাম্বার প্রদান করুন");
                     customerPhoneEdittxt.requestFocus();
                 }else{
                   saveCustomer();
                 }
            }
        });
    }

    private void saveCustomer() {
        String key =userRef.push().getKey();
        Customer customer=new Customer(key,customerName,customerType,prevMoney,"");

        userRef.child("userData").child(key)
                    .setValue(customer)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                                startActivity(new Intent(AddCustomerActivity.this,MainActivity.class));
                                        }else{
                                            Toast.makeText(AddCustomerActivity.this, "customer Create Failed", Toast.LENGTH_SHORT).show();
                                        }
                                }
                            });
    }
}