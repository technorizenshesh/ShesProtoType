package com.example.shesprototype.SaloonShopScreen;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.SaloonShopDetailsActivity;
import com.example.shesprototype.SaloonShopScreen.ApiModel.SaloonShoDataModel;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class SaloonShopRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<SaloonShoDataModel> modelList;
    private OnItemClickListener mItemClickListener;

    public SaloonShopRecyclerViewAdapter(Context context, ArrayList<SaloonShoDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<SaloonShoDataModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_saloon_productlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final SaloonShoDataModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

         //genericViewHolder.haircut_img.setImageResource(model.getImg());

            String imageUrl = model.getUsersDetails().getImage().toString();

            Glide.with(mContext).load(imageUrl).placeholder(R.drawable.haircut_img)
                    .into(genericViewHolder.haircut_img);

         genericViewHolder.product_name.setText(model.getUsersDetails().getUserName());
          genericViewHolder.txt_view.setText(model.getRating());
          genericViewHolder.address.setText(model.getUsersDetails().getAddress());
          genericViewHolder.txt_distance.setText("Distance :"+model.getDistance());
          genericViewHolder.RR_view.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {

                  Preference.save(mContext,Preference.KEY_salon_id,model.getId());

                  Intent intent=new Intent(mContext, SaloonShopDetailsActivity.class);
                  mContext.startActivity(intent);
              }
          });

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

   public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private SaloonShoDataModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, SaloonShoDataModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView haircut_img;
        private TextView product_name;
        private TextView txt_view;
        private TextView address;
        private TextView txt_distance;
        private Button RR_view;




        public ViewHolder(final View itemView) {
            super(itemView);

            this.haircut_img=itemView.findViewById(R.id.haircut_img);
             this.product_name=itemView.findViewById(R.id.product_name);
             this.txt_view=itemView.findViewById(R.id.txt_view);
             this.txt_distance=itemView.findViewById(R.id.txt_distance);
             this.address=itemView.findViewById(R.id.address);
             this.RR_view=itemView.findViewById(R.id.RR_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

