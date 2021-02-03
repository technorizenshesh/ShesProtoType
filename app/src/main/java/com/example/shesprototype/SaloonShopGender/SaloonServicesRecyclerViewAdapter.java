package com.example.shesprototype.SaloonShopGender;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopScreen.ApiModel.ServiceDetailModel;

import java.util.ArrayList;

/**
 * A custom adapter to use with the RecyclerView widget.
 */
public class SaloonServicesRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    int pos = 0;
    private Context mContext;
    private ArrayList<ServiceDetailModel> modelList;
    private OnItemClickListener mItemClickListener;

    public SaloonServicesRecyclerViewAdapter(Context context, ArrayList<ServiceDetailModel> modelList) {
        this.mContext = context;
        this.modelList = modelList;
    }

    public void updateList(ArrayList<ServiceDetailModel> modelList) {
        this.modelList = modelList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_saloon_service, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        //Here you can fill your row view
        if (holder instanceof ViewHolder) {
            final ServiceDetailModel model = getItem(position);
            final ViewHolder genericViewHolder = (ViewHolder) holder;

           genericViewHolder.txt_hair_cut.setText(model.getName());
          //genericViewHolder.edt_bridal.setText(model.getDescription()+"  ($ "+model.getPrice()+" )");
          genericViewHolder.edt_bridal.setText("$ "+model.getPrice());
           //  genericViewHolder.txt_post.setText(model.getPost());
            genericViewHolder.checl_item.setEnabled(false);
            genericViewHolder.RR_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setSelected(!model.isSelected());

                    if(model.isSelected())
                    {
                        genericViewHolder.checl_item.setChecked(true); //to check

                    }else
                    {
                        genericViewHolder.checl_item.setChecked(false);
                    }
                 //   genericViewHolder.checl_item.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
                }
            });

            genericViewHolder.edt_bridal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    model.setSelected(!model.isSelected());

                    if(model.isSelected())
                    {
                        genericViewHolder.checl_item.setChecked(true); //to check

                    }else
                    {
                        genericViewHolder.checl_item.setChecked(false);
                    }
                 //   genericViewHolder.checl_item.setBackgroundColor(model.isSelected() ? Color.CYAN : Color.WHITE);
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

    private ServiceDetailModel getItem(int position) {
        return modelList.get(position);
    }


    public interface OnItemClickListener {

        void onItemClick(View view, int position, ServiceDetailModel model);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_img;
        private TextView txt_hair_cut;
        private TextView edt_bridal;
        private RelativeLayout RR_select;
        private CheckBox checl_item;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.txt_hair_cut=itemView.findViewById(R.id.txt_hair_cut);
            this.edt_bridal=itemView.findViewById(R.id.edt_bridal);
            this.RR_select=itemView.findViewById(R.id.RR_select);
            this.checl_item=itemView.findViewById(R.id.checl_item);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onItemClick(itemView, getAdapterPosition(), modelList.get(getAdapterPosition()));
                }
            });
        }
    }



}

