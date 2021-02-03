package com.example.shesprototype.SaloonShopGender;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonSelectedDate.SaloonSelectedDate;
import com.example.shesprototype.SaloonShopScreen.ApiModel.SaloonShopModel;
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

public class SaloonshopGender extends AppCompatActivity implements IApiResponse {

    private Button btn_book_now;

    RecyclerView recycler_view_service;
    private SaloonServicesRecyclerViewAdapter mAdapter;
    private ArrayList<ServiceDetailModel> modelList = new ArrayList<>();

    private Preference preference;
    private ProgressBar progressBar;
    private String android_id ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloonshop_gender);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.black1));
        }

        findview();

        //android device Id
        android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        preference = new Preference(this);


        btn_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SaloonshopGender.this, SaloonSelectedDate.class);
                     startActivity(intent);
            }
        });

        String CategoryId =  Preference.get(SaloonshopGender.this,Preference.KEY_CategoryId);

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getServices(CategoryId);

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }
    }

    private void findview() {
        recycler_view_service=findViewById(R.id.recycler_view_service);
        progressBar=findViewById(R.id.progressBar);
        btn_book_now=findViewById(R.id.btn_book_now);
    }

    private void setAdapter(ArrayList<ServiceDetailModel> modelList) {

        mAdapter = new SaloonServicesRecyclerViewAdapter(SaloonshopGender.this, modelList);
        recycler_view_service.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SaloonshopGender.this);
        recycler_view_service.setLayoutManager(linearLayoutManager);
        recycler_view_service.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new SaloonServicesRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ServiceDetailModel model) {

            }
        });
    }


    public void getServices(String category_id){

        HashMap<String, String> map = new HashMap<>();

        map.put("category_id",category_id);

        ApiRequest apiRequest = new ApiRequest(SaloonshopGender.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_categorySaloon, Constants.USER_get_categorySaloon,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_categorySaloon.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {

                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {

                        SaloonShopModel finalArray = new Gson().fromJson(response,new TypeToken<SaloonShopModel>(){}.getType());

                        modelList = (ArrayList<ServiceDetailModel>) finalArray.getResult().get(0).getServiceDetails();

                                setAdapter(modelList);

                    }else
                    {
                        Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this, "Please Check Your Network", Toast.LENGTH_SHORT).show();
    }


}
