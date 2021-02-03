package com.example.shesprototype.BookingHistory;

import android.os.Build;
import android.os.Bundle;
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
import com.example.shesprototype.Preference;
import com.example.shesprototype.R;
import com.example.shesprototype.Volley.ApiRequest;
import com.example.shesprototype.Volley.Constants;
import com.example.shesprototype.Volley.IApiResponse;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BookingHistoryActivity extends AppCompatActivity implements IApiResponse {

    private RecyclerView recycler_booking_history;
    private Preference preference;
    private ProgressBar progressBar;
    private TextView txt_empty;
    private BookingAdapter mAdapter;
    private RelativeLayout RR_back;
    private ArrayList<BookingDataModel> modelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(
                    this, R.color.mehroon));
        }

        progressBar=findViewById(R.id.progressBar);
        txt_empty=findViewById(R.id.txt_empty);
        recycler_booking_history=findViewById(R.id.recycler_booking_history);
        RR_back=findViewById(R.id.RR_back);
        preference = new Preference(this);

        RR_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });

        if (preference.isNetworkAvailable()) {

            progressBar.setVisibility(View.VISIBLE);

            getBookingHistory();

        }else {

            Toast.makeText(this, R.string.checkInternet, Toast.LENGTH_SHORT).show();
        }

    }

    public void getBookingHistory(){

       String UerId = Preference.get(BookingHistoryActivity.this,Preference.KEY_USER_ID);


        HashMap<String, String> map = new HashMap<>();

        map.put("user_id",UerId);

        ApiRequest apiRequest = new ApiRequest(BookingHistoryActivity.this,this);

        apiRequest.postRequest(Constants.BASE_URL + Constants.USER_get_my_order, Constants.USER_get_my_order,map, Request.Method.POST);
    }


    @Override
    public void onResultReceived(String response, String tag_json_obj) {

        if (Constants.USER_get_my_order.equalsIgnoreCase(tag_json_obj)){

            progressBar.setVisibility(View.GONE);

            if (response !=null)  {
                try {
                    txt_empty.setVisibility(View.GONE);
                    JSONObject jsonObject = new JSONObject(response);

                    String status = jsonObject.getString("status").toString();

                    if(status.equalsIgnoreCase("1"))
                    {
                        BookingModel finalArray = new Gson().fromJson(response,new TypeToken<BookingModel>(){}.getType());

                        modelList = (ArrayList<BookingDataModel>) finalArray.getResult();

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

    private void setAdapter(ArrayList<BookingDataModel> modelList) {
/*
        modelList.add(new SaloonShopListModel("John Smith","4.3",R.drawable.haircut_img));
        modelList.add(new SaloonShopListModel("Olivia Parker","3.3",R.drawable.saloon_img));
        modelList.add(new SaloonShopListModel("John Smith","4.6",R.drawable.haircut_img));*/

        mAdapter = new BookingAdapter(BookingHistoryActivity.this, this.modelList);
        recycler_booking_history.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(BookingHistoryActivity.this);
        recycler_booking_history.setLayoutManager(linearLayoutManager);
        recycler_booking_history.setAdapter(mAdapter);

        mAdapter.SetOnItemClickListener(new BookingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, BookingDataModel model) {


            }
        });
    }

}