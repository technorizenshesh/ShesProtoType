package com.example.shesprototype.SaloonShopDetailsScreen.Abouts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.SaloonDetails;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AboutsFragment extends Fragment implements IApiResponse {

    View rootView;
   private TextView txt_about;
   private TextView txt_address;

   private TextView txt_monday;
   private TextView txt_tuesday;
   private TextView txt_wednesday;
   private TextView txt_thrsday;
   private TextView txt_friday;
   private TextView txt_saturday;
   private TextView txt_sunday;
   private TextView txt_Off;

    private Preference preference;


    public static AboutsFragment newInstance(String param1, String param2) {
        AboutsFragment fragment = new AboutsFragment();
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
        rootView = inflater.inflate(R.layout.activity_allfrnd_fragment, container, false);

        txt_monday = rootView.findViewById(R.id.txt_monday);
        txt_tuesday = rootView.findViewById(R.id.txt_tuesday);
        txt_wednesday = rootView.findViewById(R.id.txt_wednesday);
        txt_thrsday = rootView.findViewById(R.id.txt_thrsday);
        txt_friday = rootView.findViewById(R.id.txt_friday);
        txt_saturday = rootView.findViewById(R.id.txt_saturday);
        txt_sunday = rootView.findViewById(R.id.txt_sunday);
        txt_about = rootView.findViewById(R.id.txt_about);
        txt_address = rootView.findViewById(R.id.txt_address);
        txt_Off = rootView.findViewById(R.id.txt_Off);

        String salon_id =  Preference.get(getActivity(),Preference.KEY_salon_id);

        preference = new Preference(getActivity());

        if (preference.isNetworkAvailable()) {

            getHomeSaloon(salon_id);

        }else {

            Toast.makeText(getActivity(), R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        return  rootView;
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

                       String ShopOff = finalArray.getResult().getUsersDetails().getWeeklyClose();
                        txt_about.setText(finalArray.getResult().getAboutInfo());
                       txt_monday.setText(finalArray.getResult().getUsersDetails().getMondayOpen()+"  -  " +finalArray.getResult().getUsersDetails().getMondayClose());
                       txt_tuesday.setText(finalArray.getResult().getUsersDetails().getTuesdayOpen()+"  -  "+finalArray.getResult().getUsersDetails().getTuesdayClose());
                       txt_wednesday.setText(finalArray.getResult().getUsersDetails().getWednesdayOpen()+"  -  "+finalArray.getResult().getUsersDetails().getWednesdayClose());
                       txt_thrsday.setText(finalArray.getResult().getUsersDetails().getThursdayOpen()+"  -  "+finalArray.getResult().getUsersDetails().getThursdayClose());
                       txt_friday.setText(finalArray.getResult().getUsersDetails().getFridayOpen()+"  -  "+finalArray.getResult().getUsersDetails().getFridayClose());
                        txt_saturday.setText(finalArray.getResult().getUsersDetails().getSaturdayOpen()+"  -  "+finalArray.getResult().getUsersDetails().getSaturdayClose());
                        txt_sunday.setText(finalArray.getResult().getUsersDetails().getSaturdayOpen()+"  -  "+finalArray.getResult().getUsersDetails().getSaturdayClose());
                        txt_Off.setText(ShopOff);

                        txt_address.setText(finalArray.getResult().getUsersDetails().getAddress());

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
