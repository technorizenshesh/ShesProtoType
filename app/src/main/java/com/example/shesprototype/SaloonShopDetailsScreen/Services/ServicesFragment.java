package com.example.shesprototype.SaloonShopDetailsScreen.Services;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.Services.Pakage.PakageFragment;

public class ServicesFragment extends Fragment {

    View rootView;
    Fragment fragment;
    LinearLayout LL_Pakage;
    RelativeLayout RR_servies;
    RelativeLayout RR_price;

    public static ServicesFragment newInstance(String param1, String param2) {
        ServicesFragment fragment = new ServicesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.services_fragment, container, false);

        LL_Pakage=(LinearLayout) rootView.findViewById(R.id.LL_Pakage) ;
        RR_servies=(RelativeLayout) rootView.findViewById(R.id.RR_servies) ;
        RR_price=(RelativeLayout) rootView.findViewById(R.id.RR_price) ;
        LL_Pakage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LL_Pakage.setBackgroundResource(R.color.Mehroon);
                RR_servies.setBackgroundResource(R.drawable.border_bg_one);
                RR_price.setBackgroundResource(R.drawable.border_bg_one);

               fragment =new PakageFragment();
                loadFragment(fragment);

            }
        });
        RR_servies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RR_servies.setBackgroundResource(R.color.Mehroon);
                LL_Pakage.setBackgroundResource(R.drawable.border_bg_one);
                RR_price.setBackgroundResource(R.drawable.border_bg_one);

                fragment =new  ServicesBookingFragment();
                loadFragment(fragment);
            }
        });

        RR_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* RR_servies.setBackgroundResource(R.color.Mehroon);
                LL_Pakage.setBackgroundResource(R.drawable.border_bg_one);
                RR_price.setBackgroundResource(R.drawable.border_bg_one);

                fragment =new  ServicesBookingFragment();
                loadFragment(fragment);*/
            }
        });

        loadFragment(new ServicesBookingFragment());

        return  rootView;
    }

    public void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_container, fragment);
        transaction.addToBackStack("chat");
        transaction.commit();
    }

}
