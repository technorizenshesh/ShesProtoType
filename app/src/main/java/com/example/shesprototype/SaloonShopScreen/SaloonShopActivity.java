package com.example.shesprototype.SaloonShopScreen;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.example.shesprototype.GPSTracker;
import com.example.shesprototype.Preference;
import com.example.shesprototype.ProductFilterScreen.FilterProductActivity;
import com.example.shesprototype.R;
import com.example.shesprototype.SaloonShopDetailsScreen.SaloonShopDetailsActivity;
import com.example.shesprototype.SaloonShopScreen.ApiModel.SaloonShoDataModel;
import com.example.shesprototype.SaloonShopScreen.ApiModel.SaloonShopModel;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SaloonShopActivity extends AppCompatActivity implements IApiResponse {

    private RecyclerView recycler_view_saloon_ProductList;
    private SaloonShopRecyclerViewAdapter mAdapter;
    private ArrayList<SaloonShoDataModel> modelList = new ArrayList<>();
    private RelativeLayout RR_back;
    private RelativeLayout RR_filter;
    private TextView txt_empty;

    private Preference preference;
    private ProgressBar progressBar;
    String android_id ="";

    GPSTracker gpsTracker;
    String latitude ="";
    String longitude ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saloon_product);

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

        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RR_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(SaloonShopActivity.this, FilterProductActivity.class);
                startActivity(intent);

            }
        });

      String CategoryId =  Preference.get(SaloonShopActivity.this,Preference.KEY_CategoryId);

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getHomeSaloon(CategoryId);

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }


        gpsTracker=new GPSTracker(this);

        if(gpsTracker.canGetLocation()){

            latitude = String.valueOf(gpsTracker.getLatitude());
            longitude = String.valueOf(gpsTracker.getLongitude());

        }else{
            gpsTracker.showSettingsAlert();
        }

    }

    private void findview() {
        recycler_view_saloon_ProductList=findViewById(R.id.recycler_view_saloon_ProductList);
        RR_back=findViewById(R.id.RR_back);
        RR_filter=findViewById(R.id.RR_filter);
        progressBar=findViewById(R.id.progressBar);
        txt_empty=findViewById(R.id.txt_empty);
    }


    private void setAdapter(ArrayList<SaloonShoDataModel> modelList) {
/*
        modelList.add(new SaloonShopListModel("John Smith","4.3",R.drawable.haircut_img));
        modelList.add(new SaloonShopListModel("Olivia Parker","3.3",R.drawable.saloon_img));
        modelList.add(new SaloonShopListModel("John Smith","4.6",R.drawable.haircut_img));*/

        mAdapter = new SaloonShopRecyclerViewAdapter(SaloonShopActivity.this, this.modelList);
        recycler_view_saloon_ProductList.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SaloonShopActivity.this);
        recycler_view_saloon_ProductList.setLayoutManager(linearLayoutManager);
        recycler_view_saloon_ProductList.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new SaloonShopRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SaloonShoDataModel model) {

                Preference.save(SaloonShopActivity.this,Preference.KEY_salon_id,model.getId());
                Preference.save(SaloonShopActivity.this,Preference.KEY_salon_User_id,model.getUserId());

                Intent intent=new Intent(SaloonShopActivity.this, SaloonShopDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getHomeSaloon(String category_id){

        HashMap<String, String> map = new HashMap<>();

        map.put("category_id",category_id);
        map.put("lat",latitude);
        map.put("lon",longitude);

        ApiRequest apiRequest = new ApiRequest(SaloonShopActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_categorySaloon, Constants.USER_get_categorySaloon,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_categorySaloon.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {
                    txt_empty.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        SaloonShopModel finalArray = new Gson().fromJson(response,new TypeToken<SaloonShopModel>(){}.getType());

                         modelList = (ArrayList<SaloonShoDataModel>) finalArray.getResult();

                        setAdapter(modelList);

                    }else
                    {
                        txt_empty.setVisibility(View.VISIBLE);
                       // Toast.makeText(this, "Data Not Found", Toast.LENGTH_SHORT).show();
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
