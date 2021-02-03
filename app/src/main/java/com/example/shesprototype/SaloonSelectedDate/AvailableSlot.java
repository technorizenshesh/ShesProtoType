package com.example.shesprototype.SaloonSelectedDate;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shesprototype.R;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class AvailableSlot extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<String> modelList;
    private OnItemClickListener mItemClickListener;

    int row_index=1;

    public AvailableSlot(Context context, ArrayList<String> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<String> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_avalible_slot, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final String model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

           genericViewHolder.txt_time.setText(modelList.get(position));

           genericViewHolder.txt_time.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   row_index=position;
                   notifyDataSetChanged();

               }
           });

            if(row_index==position){

                SaloonSelectedDate.Time = modelList.get(position);

               genericViewHolder.LL_time_day.setBackgroundResource(R.drawable.border_bg);
               genericViewHolder.RR_main.setBackgroundResource(R.color.colorAccentRed);
                genericViewHolder.txt_time.setTextColor(ContextCompat.getColor(mContext, R.color.white));


            }else
           {

               genericViewHolder.LL_time_day.setBackgroundResource(R.drawable.border_bg);
               genericViewHolder.RR_main.setBackgroundResource(R.color.colorAccent_new);

               genericViewHolder.txt_time.setTextColor(ContextCompat.getColor(mContext, R.color.light_black));


           }

           // String imageUrl = model.getImage().toString();
        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private String getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, String model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView user_profile;
        private TextView txt_name;
        private TextView txt_time;
        private LinearLayout LL_time_day;
        private RelativeLayout RR_main;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txt_time=itemView.findViewById(R.id.txt_time);
            this.LL_time_day=itemView.findViewById(R.id.LL_time_day);
            this.RR_main=itemView.findViewById(R.id.RR_main);
            //this.txt_name=itemView.findViewById(R.id.txt_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

