package com.example.talikhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.talikhata.Adapter.BillAdapter;
import com.example.talikhata.DataModuler.Bill;
import com.example.talikhata.DataModuler.Customer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerBillDetailsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Bill> billList=new ArrayList<>();
    private BillAdapter billAdapter;


    private  String customerid,currentUserid;

    private FirebaseAuth mAuth;
    private DatabaseReference billRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_bill_details);

        customerid=getIntent().getStringExtra("customerId");

        mAuth=FirebaseAuth.getInstance();
        currentUserid=mAuth.getCurrentUser().getUid();

        billRef= FirebaseDatabase.getInstance().getReference().child("tali").child("user").child(currentUserid).child("billDetails");



        recyclerView=findViewById(R.id.bilDetailsRecyclerviewid);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        billAdapter=new BillAdapter(this,billList);
        recyclerView.setAdapter(billAdapter);







    }


    @Override
    protected void onStart() {
        super.onStart();

        billRef.child(customerid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    billList.clear();
                    for(DataSnapshot snapshot1: snapshot.getChildren()){
                        Bill c=snapshot1.getValue(Bill.class);
                        billList.add(c);
                        billAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }



}