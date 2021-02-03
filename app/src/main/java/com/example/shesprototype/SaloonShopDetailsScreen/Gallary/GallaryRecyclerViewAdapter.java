package com.example.shesprototype.SaloonShopDetailsScreen.Gallary;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopScreen.ApiModel.GalleryOne;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class GallaryRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<GalleryOne> modelList;
    private OnItemClickListener mItemClickListener;

    public GallaryRecyclerViewAdapter(Context context, ArrayList<GalleryOne> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<GalleryOne> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_saloon_galary, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final GalleryOne model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            String imageUrl = model.getImage().toString();

            if(!imageUrl.equalsIgnoreCase(""))
            {
                Glide.with(mContext).load(imageUrl).placeholder(R.mipmap.eyelashes)
                        .into(genericViewHolder.product_img);
            }
          //genericViewHolder.user_profile.setImageResource(model.getImg());
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

    private GalleryOne getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, GalleryOne model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_img;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.product_img=itemView.findViewById(R.id.product_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

