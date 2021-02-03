package com.example.shesprototype.SaloonShopDetailsScreen.Services.Pakage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.shesprototype.R;

public class PakageFragment extends Fragment {

    View rootView;

    public static PakageFragment newInstance(String param1, String param2) {
        PakageFragment fragment = new PakageFragment();
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
        rootView = inflater.inflate(R.layout.pakage_fragment, container, false);

        return  rootView;
    }

}
