     package com.example.talikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private EditText firstNameEdittext,lastNameEdittext,phoneNumberEdittext;
    private  EditText emailEdittext,passwordEdittext;
    private Button registerButton;
    private TextView loginLink;

    //<-------------------Database--------------------->
    private DatabaseReference userRef;
    private FirebaseAuth mAuth;

    private  String firstName,lastName,phoneNumber,email,password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("tali");


        firstNameEdittext=findViewById(R.id.registerFirstNameEidttextid);
        lastNameEdittext=findViewById(R.id.registerLastNameEdittextid);
        phoneNumberEdittext=findViewById(R.id.registerPhoneNumberEdittextid);
        emailEdittext=findViewById(R.id.registerEmailEdittextid);
        passwordEdittext=findViewById(R.id.registerPasswordEdittextid);
        registerButton=findViewById(R.id.registerButtonid);
        loginLink=findViewById(R.id.loginActivityLink);


        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    sendUsertoLoginActivity();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    email=emailEdittext.getText().toString();
                    password=passwordEdittext.getText().toString();
                    firstName=firstNameEdittext.getText().toString();
                    lastName=lastNameEdittext.getText().toString();
                    phoneNumber=phoneNumberEdittext.getText().toString();


                    if(firstName.isEmpty()){
                        firstNameEdittext.setError("Please Enter Your First Name");
                        firstNameEdittext.requestFocus();
                    }
                    else if(lastName.isEmpty()){
                        lastNameEdittext.setError("Please Enter Your Last Name");
                        lastNameEdittext.requestFocus();
                    } else if(phoneNumber.isEmpty()){
                        phoneNumberEdittext.setError("Please Enter Your Phone Number");
                        phoneNumberEdittext.requestFocus();
                    }else if(password.isEmpty()){
                        passwordEdittext.setError("Please Enter Your Password");
                        passwordEdittext.requestFocus();
                    }else{
                        registerUser();
                    }
            }
        });






    }

    private void sendUsertoLoginActivity() {

        Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onStart() {
        super.onStart();
       FirebaseUser currentUser = mAuth.getCurrentUser();
       if(currentUser!=null){
           sendUserToMainActivity();
       }
    }
    private void registerUser() {
        CustomAlert alert=new CustomAlert(this,"Please Wait.while we are creating your account","Loading....");
        alert.showAlert();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){
                  HashMap<String,Object> userMap=new HashMap<>();
                  userMap.put("email",email);
                  userMap.put("firstName",firstName);
                  userMap.put("lastName",lastName);
                  userMap.put("phoneNumber",phoneNumber);
                  userMap.put("password",password);

                  String currentUserId=mAuth.getCurrentUser().getUid();



                  userRef.child("user").child(currentUserId).updateChildren(userMap)
                          .addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                         saveLocal();
                                        sendUserToMainActivity();
                                }else{
                                    Toast.makeText(RegisterActivity.this, "Account Create Failed", Toast.LENGTH_SHORT).show();
                                }
                              }
                          });






              }else{
                  Toast.makeText(RegisterActivity.this, "Something Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                  alert.cancelAlert();
              }
            }
        });


    }


    public void sendUserToMainActivity(){

        Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void saveLocal(){
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("firstName",firstName);
        editor.putString("lastName",lastName);
        editor.putString("email",email);
        editor.commit();
    }


}