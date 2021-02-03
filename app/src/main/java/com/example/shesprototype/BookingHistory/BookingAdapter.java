package com.example.shesprototype.BookingHistory;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shesprototype.R;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class BookingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<BookingDataModel> modelList;
    private OnItemClickListener mItemClickListener;

    public BookingAdapter(Context context, ArrayList<BookingDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<BookingDataModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_booked, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final BookingDataModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

         genericViewHolder.txt_name.setText(model.getUserName());
         genericViewHolder.txt_order_id.setText(model.getOrderId());
         genericViewHolder.txt_date.setText(model.getDate());
         genericViewHolder.txt_time.setText(model.getTime());
         genericViewHolder.txt_amount.setText(model.getAmount());
         //genericViewHolder.txt_address.setText(model.e());

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

   public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private BookingDataModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, BookingDataModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txt_name;
        private TextView txt_order_id;
        private TextView txt_date;
        private TextView txt_time;
        private TextView txt_address;
        private TextView txt_amount;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txt_name=itemView.findViewById(R.id.txt_name);
            this.txt_order_id=itemView.findViewById(R.id.txt_order_id);
            this.txt_date=itemView.findViewById(R.id.txt_date);
            this.txt_time=itemView.findViewById(R.id.txt_time);
            this.txt_address=itemView.findViewById(R.id.txt_address);
            this.txt_amount=itemView.findViewById(R.id.txt_amount);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

