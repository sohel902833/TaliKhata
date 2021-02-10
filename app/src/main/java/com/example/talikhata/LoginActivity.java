package com.example.talikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {




    private TextView registerActivityLink,autoPhoneTextview,autoEmailTextview,loginAnotherTextviwe;
    private EditText emailEdittext,passwordEdittext;
    private Button loginButton;



    String prevPhoneNumber,prevEmail;



    boolean prevLogin=true;



    //<=-----------Database---------->
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference().child("tali");



        autoPhoneTextview=findViewById(R.id.login_autoPhoneTextviewid);
        autoEmailTextview=findViewById(R.id.login_autoEmailTextviewid);
        loginAnotherTextviwe=findViewById(R.id.login_loginAnotherAccountTextviewid);
        registerActivityLink=findViewById(R.id.registerActivityLink);
        emailEdittext=findViewById(R.id.login_EmailEdittextid);
        passwordEdittext=findViewById(R.id.login_PasswordEdittextid);
        loginButton=findViewById(R.id.login_LoginButtonid);



        UserData userData=new UserData(LoginActivity.this);
        prevEmail=userData.getEmail();
        prevPhoneNumber=userData.getPhoneNumber();


        if(prevEmail.equals("") || prevPhoneNumber.equals("")){
            emailEdittext.setVisibility(View.VISIBLE);
            autoPhoneTextview.setVisibility(View.GONE);
            autoEmailTextview.setVisibility(View.GONE);
            loginAnotherTextviwe.setVisibility(View.GONE);
            prevLogin=false;
        }else{
            autoPhoneTextview.setText("Phone: "+prevPhoneNumber);
            autoEmailTextview.setText("Email: "+prevEmail);
        }

        registerActivityLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToLoginActivity();
            }
        });

        loginAnotherTextviwe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEdittext.setVisibility(View.VISIBLE);
                autoPhoneTextview.setVisibility(View.GONE);
                autoEmailTextview.setVisibility(View.GONE);
                loginAnotherTextviwe.setVisibility(View.GONE);
                prevLogin=false;
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=passwordEdittext.getText().toString();
                if (password.isEmpty()){
                    passwordEdittext.setError("PLease Type Your Password");
                    passwordEdittext.requestFocus();
                }else{
                    if(prevLogin==true){
                        loginUser(prevEmail,password);
                    }else{
                        String email=emailEdittext.getText().toString();
                        if(email.isEmpty()){
                            emailEdittext.setError("Enter Your Email");
                            emailEdittext.requestFocus();
                        }else {
                            loginUser(email,password);

                        }
                    }
                }


            }
        });
    }

    private void sendUserToLoginActivity() {
        Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
        finish();
    }


    public void loginUser(String email,String password){
        CustomAlert alert=new CustomAlert(this,"Please Wait.","Logging in....");
        alert.showAlert();
           mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String currentUserId=mAuth.getCurrentUser().getUid();
                        userRef.child("user").child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String email=snapshot.child("email").getValue().toString();
                                        String firstName=snapshot.child("firstName").getValue().toString();
                                        String lastName=snapshot.child("lastName").getValue().toString();
                                        String phoneNumber=snapshot.child("phoneNumber").getValue().toString();
                                        saveLocal(email,firstName,lastName,phoneNumber);
                                        sendUserToMainActivity();
                                        alert.cancelAlert();
                                   }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }else{
                        alert.cancelAlert();
                        Toast.makeText(LoginActivity.this, "Login Failed .."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
               }
           });
    }
    public void sendUserToMainActivity(){

        Intent intent=new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
    public void saveLocal(String email,String firstName,String lastName,String phoneNumber){
        SharedPreferences sharedPreferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("phoneNumber",phoneNumber);
        editor.putString("firstName",firstName);
        editor.putString("lastName",lastName);
        editor.putString("email",email);
        editor.commit();
    }



}