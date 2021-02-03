package com.example.shesprototype.SaloonShopDetailsScreen.Services;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonSelectedDate.SaloonSelectedDate;
import com.example.shesprototype.SaloonShopDetailsScreen.SaloonDetails;
import com.example.shesprototype.SaloonShopGender.SaloonServicesRecyclerViewAdapter;
import com.example.shesprototype.SaloonShopScreen.ApiModel.ServiceDetailModel;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServicesBookingFragment extends Fragment implements IApiResponse {

    View rootView;
    private Button btn_book_now1;

    RecyclerView recycler_view_service;
    private SaloonServicesRecyclerViewAdapter mAdapter;
    private ArrayList<ServiceDetailModel> modelList = new ArrayList<>();

    private Preference preference;
    private ProgressBar progressBar;
    private String android_id ="";


    public static ServicesBookingFragment newInstance(String param1, String param2) {
        ServicesBookingFragment fragment = new ServicesBookingFragment();
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
        rootView = inflater.inflate(R.layout.services_fragment_booking, container, false);
        btn_book_now1=rootView.findViewById(R.id.btn_book_now1);
        recycler_view_service=rootView.findViewById(R.id.recycler_view_service);
        progressBar=rootView.findViewById(R.id.progressBar);
        btn_book_now1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // Intent intent=new Intent(getActivity(), SaloonshopGender.class);
                Intent intent=new Intent(getActivity(), SaloonSelectedDate.class);
                startActivity(intent);
            }
        });

        //android device Id
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        preference = new Preference(getActivity());
        String salon_id =  Preference.get(getActivity(),Preference.KEY_salon_id);

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getServices(salon_id);

        }else {

            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

        return  rootView;
    }

    public void getServices(String salon_id){

        HashMap<String, String> map = new HashMap<>();

        map.put("salon_id",salon_id);

        ApiRequest apiRequest = new ApiRequest(getActivity(),this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_salon_detaild, Constants.USER_get_salon_detaild,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_salon_detaild.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {

                        SaloonDetails finalArray = new Gson().fromJson(response,new TypeToken<SaloonDetails>(){}.getType());

                        modelList = (ArrayList<ServiceDetailModel>) finalArray.getResult().getServiceDetails();

                        setAdapter(modelList);

                    }else
                    {
                        Toast.makeText(getActivity(), "Data Not Found", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }


    private void setAdapter(ArrayList<ServiceDetailModel> modelList) {

        mAdapter = new SaloonServicesRecyclerViewAdapter(getActivity(), modelList);
        recycler_view_service.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view_service.setLayoutManager(linearLayoutManager);
        recycler_view_service.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new SaloonServicesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ServiceDetailModel model) {

            }
        });
    }

}
