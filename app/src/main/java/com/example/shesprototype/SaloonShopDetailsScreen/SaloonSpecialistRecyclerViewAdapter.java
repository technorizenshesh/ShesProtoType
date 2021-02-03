package com.example.shesprototype.SaloonShopDetailsScreen;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopScreen.ApiModel.UsersDetails;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class SaloonSpecialistRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<UsersDetails> modelList;
    private OnItemClickListener mItemClickListener;

    public SaloonSpecialistRecyclerViewAdapter(Context context, ArrayList<UsersDetails> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<UsersDetails> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_saloon_specialist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final UsersDetails model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

        //    genericViewHolder.user_profile.setImageResource(model.getImg());

            String imageUrl = model.getImage().toString();

            Glide.with(mContext).load(imageUrl).placeholder(R.drawable.haircut_img)
                    .into(genericViewHolder.user_profile);
           // genericViewHolder.txt_name.setText(model.getUserName());
          //  genericViewHolder.txt_post.setText(model.getPost());

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private UsersDetails getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, UsersDetails model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView user_profile;
        private TextView txt_name;
        private TextView txt_post;




        public ViewHolder(final View itemView) {
            super(itemView);

            this.user_profile=itemView.findViewById(R.id.user_profile);
            this.txt_name=itemView.findViewById(R.id.txt_name);
            this.txt_post=itemView.findViewById(R.id.txt_post);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

