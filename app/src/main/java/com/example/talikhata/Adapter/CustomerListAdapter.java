package com.example.talikhata.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talikhata.DataModuler.Customer;
import com.example.talikhata.Extras;
import com.example.talikhata.R;

import java.util.ArrayList;
import java.util.List;

public class CustomerListAdapter extends RecyclerView.Adapter<CustomerListAdapter.MyViewHolder> {

    private Context context;
    private List<Customer> customerDataList =new ArrayList<>();
    OnItemClickListner listner;

    public CustomerListAdapter(Context context, List<Customer> customerDataList) {
        this.context = context;
        this.customerDataList = customerDataList;
    }

    @NonNull
    @Override
    public CustomerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.customer_list_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerListAdapter.MyViewHolder holder, int position) {
        Customer currentData=customerDataList.get(position);

        holder.nameTextview.setText(currentData.getName());

        String getMoney=currentData.getGetMoney();
        String givenMoney=currentData.getGivenMoney();

        if(Integer.parseInt(getMoney)==0 && Integer.parseInt(givenMoney)==0){
            holder.getMoneyTextview.setText(0+"\u09F3");
        }else if(Integer.parseInt(getMoney)>0){
            holder.getMoneyTextview.setText("আমি পাবো \n"+ Extras.getdateInBangla(String.valueOf(getMoney))+"\u09F3");
        }else if(Integer.parseInt(givenMoney)>0){
            holder.getMoneyTextview.setText("সে পাবে \n"+ Extras.getdateInBangla(String.valueOf(givenMoney))+"\u09F3");
        }




        holder.symbleTextview.setText(currentData.getName().substring(0,1));
        holder.customerTypeTextview.setText(currentData.getType());




    }

    @Override
    public int getItemCount() {
        return customerDataList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView nameTextview,customerTypeTextview,symbleTextview,getMoneyTextview;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nameTextview=itemView.findViewById(R.id.customer_list_NameTextviewid);
            customerTypeTextview=itemView.findViewById(R.id.customer_list_CustomerTypeTextviewid);
            symbleTextview=itemView.findViewById(R.id.customer_list_SymbleTextviewid);
            getMoneyTextview=itemView.findViewById(R.id.customer_list_PriceTextviewid);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(listner!=null){
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION){
                    listner.onItemClick(position);
                }
            }
        }
    }



    public interface OnItemClickListner{
        void onItemClick(int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }


}
