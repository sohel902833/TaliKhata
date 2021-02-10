package com.example.talikhata.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talikhata.DataModuler.Bill;
import com.example.talikhata.DataModuler.Customer;
import com.example.talikhata.Extras;
import com.example.talikhata.R;

import java.util.ArrayList;
import java.util.List;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.MyViewHolder> {

    private Context context;
    private List<Bill> billList =new ArrayList<>();
    OnItemClickListner listner;

    public BillAdapter(Context context, List<Bill> billList) {
        this.context = context;
        this.billList = billList;
    }

    @NonNull
    @Override
    public BillAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.bill_details_item_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillAdapter.MyViewHolder holder, int position) {
        Bill currentData=billList.get(position);

        holder.dateText.setText(currentData.getDate()+"\n At "+currentData.getTime());
        holder.detailsTextview.setText(currentData.getDetails()+"নিয়েছিলেন");
        holder.getMoneyTextview.setText("দিয়েছিলাম \n"+currentData.getGetMoney());
        holder.givenMoneyTextview.setText("পেয়েছিলাম \n"+currentData.getGivenMoney());


    }

    @Override
    public int getItemCount() {
        return billList.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {

        TextView dateText,getMoneyTextview,givenMoneyTextview,detailsTextview;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            getMoneyTextview=itemView.findViewById(R.id.bilDetails_GetMoneyTextViewid);
            givenMoneyTextview=itemView.findViewById(R.id.billDetails_givenMoneyTextviewid);
            detailsTextview=itemView.findViewById(R.id.billDetails_DetailsTextviewid);
            dateText=itemView.findViewById(R.id.billDetails_DateTextviewid);
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
