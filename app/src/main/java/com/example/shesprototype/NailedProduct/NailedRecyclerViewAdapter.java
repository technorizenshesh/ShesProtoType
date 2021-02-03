package com.example.shesprototype.NailedProduct;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shesprototype.R;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class NailedRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<getHomeDataModel> modelList;
    private OnItemClickListener mItemClickListener;

    public NailedRecyclerViewAdapter(Context context, ArrayList<getHomeDataModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<getHomeDataModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_productlist, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final getHomeDataModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

            String ImageUrl = model.getImage().toString();

            Glide.with(mContext).load(ImageUrl).placeholder(R.mipmap.eyelashes)
                    .into(genericViewHolder.product_img);
        // genericViewHolder.product_img.setImageResource(model.getImg());
        genericViewHolder.txt_product_name.setText(model.getCategoryName());

        }
    }


    @Override
    public int getItemCount() {

        return modelList.size();
    }

   public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    private getHomeDataModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, getHomeDataModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_img;
        private TextView txt_product_name;




        public ViewHolder(final View itemView) {
            super(itemView);

            this.product_img=itemView.findViewById(R.id.product_img);
            this.txt_product_name=itemView.findViewById(R.id.txt_product_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

