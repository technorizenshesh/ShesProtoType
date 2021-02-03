package com.example.shesprototype.SaloonShopDetailsScreen.Gallary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.SaloonDetails;
import com.example.shesprototype.SaloonShopScreen.ApiModel.GalleryOne;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class GallaryFragment extends Fragment implements IApiResponse{

    View rootView;
    RecyclerView recycler_view_gallery;
    private GallaryRecyclerViewAdapter mAdapter;
    private ArrayList<GalleryOne> modelList = new ArrayList<>();

    private Preference preference;

    public static GallaryFragment newInstance(String param1, String param2) {
        GallaryFragment fragment = new GallaryFragment();
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
        rootView = inflater.inflate(R.layout.gallary_fragment, container, false);

        recycler_view_gallery=rootView.findViewById(R.id.recycler_view_gallery);

        String salon_id =  Preference.get(getActivity(),Preference.KEY_salon_id);

        preference = new Preference(getActivity());

        if (preference.isNetworkAvailable()) {

            getHomeSaloon(salon_id);

        }else {

            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        return  rootView;
    }

    private void setAdapter(ArrayList<GalleryOne> modelList) {

        mAdapter = new GallaryRecyclerViewAdapter(getActivity(),modelList);
        recycler_view_gallery.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_view_gallery.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recycler_view_gallery.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new GallaryRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, GalleryOne model) {

            }
        });
    }

    public void getHomeSaloon(String salon_id){

        HashMap<String, String> map = new HashMap<>();

        map.put("salon_id",salon_id);

        ApiRequest apiRequest = new ApiRequest(getActivity(),this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_salon_detaild, Constants.USER_get_salon_detaild,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_salon_detaild.equalsIgnoreCase(tag_json_obj)){

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {

                        SaloonDetails finalArray = new Gson().fromJson(response,new TypeToken<SaloonDetails>(){}.getType());

                        modelList.add(new GalleryOne(finalArray.getResult().getImage().toString()));
                        modelList.add(new GalleryOne(finalArray.getResult().getImage1().toString()));
                        modelList.add(new GalleryOne(finalArray.getResult().getImage2().toString()));
                        modelList.add(new GalleryOne(finalArray.getResult().getImage3().toString()));
                        modelList.add(new GalleryOne(finalArray.getResult().getImage4().toString()));
                        modelList.add(new GalleryOne(finalArray.getResult().getImage5().toString()));
                        modelList.add(new GalleryOne(finalArray.getResult().getImage6().toString()));

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
        Toast.makeText(getActivity(), "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }


}
